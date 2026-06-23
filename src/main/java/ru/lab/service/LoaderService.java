package ru.lab.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab.config.ObjectMapperFactory;
import ru.lab.database.ClickhouseWorker;
import ru.lab.database.PostgresWorker;
import ru.lab.dto.LoadContextDto;
import ru.lab.dto.response.EBudgetResponseDto;
import ru.lab.dto.response.ResponseDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Основной класс приложения.
 * Вся логика тут.
 */
public class LoaderService {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoaderService.class);

    private final ObjectMapper mapper = ObjectMapperFactory.getMapper();
    private final PostgresWorker postgresWorker = new PostgresWorker();
    private final ClickhouseWorker clickhouseWorker = new ClickhouseWorker();
    private final EBudgetApiService apiService = new EBudgetApiService();
    private final ArchiveService archiveService = new ArchiveService();

    public void load(LoadContextDto loadContextDto) {

        // Это поле нужно, чтобы понять, какие данные нужно перегрузить из Postgres в Clickhouse.
        // В идеале оно должно где-то храниться (типа последнее время синхронизации), но в качестве примера сделаем так.
        final LocalDateTime now = LocalDateTime.now();

        archiveService.deleteTmpDirectory(loadContextDto); // Предварительно очищаем папку, куда будет сохранять ответы.
        postgresWorker.initialize(); // Создаём табличку, если её нет
        clickhouseWorker.initialize(); // Создаём табличку, если её нет

        int page = 1;
        while (true){
            LOGGER.info("Выполнение запроса, страница {}", page);
            final String responseRaw = apiService.getPage(loadContextDto, page);

            try {
                final ResponseDto responseDto = mapper.readValue(responseRaw, ResponseDto.class);
                final List<EBudgetResponseDto> eBudgetResponseDtoList = responseDto.getData();

                archiveService.saveJson(page, loadContextDto, responseRaw);
                archiveService.saveXml(page, loadContextDto, eBudgetResponseDtoList);
                postgresWorker.insertBatch(eBudgetResponseDtoList, loadContextDto.getFrom(), loadContextDto.getTo());

                LOGGER.info("Запрос выполнен, страница {}/{}. Добавлено {} элементов",
                        page, responseDto.getPageCount(), eBudgetResponseDtoList.size());

                if (responseDto.getPageCount() <= page){
                    break;
                }
                else{
                    page += 1;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        archiveService.zip(loadContextDto, false); // Архивируем папку с ответами
        clickhouseWorker.sync(now); // Запускаем синхронизацию с ClickHouse
    }

}
