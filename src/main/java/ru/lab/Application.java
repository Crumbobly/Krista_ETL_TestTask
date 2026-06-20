package ru.lab;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab.config.DBConfig;
import ru.lab.config.ObjectMapperFactory;
import ru.lab.database.impl.DatabaseWorkerImpl;
import ru.lab.dto.EBudgetResponseDto;
import ru.lab.dto.ResponseDto;
import ru.lab.service.EBudgetApiService;
import ru.lab.service.EBudgetFlattenerService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Application {

    private final static Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        final DatabaseWorkerImpl databaseWorker = new DatabaseWorkerImpl();
        databaseWorker.initialize();

        final ObjectMapper mapper = ObjectMapperFactory.getMapper();
        final EBudgetApiService apiService = new EBudgetApiService();
        final EBudgetFlattenerService flattenerService = new EBudgetFlattenerService();

        final LocalDate from = LocalDate.of(2025, 11, 11);
        final LocalDate to = LocalDate.of(2025, 11, 12);

        int page = 1;
        while (true){
            LOGGER.info("Выполнение запроса, page {}", page);
            final String responseRaw = apiService.getPage(from, to, page);

            try {
                final ResponseDto responseDto = mapper.readValue(responseRaw, ResponseDto.class);
                final List<EBudgetResponseDto> eBudgetResponseDtoList = responseDto.getData();

                for (EBudgetResponseDto eBudgetResponseDto : eBudgetResponseDtoList) {
                    Map<String, Object> flatDto = flattenerService.flat(eBudgetResponseDto);

                    // сделать меньше запросов
                    databaseWorker.addMissingColumns(flatDto.keySet());
                }


                LOGGER.info("Запрос выполнен, page {}, total {}", page, responseDto.getPageCount());
                if (responseDto.getPageCount() == page){
                    break;
                }
                else{
                    page += 1;
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }




    }
}
