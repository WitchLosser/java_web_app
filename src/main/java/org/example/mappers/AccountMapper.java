package org.example.mappers;


import org.example.dto.auth.RegisterDTO;
import org.example.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "password", ignore = true)
    UserEntity itemDtoToUser(RegisterDTO registerDTO);
}
