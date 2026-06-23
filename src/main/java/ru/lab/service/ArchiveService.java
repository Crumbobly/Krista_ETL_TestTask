package ru.lab.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab.config.ArchiveConfig;
import ru.lab.config.XmlMapperFactory;
import ru.lab.dto.LoadContextDto;
import ru.lab.dto.XmlDto;
import ru.lab.dto.response.EBudgetResponseDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArchiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchiveService.class);

    private static final XmlMapper xmlMapper = XmlMapperFactory.getMapper();

    /**
     * Сохраняет сырой JSON-ответ страницы во временную директорию.
     */
    public void saveJson(int page, LoadContextDto loadContextDto, String responseRaw) {

        final Path dir = getTmpDirectoryPath(loadContextDto);
        final Path file = dir.resolve("page_" + page + ".json");

        try {
            Files.write(file, responseRaw.getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Сохраняет страницу данных в XML-файл во временную директорию.
     */
    public void saveXml(int page, LoadContextDto loadContextDto, List<EBudgetResponseDto> responseDtoLst) {

        final XmlDto.Meta meta = new XmlDto.Meta();
        meta.page = page;
        meta.from = loadContextDto.getFrom().toString();
        meta.to = loadContextDto.getTo().toString();

        final XmlDto xml = new XmlDto();
        xml.meta = meta;
        xml.data = responseDtoLst;

        final Path dir = getTmpDirectoryPath(loadContextDto);
        final Path file = dir.resolve("page_" + page + ".xml");
        try {
            xmlMapper.writeValue(file.toFile(), xml);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Создаёт zip-архив с содержимым временной директории.
     *
     * @param deleteTmpFolder удалить ли временную директорию после архивирования
     */
    public void zip(LoadContextDto loadContextDto, boolean deleteTmpFolder) {

        final Path dir = getTmpDirectoryPath(loadContextDto);
        final Path zipFile = Paths.get(dir + ".zip");
        LOGGER.info("Запуск создания архива...");

        if(Files.exists(zipFile)) {
            try {
                Files.delete(zipFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile))) {

            try (Stream<Path> files = Files.list(dir)) {

                files.forEach(file -> {
                    try {
                        zos.putNextEntry(new ZipEntry(file.getFileName().toString()));
                        Files.copy(file, zos);
                        zos.closeEntry();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            LOGGER.info("Архив {} создан", zipFile);
            if (deleteTmpFolder) {
                deleteTmpDirectory(loadContextDto);
                LOGGER.info("Временная папка удалена");
                return;
            }
            LOGGER.info("Временная папка сохранена");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создаёт временную директорию для текущего диапазона дат.
     */
    public void createTmpDirectory(LoadContextDto loadContextDto){
        final Path dir = getTmpDirectoryPath(loadContextDto);
        try {
            Files.createDirectories(dir);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает путь к директории текущей выгрузки.
     */
    private Path getTmpDirectoryPath(LoadContextDto loadContextDto) {
        return Paths.get(ArchiveConfig.ARCHIVE_FOLDER, loadContextDto.getFrom() + "_" + loadContextDto.getTo());
    }

    /**
     * Удаляет содержимое временной директории и её саму.
     */
    public void deleteTmpDirectory(LoadContextDto loadContextDto) {

        final Path dir = getTmpDirectoryPath(loadContextDto);
        if (!dir.toFile().exists()) {
            return;
        }

        try (Stream<Path> paths = Files.walk(dir)) {

            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
