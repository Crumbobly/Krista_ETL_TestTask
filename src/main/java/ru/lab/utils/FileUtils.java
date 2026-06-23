package ru.lab.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    private static byte[] toByteArray(InputStream is) throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int nRead;

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }

    public static String readFile(String path){
        try (final InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(path)) {

            if (inputStream == null) {
                throw new RuntimeException(path + " not found");
            }
            return new String(toByteArray(inputStream), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
