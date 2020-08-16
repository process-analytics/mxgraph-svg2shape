package com.mxgraph.utils;

import java.io.File;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.joining;

public class FileUtils {

    public static final String EOL = System.lineSeparator();

    // when switching to JDK11+, use Files#readString instead
    public static String fileContent(File file) {
        try {
            return lines(file.toPath(), UTF_8).collect(joining(EOL));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read the content of " + file, e);
        }
    }

}
