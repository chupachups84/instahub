package vistar.practice.demo.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import vistar.practice.demo.dtos.authentication.RegisterDto;
import vistar.practice.demo.dtos.user.UserResponseDto;
import vistar.practice.demo.models.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    UserResponseDto toDto(UserEntity user);

    UserEntity toEntity(RegisterDto registerDto);

}
