package com.mxgraph.svg2xml;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

class Svg2XmlTest {

    @Test
    void convertToXml() {
        Svg2Xml svg2Xml = new Svg2Xml();

        File destPath = destinationFolder("simple-conversion");
        svg2Xml.convertToXml(new File [] {svgSourceFile("simple/green-circle.svg")}, destPath);

        File expectedGeneratedFile = new File(destPath, "green-circle.xml");
        assertThat(expectedGeneratedFile).isFile();
        assertThat(fileContent(expectedGeneratedFile)).contains(
                "<fillcolor color=\"green\"/>",
                "<ellipse h=\"200\" w=\"200\" x=\"0\" y=\"0\"/>"
        );
    }

    // =================================================================================================================
    // UTILS
    // =================================================================================================================

    private static File svgSourceFile(String fileName) {
        return new File("src/test/resources/svg", fileName);
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