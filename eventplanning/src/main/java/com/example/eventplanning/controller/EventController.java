package com.example.eventplanning.controller;

import com.example.eventplanning.dto.EventCreationDto;
import com.example.eventplanning.dto.EventResponseDto;
import com.example.eventplanning.model.Event;
import com.example.eventplanning.model.Organizer;
import com.example.eventplanning.service.EventService;
import com.example.eventplanning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @PostMapping("/create")
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventCreationDto dto, Authentication authentication) {
        String username = authentication.getName();
        Event event = eventService.createEvent(dto, username);
        EventResponseDto responseDto = eventService.convertToDto(event);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/signup/{eventId}")
    public ResponseEntity<Void> signUpToEvent(@PathVariable Long eventId, Authentication authentication) {
        String username = authentication.getName();
        eventService.signUpToEvent(eventId, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        List<EventResponseDto> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }



}
