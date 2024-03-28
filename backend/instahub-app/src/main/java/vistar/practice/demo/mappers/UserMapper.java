package vistar.practice.demo.mappers;

import vistar.practice.demo.dtos.user.UserRequestDto;
import vistar.practice.demo.dtos.user.UserResponseDto;
import vistar.practice.demo.models.UserEntity;

public class UserMapper {
    private UserMapper(){}

    public static UserResponseDto toInfoDto(UserEntity user){
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

    public static UserEntity toEntity(UserRequestDto user){
        return UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .patronymic(user.getPatronymic())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
