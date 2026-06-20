package ru.lab.utils;

import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static String readFile(String path){
        try (final InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(path)) {

            if (inputStream == null) {
                throw new RuntimeException(path + " not found");
            }
            return new String(IOUtils.readAllBytes(inputStream), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
