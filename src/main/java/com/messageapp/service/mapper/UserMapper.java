package com.messageapp.service.mapper;

import com.messageapp.domain.Authority;
import com.messageapp.domain.User;
import com.messageapp.repository.UserRepository;
import com.messageapp.service.dto.UserDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity User and its DTO UserDTO.
 */
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    UserRepository userRepository;

    public UserDTO userToUserDTO(User user) {
        return new UserDTO(user);
    }


    abstract List<UserDTO> usersToUserDTOs(List<User> users);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "persistentTokens", ignore = true)
    @Mapping(target = "activationKey", ignore = true)
    @Mapping(target = "resetKey", ignore = true)
    @Mapping(target = "resetDate", ignore = true)
    @Mapping(target = "password", ignore = true)
    public abstract User userDTOToUser(UserDTO userDTO);

    public abstract List<User> userDTOsToUsers(List<UserDTO> userDTOs);

    public User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    Set<Authority> authoritiesFromStrings(Set<String> strings) {
        return strings.stream().map(string -> {
            Authority auth = new Authority();
            auth.setName(string);
            return auth;
        }).collect(Collectors.toSet());
    }

    User toUser(String userlogin){
        return userRepository.findOneByLogin(userlogin).get();
    }


}
