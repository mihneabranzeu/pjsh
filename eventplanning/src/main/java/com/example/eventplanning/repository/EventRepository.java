package com.example.eventplanning.repository;

import com.example.eventplanning.model.Event;
import com.example.eventplanning.model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
}
