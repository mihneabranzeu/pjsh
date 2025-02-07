package com.example.eventplanning.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Participant extends User {
    @ElementCollection
    private Set<Hobby> hobbies;

    @ManyToMany(mappedBy = "participants")
    private List<Event> events;
}
