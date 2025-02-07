package com.example.eventplanning;

import com.example.eventplanning.dto.EventResponseDto;
import com.example.eventplanning.exception.UnauthorizedException;
import com.example.eventplanning.model.Event;
import com.example.eventplanning.model.Organizer;
import com.example.eventplanning.model.Participant;
import com.example.eventplanning.repository.EventRepository;
import com.example.eventplanning.repository.OrganizerRepository;
import com.example.eventplanning.repository.ParticipantRepository;
import com.example.eventplanning.service.EmailService;
import com.example.eventplanning.service.EventService;
import com.example.eventplanning.dto.EventCreationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class EventServiceTest {
        @Mock
        private EventRepository eventRepository;

        @Mock
        private OrganizerRepository organizerRepository;

        @Mock
        private ParticipantRepository participantRepository;

        @Mock
        private EmailService emailService;

        @InjectMocks
        private EventService eventService;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testCreateEvent() {
            Organizer organizer = new Organizer();
            organizer.setUsername("organizer");

            when(organizerRepository.findByUsername("organizer")).thenReturn(Optional.of(organizer));

            Event event = new Event();
            event.setName("Event");
            when(eventRepository.save(any(Event.class))).thenReturn(event);

            EventCreationDto dto = new EventCreationDto();
            dto.setName("Event");

            Event createdEvent = eventService.createEvent(dto, "organizer");

            assertNotNull(createdEvent);
            assertEquals("Event", createdEvent.getName());
        }

        @Test
        void testSignUpToEvent() {
            Participant participant = new Participant();
            participant.setUsername("participant");
            participant.setEmail("participant@example.com");

            Event event = new Event();
            event.setName("Event");

            when(participantRepository.findByUsername("participant")).thenReturn(Optional.of(participant));
            when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
            when(emailService.sendConfirmationEmail(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(null));

            eventService.signUpToEvent(1L, "participant");

            verify(eventRepository, times(1)).save(event);
            verify(emailService, times(1)).sendConfirmationEmail("participant@example.com", "Event");
        }

    @Test
    void testConvertToDto() {
        Organizer organizer = new Organizer();
        organizer.setUsername("organizer");

        Participant participant1 = new Participant();
        participant1.setUsername("participant1");

        Participant participant2 = new Participant();
        participant2.setUsername("participant2");

        Date date = Date.from(Instant.parse("2023-12-31T10:15:30.00Z"));

        Event event = new Event();
        event.setId(1L);
        event.setName("Event Name");
        event.setDate(date);
        event.setLocation("Event Location");
        event.setOrganizer(organizer);
        event.setParticipants(List.of(participant1, participant2));

        EventResponseDto dto = eventService.convertToDto(event);

        assertEquals(1L, dto.getId());
        assertEquals("Event Name", dto.getName());
        assertEquals(date, dto.getDate());
        assertEquals("Event Location", dto.getLocation());
        assertEquals("organizer", dto.getOrganizerName());
        assertEquals(Set.of("participant1", "participant2"), Set.copyOf(dto.getParticipants()));
    }

    @Test
    void testCreateEventThrowsUnauthorizedException() {
        String username = "participant";
        EventCreationDto dto = new EventCreationDto();
        dto.setName("Event");

        when(participantRepository.findByUsername(username)).thenReturn(Optional.of(new Participant()));

        assertThrows(UnauthorizedException.class, () -> eventService.createEvent(dto, username));
    }

    @Test
    void testSignUpToEventEmailFailure() {
        Participant participant = new Participant();
        participant.setUsername("participant");
        participant.setEmail("participant@example.com");

        Event event = new Event();
        event.setName("Event");

        when(participantRepository.findByUsername("participant")).thenReturn(Optional.of(participant));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(emailService.sendConfirmationEmail(anyString(), anyString()))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Email service failure")));

        eventService.signUpToEvent(1L, "participant");

        verify(eventRepository, times(1)).save(event);
        verify(emailService, times(1)).sendConfirmationEmail("participant@example.com", "Event");
    }

    @Test
    void testGetAllEvents() {
        Organizer organizer = new Organizer();
        organizer.setUsername("organizer");

        Participant participant = new Participant();
        participant.setUsername("participant");

        Event event = new Event();
        event.setId(1L);
        event.setName("Event Name");
        event.setDate(new Date());
        event.setLocation("Event Location");
        event.setOrganizer(organizer);
        event.setParticipants(List.of(participant));

        when(eventRepository.findAll()).thenReturn(List.of(event));

        List<EventResponseDto> eventResponseDtos = eventService.getAllEvents();

        assertEquals(1, eventResponseDtos.size());
        EventResponseDto dto = eventResponseDtos.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("Event Name", dto.getName());
        assertEquals("Event Location", dto.getLocation());
        assertEquals("organizer", dto.getOrganizerName());
        assertEquals(List.of("participant"), dto.getParticipants());
    }
}
