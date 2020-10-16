package com.mxgraph;

import java.io.File;
import java.util.Arrays;

public class FileTooling {

    public static File[] svgSourceFiles(String... fileNames) {
        File parent = new File(System.getProperty("user.dir"), "src/test/resources/svg"); // ensure we pass absolute path
        return Arrays.stream(fileNames)
                .map(fileName -> new File(parent, fileName))
                .toArray(File[]::new);
    }

    public static File svgSourceFile(String fileName) {
        return svgSourceFiles(fileName)[0];
    }

    public static File destinationFolder(String folderName) {
        return new File("target/test/output/", folderName);
    }

}
