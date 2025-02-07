package com.example.eventplanning.service;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.eventplanning.controller.EventController;
import com.example.eventplanning.dto.EventCreationDto;
import com.example.eventplanning.dto.EventResponseDto;
import com.example.eventplanning.exception.UnauthorizedException;
import com.example.eventplanning.model.Event;
import com.example.eventplanning.model.Organizer;
import com.example.eventplanning.model.Participant;
import com.example.eventplanning.repository.EventRepository;
import com.example.eventplanning.repository.OrganizerRepository;
import com.example.eventplanning.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final OrganizerRepository organizerRepository;
    private final ParticipantRepository participantRepository;
    private final EmailService emailService;

    @Autowired
    public EventService(EventRepository eventRepository, OrganizerRepository organizerRepository, ParticipantRepository participantRepository, EmailService emailService) {
        this.eventRepository = eventRepository;
        this.organizerRepository = organizerRepository;
        this.participantRepository = participantRepository;
        this.emailService = emailService;
    }

    public Event createEvent(EventCreationDto dto, String username) {
        if (participantRepository.findByUsername(username).isPresent()) {
            throw new UnauthorizedException("Only organizers can create events");
        }

        Organizer organizer = organizerRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        Event event = new Event();
        event.setName(dto.getName());
        event.setDate(dto.getDate());
        event.setLocation(dto.getLocation());
        event.setOrganizer(organizer);
        return eventRepository.save(event);
    }

    public void signUpToEvent(Long eventId, String username) {
        Participant participant = participantRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        event.getParticipants().add(participant);
        eventRepository.save(event);

        CompletableFuture<Void> emailFuture = emailService.sendConfirmationEmail(participant.getEmail(), event.getName());
        emailFuture.thenAccept(aVoid -> System.out.println("Email sent successfully"))
                .exceptionally(throwable -> {
                    System.out.println("Failed to send email: " + throwable.getMessage());
                    return null;
                });
    }

    public List<EventResponseDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EventResponseDto convertToDto(Event event) {
        EventResponseDto dto = new EventResponseDto();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDate(event.getDate());
        dto.setLocation(event.getLocation());
        dto.setOrganizerName(event.getOrganizer().getUsername());
        dto.setParticipants(event.getParticipants().stream().map(Participant::getUsername).toList());
        return dto;
    }


}
