package com.example.eventplanning.dto;

import com.example.eventplanning.model.Hobby;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EventCreationDto {
    private String name;
    private String description;
    private String location;
    private Date date;
    private Hobby type;
    private Long organizerId;
}
