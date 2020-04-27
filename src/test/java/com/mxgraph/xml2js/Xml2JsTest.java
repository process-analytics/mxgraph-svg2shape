package com.mxgraph.xml2js;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Xml2JsTest {
    private final Xml2Js xml2Js = new Xml2Js().infoLog(false).debugLog(false);

    @Test
    void parse_code_lines() {
        StencilShapeXmlBuilder builder = new StencilShapeXmlBuilder();
        builder.shape("shape-01", "284.03", "327.7")
                .element("<move x='141.9' y='0'/>")
                .element("<curve x1='85.52' x2='39.66' x3='39.66' y1='0' y2='45.87' y3='102.25'/>")
                .element("<line x='39.66' y='131.4'/>")
                .element("<close/>")
                .end()
        ;
        String xml = builder.build();

        assertThat(xml2Js.parseCodeLines(xml)).containsExactly(
                "// foreground",
                "canvas.begin();",
                "canvas.moveTo(141.9, 0);",
                "canvas.curveTo(85.52, 0, 39.66, 45.87, 39.66, 102.25);",
                "canvas.lineTo(39.66, 131.4);",
                "canvas.close();",
                "canvas.fillAndStroke();"
        );
    }

}