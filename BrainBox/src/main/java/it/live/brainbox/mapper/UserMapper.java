package it.live.brainbox.mapper;

import it.live.brainbox.entity.User;
import it.live.brainbox.entity.enums.SystemRoleName;
import it.live.brainbox.payload.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public User toEntity(UserDTO userDTO) {
        return User.builder().coins(300).imageUrl(userDTO.getImageUrl()).email(userDTO.getEmail()).systemRoleName(SystemRoleName.ROLE_USER).enabled(true).isAccountNonExpired(true).isAccountNonLocked(true).isCredentialsNonExpired(true).name(userDTO.getName()).surname(userDTO.getSurname()).uniqueId(userDTO.getUniqueId()).build();
    }
}
