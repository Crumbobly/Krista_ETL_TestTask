package ru.lab;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab.dto.LoadContextDto;
import ru.lab.service.LoaderService;

import java.time.LocalDate;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        LocalDate from;
        LocalDate to;
        int pageSize;

        if (args.length >= 2) {
            from = LocalDate.parse(args[0]);
            to = LocalDate.parse(args[1]);
            pageSize = Integer.parseInt(args[2]);
        }
        else{
            from = LocalDate.of(2025, 11, 12);
            to = LocalDate.of(2025, 11, 19);
            pageSize = 1000;
            LOGGER.info("Аргументы не были переданы, взяты значения по умолчанию.");
        }

        final LoadContextDto loadContextDto = new LoadContextDto(from, to, pageSize);
        LoaderService loaderService = new LoaderService();
        loaderService.load(loadContextDto);
    }
}
