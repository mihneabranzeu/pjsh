package com.example.eventplanning.repository;

import com.example.eventplanning.model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
    Optional<Organizer> findByUsername(String username);
}
