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
import vistar.practice.demo.dtos.token.TokenDto;
import vistar.practice.demo.models.JwtEntity;
import vistar.practice.demo.models.UserEntity;
import vistar.practice.demo.repositories.JwtRepository;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class JwtService {
    @Value("${jwt.signing-key}")
    private String signingKey;

    @Value("${jwt.expiration.access-token}")
    private Long accessTokenExpiration;

    @Value("${jwt.expiration.refresh-token}")
    private Long refreshTokenExpiration;

    private final JwtRepository jwtRepository;


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

    public void revokeAllUserToken(UserEntity user) {
        jwtRepository.findAllValidTokensByUser(user.getId())
                .filter(tokens -> !tokens.isEmpty())
                .ifPresent(
                        tokenList -> tokenList.forEach(
                                t -> t.setRevoked(true)
                        )
                );
    }

    public void saveUserToken(String token, UserEntity user) {
        jwtRepository.save(
                JwtEntity.builder()
                        .token(token)
                        .user(user)
                        .build()
        );
    }

    @Transactional(readOnly = true,transactionManager = "transactionManager")
    public boolean isTokenRevoked(String token) {
        return jwtRepository.findByToken(token).filter(JwtEntity::isRevoked).isPresent();
    }

    public TokenDto getTokenDtoByUser(UserEntity user) {
        var accessToken = generateAccessToken(user);
        var refreshToken = generateRefreshToken(user);
        saveUserToken(accessToken, user);
        saveUserToken(refreshToken, user);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
