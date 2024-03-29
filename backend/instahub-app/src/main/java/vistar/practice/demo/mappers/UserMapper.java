package vistar.practice.demo.mappers;

import vistar.practice.demo.dtos.authentication.RegisterDto;
import vistar.practice.demo.dtos.user.UserResponseDto;
import vistar.practice.demo.models.UserEntity;

public class UserMapper {

    private UserMapper() {
    }

    public static UserResponseDto toInfoDto(UserEntity user) {
        return UserResponseDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .patronymic(user.getPatronymic())
                .createdAt(user.getCreatedAt())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    public static UserEntity toEntity(RegisterDto registerDto){
        return UserEntity.builder()
                .firstName(registerDto.getFirstName())
                .middleName(registerDto.getMiddleName())
                .email(registerDto.getEmail())
                .username(registerDto.getUsername())
                .patronymic(registerDto.getPatronymic())
                .lastName(registerDto.getLastName())
                .password(registerDto.getPassword())
                .build();
    }

}
