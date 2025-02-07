package com.example.eventplanning.service;

import com.example.eventplanning.config.JwtUtil;
import com.example.eventplanning.dto.UserLoginDto;
import com.example.eventplanning.dto.UserRegistrationDto;
import com.example.eventplanning.model.Organizer;
import com.example.eventplanning.model.Participant;
import com.example.eventplanning.model.UserType;
import com.example.eventplanning.repository.OrganizerRepository;
import com.example.eventplanning.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final OrganizerRepository organizerRepository;
    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(OrganizerRepository organizerRepository,
                       ParticipantRepository participantRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.organizerRepository = organizerRepository;
        this.participantRepository = participantRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String loginUser(UserLoginDto dto) {
        Optional<Organizer> organizer = organizerRepository.findByUsername(dto.getUsername());
        if (organizer.isPresent() && passwordEncoder.matches(dto.getPassword(), organizer.get().getPassword())) {
            return jwtUtil.generateToken(dto.getUsername()); // Generate JWT for organizer
        }

        Optional<Participant> participant = participantRepository.findByUsername(dto.getUsername());
        if (participant.isPresent() && passwordEncoder.matches(dto.getPassword(), participant.get().getPassword())) {
            return jwtUtil.generateToken(dto.getUsername()); // Generate JWT for participant
        }

        throw new IllegalArgumentException("Invalid username or password");
    }

    public void registerUser(UserRegistrationDto dto) {
        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        if (dto.getUserType() == UserType.ORGANIZER) {
            Organizer organizer = new Organizer();
            organizer.setUsername(dto.getUsername());
            organizer.setPassword(hashedPassword);
            organizer.setEmail(dto.getEmail());
            organizer.setOrganizationName(dto.getOrganizationName());
            organizerRepository.save(organizer);
        } else if (dto.getUserType() == UserType.PARTICIPANT) {
            Participant participant = new Participant();
            participant.setUsername(dto.getUsername());
            participant.setPassword(hashedPassword);
            participant.setEmail(dto.getEmail());
            participant.setHobbies(dto.getHobbies());
            participantRepository.save(participant);
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
    }

    public Optional<Organizer> getOrganizerByUsername(String username) {
        return organizerRepository.findByUsername(username);
    }
}
