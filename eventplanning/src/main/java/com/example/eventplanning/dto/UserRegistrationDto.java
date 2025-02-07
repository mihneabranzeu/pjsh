package com.example.eventplanning.dto;

import com.example.eventplanning.model.Hobby;
import com.example.eventplanning.model.UserType;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserRegistrationDto {
    private String username;
    private String password;
    private String email;
    private UserType userType;
    private Set<Hobby> hobbies;
    private String organizationName;
}
