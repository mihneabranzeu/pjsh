package com.example.eventplanning;

import com.example.eventplanning.config.JwtUtil;
import com.example.eventplanning.dto.UserLoginDto;
import com.example.eventplanning.dto.UserRegistrationDto;
import com.example.eventplanning.model.Organizer;
import com.example.eventplanning.model.Participant;
import com.example.eventplanning.model.UserType;
import com.example.eventplanning.repository.OrganizerRepository;
import com.example.eventplanning.repository.ParticipantRepository;
import com.example.eventplanning.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private OrganizerRepository organizerRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginUserAsOrganizer() {
        UserLoginDto dto = new UserLoginDto();
        dto.setUsername("organizer");
        dto.setPassword("password");

        Organizer organizer = new Organizer();
        organizer.setUsername("organizer");
        organizer.setPassword("encodedPassword");

        when(organizerRepository.findByUsername("organizer")).thenReturn(Optional.of(organizer));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("organizer")).thenReturn("token");

        String token = userService.loginUser(dto);

        assertEquals("token", token);
    }

    @Test
    void testLoginUserAsParticipant() {
        UserLoginDto dto = new UserLoginDto();
        dto.setUsername("participant");
        dto.setPassword("password");

        Participant participant = new Participant();
        participant.setUsername("participant");
        participant.setPassword("encodedPassword");

        when(participantRepository.findByUsername("participant")).thenReturn(Optional.of(participant));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("participant")).thenReturn("token");

        String token = userService.loginUser(dto);

        assertEquals("token", token);
    }

    @Test
    void testLoginUserInvalidCredentials() {
        UserLoginDto dto = new UserLoginDto();
        dto.setUsername("user");
        dto.setPassword("password");

        when(organizerRepository.findByUsername("user")).thenReturn(Optional.empty());
        when(participantRepository.findByUsername("user")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.loginUser(dto));
    }

    @Test
    void testRegisterUserAsOrganizer() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("organizer");
        dto.setPassword("password");
        dto.setEmail("organizer@example.com");
        dto.setUserType(UserType.ORGANIZER);
        dto.setOrganizationName("OrgName");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        userService.registerUser(dto);

        verify(organizerRepository, times(1)).save(any(Organizer.class));
    }

    @Test
    void testRegisterUserAsParticipant() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("participant");
        dto.setPassword("password");
        dto.setEmail("participant@example.com");
        dto.setUserType(UserType.PARTICIPANT);
        dto.setHobbies(Set.of());

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        userService.registerUser(dto);

        verify(participantRepository, times(1)).save(any(Participant.class));
    }

    @Test
    void testRegisterUserInvalidUserType() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("user");
        dto.setPassword("password");
        dto.setEmail("user@example.com");
        dto.setUserType(null);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
    }

    @Test
    void testGetOrganizerByUsername() {
        Organizer organizer = new Organizer();
        organizer.setUsername("organizer");

        when(organizerRepository.findByUsername("organizer")).thenReturn(Optional.of(organizer));

        Optional<Organizer> result = userService.getOrganizerByUsername("organizer");

        assertTrue(result.isPresent());
        assertEquals("organizer", result.get().getUsername());
    }
}