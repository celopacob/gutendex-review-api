package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class GutendexApiDto {
    private ArrayList<BookDto> books;
}