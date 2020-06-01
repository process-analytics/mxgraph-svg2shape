package com.mxgraph.xml2js;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.mxgraph.utils.FileUtils.EOL;
import static com.mxgraph.utils.FileUtils.fileContent;
import static java.lang.String.format;

public class Xml2Js {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("xml2js requires at least one argument (path to xml stencil file)");
            System.exit(-1);
        }

        File source = new File(args[0]);
        String code = new Xml2Js().infoLog(true).debugLog(false).parse(source);
        System.out.println(code);
    }

    private List<String> codeLines;

    private boolean isInfoLogActivated = true;
    private boolean isDebugLogActivated = false;

    public Xml2Js infoLog(boolean activate) {
        this.isInfoLogActivated = activate;
        return this;
    }

    public Xml2Js debugLog(boolean activate) {
        this.isDebugLogActivated = activate;
        return this;
    }

    public String parse(File source) {
        logInfo("Parsing file " + source.getAbsolutePath());
        String fileContent = fileContent(source);
        String code = parse(fileContent);
        logInfo("Parsing completed");
        return code;
    }

    private String parse(String shapeStencilXml) {
        List<String> lines = parseCodeLines(shapeStencilXml);
        return String.join(EOL, lines);
    }

    // visible for testing
    List<String> parseCodeLines(String shapeStencilXml) {
        Document document = parseXml(shapeStencilXml);
        NodeList shapes = document.getElementsByTagName("shape");
        int shapesNumber = shapes.getLength();
        logInfo("Found " + shapesNumber + " shapes");

        codeLines = new ArrayList<>();
        for (int i = 0; i < shapesNumber; i++) {
            parseShape(shapes.item(i));
        }
        return codeLines;
    }


    private static Document parseXml(String xml) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            return docBuilder.parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse xml", e);
        }
    }

    public void parseShape(Node shape) {
        logInfo(format("Parsing shape '%s'", shape.getAttributes().getNamedItem("name").getTextContent()));
        NodeList shapeChildren = shape.getChildNodes();
        for (int i = 0; i < shapeChildren.getLength(); i++) {
            Node item = shapeChildren.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                if (item.getNodeName().equals("foreground")) {
                    parseForeground(item);
                }
            }
        }
    }

    private void parseForeground(Node foreground) {
        logDebug("Parsing foreground");
        generateCode("// foreground");
        NodeList children = foreground.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node item = children.item(i);
            String nodeName = item.getNodeName();
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                switch (nodeName) {
                    case "fillstroke":
                        logDebug("Parsing fillstroke");
                        generateCanvasMethodCall("fillAndStroke()");
                        break;
                    case "path":
                        parsePath((Element) item);
                }
            }
        }
    }

    private void generateCanvasMethodCall(String methodCall) {
        generateCode("canvas." + methodCall + ";");
    }

    private void generateCode(String code) {
        logDebug("@@Generated code@@ " + code);
        codeLines.add(code);
    }

    private void parsePath(Node path) {
        logDebug("Parsing path");
        generateCanvasMethodCall("begin()");
        NodeList children = path.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node item = children.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                parsePathElement((Element) item);
            }
        }
    }

    private void parsePathElement(Element element) {
        String nodeName = element.getNodeName();
        logDebug("Parsing path element: " + nodeName);
        switch (nodeName) {
            case "close":
                generateCanvasMethodCall("close()");
                break;
            case "curve":
                generateCanvasMethodCall(format("curveTo(%s, %s, %s, %s, %s, %s)",
                        element.getAttribute("x1"), element.getAttribute("y1"),
                        element.getAttribute("x2"), element.getAttribute("y2"),
                        element.getAttribute("x3"), element.getAttribute("y3")));
                break;
            case "arc":
                generateCanvasMethodCall(format("arcTo(%s, %s, %s, %s, %s, %s, %s)",
                        element.getAttribute("rx"), element.getAttribute("ry"), element.getAttribute("x-axis-rotation"),
                        element.getAttribute("large-arc-flag"), element.getAttribute("sweep-flag"), element.getAttribute("x"), element.getAttribute("y"))
                );
                break;
            case "line":
                generateCanvasMethodCall(format("lineTo(%s, %s)", element.getAttribute("x"), element.getAttribute("y")));
                break;
            case "move":
                generateCanvasMethodCall(format("moveTo(%s, %s)", element.getAttribute("x"), element.getAttribute("y")));
                break;

        }
    }

    private void logInfo(String msg) {
        if (isInfoLogActivated) {
            System.out.println(format("[INFO] %s", msg));
        }
    }

    private void logDebug(String msg) {
        if (isDebugLogActivated) {
            System.out.println(format("[DEBUG] %s", msg));
        }
    }

}
