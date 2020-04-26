package com.mxgraph.xml2js;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class StencilShapeXmlBuilder {

    private List<ShapeBuilder> shapes = new ArrayList<>();

    public ShapeBuilder shape(String name, String w, String h) {
        ShapeBuilder shapeBuilder = new ShapeBuilder(this, name, w, h);
        shapes.add(shapeBuilder);
        return shapeBuilder;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append(toXmlQuote("<shapes name='mxGraph.test.%s'>", System.currentTimeMillis()));
        for (ShapeBuilder shape : shapes) {
            builder.append(toXmlQuote("<shape aspect='variable' strokewidth='inherit' name='%s' w='%s' h='%s'>", shape.name, shape.w, shape.h));
            builder.append("<connections/>");
            builder.append("<foreground>");
            builder.append("<path>");
//            for (String child : shape.children) {
//                builder.append(toXmlQuote(child));
//            }
            shape.children.forEach(child ->  builder.append(toXmlQuote(child)));
            builder.append("</path>");
            builder.append("<fillstroke/>");
            builder.append("</foreground>");
            builder.append("</shape>");
        }
        builder.append("</shapes>");
        return builder.toString();
    }

    private String toXmlQuote(String s) {
        return s.replace("\"", "'");
    }

    private String toXmlQuote(String s, Object... args) {
        return toXmlQuote(format(s, args));
    }

//    private String toXmlQuote(String input) {
//        return input.replaceAll("'", "\"");
//    }


    public static class ShapeBuilder {
        private final StencilShapeXmlBuilder parent;
        private final String name;
        private final String w;
        private final String h;
        private final List<String> children = new ArrayList<>();

        private ShapeBuilder(StencilShapeXmlBuilder parent, String name, String w, String h) {
            this.parent = parent;
            this.name = name;
            this.w = w;
            this.h = h;
        }

        public ShapeBuilder element(String elem) {
            children.add(elem);
            return this;
        }

        public StencilShapeXmlBuilder end() {
            return parent;
        }
    }
}

