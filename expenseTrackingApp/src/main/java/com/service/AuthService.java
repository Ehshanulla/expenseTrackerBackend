package com.service;



import com.entities.User;
import com.entities.UserRole;
import com.eventproducer.UserInfoEvent;
import com.eventproducer.UserInfoProducer;
import com.models.UserInfoDto;
import com.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoProducer userInfoProducer;


    // SIGNUP
    public Boolean signup(UserInfoDto userInfo) {

        if (userRepository.findByUsername(userInfo.getUsername()).isPresent()) {
            return false;
        }

        String userId = UUID.randomUUID().toString();
        Set<UserRole> roles = userInfo.getRoles();
        Set<UserRole> updatedRoles;
        if(Objects.nonNull(userInfo.getRoles())) updatedRoles = roles.stream().map(role-> UserRole.builder().roleId(role.getRoleId()).name(role.getName().toUpperCase()).build()).collect(Collectors.toSet());
        else updatedRoles = new HashSet<>();
        userInfo.setRoles(updatedRoles);

        userInfoProducer.sendEventToKafka(convertToUserInfoEvent(userInfo,userId));
        userRepository.save(User.builder().
                username(userInfo.getUsername()).
                userId(userId).
                password(passwordEncoder.encode(userInfo.getPassword())).
                roles(updatedRoles).build());


        return true;
    }

    private UserInfoEvent convertToUserInfoEvent(UserInfoDto userInfo,String userId) {
        return UserInfoEvent.builder().email(userInfo.getEmail()).firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName()).phoneNumber(userInfo.getPhoneNumber()).userId(userId).build();
    }

}
