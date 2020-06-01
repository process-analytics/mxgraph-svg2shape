package com.mxgraph.svg2xml;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

class Svg2XmlTest {

    @Test
    void convertToXml_single_file() {
        Svg2Xml svg2Xml = new Svg2Xml();

        File destPath = destinationFolder("Simple-Single file");
        svg2Xml.convertToXml(svgSourceFiles("simple/circle-green.svg"), destPath);

        File expectedGeneratedFile = new File(destPath, "circle-green.xml");
        assertThat(expectedGeneratedFile).isFile();
        assertThat(fileContent(expectedGeneratedFile)).contains(
                "<fillcolor color=\"green\"/>",
                "<ellipse h=\"200\" w=\"200\" x=\"0\" y=\"0\"/>"
        );
    }

    @Test
    void convertToXml_two_files_from_the_same_folder() {
        Svg2Xml svg2Xml = new Svg2Xml();

        File destPath = destinationFolder("Simple-Two files");
        svg2Xml.convertToXml(svgSourceFiles("simple/circle-green.svg", "simple/rectangle-blue.svg"), destPath);

        File expectedGeneratedFile = new File(destPath, "rectangle-blue.xml"); // use base name of the latest svg file in the source folder
        assertThat(expectedGeneratedFile).isFile();
        assertThat(fileContent(expectedGeneratedFile)).contains(
                "<fillcolor color=\"green\"/>",
                "<ellipse h=\"200\" w=\"200\" x=\"0\" y=\"0\"/>"
        );
        // TODO check that the file contains the 2 shapes
    }

    // =================================================================================================================
    // UTILS
    // =================================================================================================================

    private static File[] svgSourceFiles(String... fileNames) {
        File parent = new File(System.getProperty("user.dir"), "src/test/resources/svg"); // ensure we pass absolute path
        return Arrays.stream(fileNames)
                .map(fileName -> new File(parent, fileName))
                .toArray(File[]::new);
    }

    private static File destinationFolder(String folderName) {
        return new File("target/test/output/", folderName);
    }

    // when switching to JDK11+, use Files#readString instead
    public static String fileContent(File file) {
        try {
            // we do not care of having a OS related eol as we test line content here
            return lines(file.toPath(), UTF_8).collect(joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read the content of " + file, e);
        }
    }

}