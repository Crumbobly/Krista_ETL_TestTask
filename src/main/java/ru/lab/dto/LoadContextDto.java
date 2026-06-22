package ru.lab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class LoadContextDto {
    private LocalDate from;
    private LocalDate to;
    private int pageSize;
}
