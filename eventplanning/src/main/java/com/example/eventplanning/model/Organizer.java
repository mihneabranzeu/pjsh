package com.example.eventplanning.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Organizer extends User {
    private String organizationName;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    private List<Event> events;
}

