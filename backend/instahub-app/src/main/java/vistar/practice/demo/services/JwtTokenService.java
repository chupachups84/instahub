package vistar.practice.demo.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.models.JwtTokenEntity;
import vistar.practice.demo.models.UserEntity;
import vistar.practice.demo.repositories.JwtTokenRepository;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    @Value("${security.jwt.signing-key}")
    private String signingKey;

    @Value("${security.jwt.expiration.access-token}")
    private Long accessTokenExpiration;

    @Value("${security.jwt.expiration.refresh-token}")
    private Long refreshTokenExpiration;

    private final JwtTokenRepository jwtTokenRepository;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims,
                              UserDetails userDetails,
                              Long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails user) {
        return (extractUsername(token).equals(user.getUsername())
                && !extractExpiration(token).before(new Date()));
    }


    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(signingKey));
    }

    @Transactional
    public void revokeAllUserToken(UserEntity user){
        jwtTokenRepository.findAllValidTokensByUser(user.getId())
                .filter(tokens->!tokens.isEmpty())
                .ifPresent(
                        tokenList->tokenList.forEach(
                                t->{
                                    t.setExpired(true);
                                    t.setRevoked(true);
                                }
                        )
                );
    }
    @Transactional
    public void saveUserToken(String token,UserEntity user){
        jwtTokenRepository.save(
                JwtTokenEntity.builder()
                        .token(token)
                        .user(user)
                        .build()
        );
    }
    @Transactional(readOnly = true)
    public boolean isTokenRevoked(String token){
        return jwtTokenRepository.findByToken(token).filter(JwtTokenEntity::isRevoked).isPresent();
    }
}
