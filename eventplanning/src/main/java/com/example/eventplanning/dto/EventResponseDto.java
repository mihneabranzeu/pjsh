package com.example.eventplanning.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EventResponseDto {
    private Long id;
    private String name;
    private String location;
    private Date date;
    private String organizerName;
    private List<String> participants;
}