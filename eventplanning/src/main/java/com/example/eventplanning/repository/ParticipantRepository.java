package com.example.eventplanning.repository;

import com.example.eventplanning.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByUsername(String username);
}
