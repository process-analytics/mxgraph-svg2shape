package com.mxgraph.svg2js;

import com.mxgraph.svg2xml.Svg2Xml;
import com.mxgraph.xml2js.Xml2Js;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

import static java.lang.String.format;

public class Svg2Js {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("svg2js requires at least one argument (path to the svg file)");
            System.exit(-1);
        }

        File svg = new File(args[0]);
        File currentDirectory = new File(System.getProperty("user.dir"));
        File destinationDirectory = new File(currentDirectory, "mxgraph-stencil-from-svg_" + System.currentTimeMillis());

        new Svg2Js().process(svg, destinationDirectory);
    }

    public void process(File svg, File tempDirectory) throws IOException {
        logInfo("Start generating mxgraph JS code from SVG " + svg);
        logInfo("mxgraph stencil will be generated into " + tempDirectory);

        Svg2Xml svg2Xml = new Svg2Xml();
        svg2Xml.convertToXml(new File[]{svg}, tempDirectory);
        logInfo("SVG converted into mxgraph stencil");

        File expectedGeneratedStencilFile = new File(tempDirectory, FilenameUtils.getBaseName(svg.getName()) + ".xml");
        if (!expectedGeneratedStencilFile.isFile()) {
            throw new IOException(expectedGeneratedStencilFile + " has not been generated, see logs below");
        }

        logInfo("Converting SVG into JS code");
        String code = new Xml2Js().infoLog(true).debugLog(false).parse(expectedGeneratedStencilFile);
        System.out.println(code);
        System.out.println();
        FileUtils.deleteQuietly(tempDirectory);
        logInfo("mxgraph JS code from SVG produced");
    }

    private boolean isInfoLogActivated = true;
    private void logInfo(String msg) {
        if (isInfoLogActivated) {
            System.out.println(format("Svg2Js [INFO] %s", msg));
        }
    }

}
