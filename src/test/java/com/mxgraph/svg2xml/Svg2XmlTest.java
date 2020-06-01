package com.mxgraph.svg2xml;

import static com.mxgraph.utils.FileUtils.EOL;
import static com.mxgraph.utils.FileUtils.fileContent;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Arrays;

class Svg2XmlTest {

    @Test
    void convertToXml_single_file() {
        Svg2Xml svg2Xml = new Svg2Xml();

        File destPath = destinationFolder("Simple-Single file");
        svg2Xml.convertToXml(svgSourceFiles("simple-01/circle-green.svg"), destPath);

        File expectedGeneratedFile = new File(destPath, "circle-green.xml");
        assertThat(expectedGeneratedFile).isFile();
        String fileContent = fileContent(expectedGeneratedFile);
        assertThat(fileContent).startsWith("<shapes name=\"mxgraph.simple-01\">");
        assertThat(fileContent).contains(
                "<fillcolor color=\"green\"/>",
                "<ellipse h=\"200\" w=\"200\" x=\"0\" y=\"0\"/>"
        );
    }

    @Test
    void convertToXml_two_files_from_the_same_folder() {
        Svg2Xml svg2Xml = new Svg2Xml();

        File destPath = destinationFolder("Simple-Two files");
        svg2Xml.convertToXml(svgSourceFiles("simple-01/circle-green.svg", "simple-01/rectangle-blue.svg"), destPath);

        File expectedGeneratedFile = new File(destPath, "rectangle-blue.xml"); // use base name of the latest svg file in the source folder
        assertThat(expectedGeneratedFile).isFile();
        String fileContent = fileContent(expectedGeneratedFile);
        assertThat(fileContent).startsWith("<shapes name=\"mxgraph.simple-01\">");
        assertThat(fileContent).contains(
                // 1st shape
                "<shape aspect=\"variable\" h=\"200\" name=\"circle-green\"",
                // 2nde shape
                "<shape aspect=\"variable\" h=\"100\" name=\"rectangle-blue\""
        );
    }

    @Test
    void convertToXml_files_from_two_folders_without_subfolders_files_given_ordered_by_folders() {
        Svg2Xml svg2Xml = new Svg2Xml();

        File destPath = destinationFolder("files from 2 folders - no subfolders");
        // in the current implementation, the files are supposed to be passed ordered by folder
        svg2Xml.convertToXml(svgSourceFiles("simple-01/circle-green.svg", "simple-01/rectangle-blue.svg", "simple-02/path-blue.svg"), destPath);


        // File generated from source files in 'simple-01'
        File expected1stGeneratedFile = new File(destPath, "rectangle-blue.xml");
        assertThat(expected1stGeneratedFile).isFile();
        String contentOfFirstFile = fileContent(expected1stGeneratedFile);
        assertThat(contentOfFirstFile).startsWith("<shapes name=\"mxgraph.simple-01\">");

        // File generated from source files in 'simple-02'
        File expected2ndGeneratedFile = new File(destPath, "path-blue.xml");
        assertThat(expected2ndGeneratedFile).isFile();
        String contentOf2ndFile = fileContent(expected2ndGeneratedFile);
        assertThat(contentOf2ndFile).startsWith("<shapes name=\"mxgraph.simple-02\">");
        assertThat(contentOf2ndFile).describedAs("Content of the 2nd generated file").contains(
                "<quad x1=\"20\" x2=\"30\" y1=\"0\" y2=\"25\"/>",
                "<quad x1=\"40\" x2=\"70\" y1=\"50\" y2=\"25\"/>"
        );
    }

    // =================================================================================================================
    // UTILS
    // =================================================================================================================

    private static void assertFirstLine(String fileContent, String expectedStart, String expectedEnd) {
        String firstLine = fileContent.substring(0, fileContent.indexOf(EOL));
        assertThat(firstLine).describedAs("1st line of the generated file")
                .startsWith(expectedStart)
                .endsWith(expectedEnd);
    }

    private static File[] svgSourceFiles(String... fileNames) {
        File parent = new File(System.getProperty("user.dir"), "src/test/resources/svg"); // ensure we pass absolute path
        return Arrays.stream(fileNames)
                .map(fileName -> new File(parent, fileName))
                .toArray(File[]::new);
    }

    private static File destinationFolder(String folderName) {
        return new File("target/test/output/", folderName);
    }

}