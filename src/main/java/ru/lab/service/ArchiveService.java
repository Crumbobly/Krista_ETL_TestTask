package ru.lab.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final String ARCHIEVE_FOLDER = System.getenv("ARCHIEVE_FOLDER");

    private final XmlMapper xmlMapper = XmlMapperFactory.getMapper();

    public void saveJson(int page, LoadContextDto loadContextDto, String responseRaw) {

        final Path dir = getDirectory(loadContextDto);

        try {
            Files.createDirectories(dir);
            Path file = dir.resolve("page_" + page + ".json");
            Files.write(file, responseRaw.getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveXml(int page, LoadContextDto loadContextDto, List<EBudgetResponseDto> responseDtoLst) {

        final Path dir = getDirectory(loadContextDto);

        try {
            Files.createDirectories(dir);
            final XmlDto xml = new XmlDto();
            final XmlDto.Meta meta = new XmlDto.Meta();
            meta.page = page;
            meta.from = loadContextDto.getFrom().toString();
            meta.to = loadContextDto.getTo().toString();

            xml.meta = meta;
            xml.data = responseDtoLst;

            final Path file = dir.resolve("page_" + page + ".xml");

            xmlMapper.writeValue(file.toFile(), xml);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void zip(LoadContextDto loadContextDto, boolean deleteTmpFolder) {

        final Path dir = getDirectory(loadContextDto);
        final Path zipFile = Paths.get(dir + ".zip");

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
                        zos.putNextEntry(
                                new ZipEntry(
                                        file.getFileName().toString()
                                )
                        );

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

    private Path getDirectory(LoadContextDto loadContextDto) {
        return Paths.get(ARCHIEVE_FOLDER , loadContextDto.getFrom() + "_" + loadContextDto.getTo());
    }

    public void deleteTmpDirectory(LoadContextDto loadContextDto) {

        final Path dir = getDirectory(loadContextDto);
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
