package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class BookDto {
    private int id;
    private String title;
    private ArrayList<AuthorDto> authors;
    private ArrayList<String> languages;
    private int downloadCount;
    private Double rating;
    private ArrayList<String> reviews;
}
