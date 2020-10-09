package com.mxgraph.svg2js;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.mxgraph.FileTooling.*;
import static com.mxgraph.utils.FileUtils.EOL;
import static org.assertj.core.api.Assertions.assertThat;

class Svg2JsTest {

    private final Svg2Js xml2Js = new Svg2Js().infoLog(false).debugLog(false);

    @Test
    void process_svg_file() throws IOException {
        File workingDirectory = destinationFolder("Svg2Js/work");

        assertThat(xml2Js.convertToJsCode(svgSourceFile("simple-02/path-blue.svg"), workingDirectory))
                .isEqualTo(toCode("// shape: path-blue",
                        "// width: 70",
                        "// height: 50",
                        "// foreground",
                        "canvas.begin();",
                        "canvas.moveTo(0, 25);",
                        "canvas.quadTo(20, 0, 30, 25);",
                        "canvas.quadTo(40, 50, 70, 25);"
                        )
                );
    }

    private String toCode(String... lines) {
        return String.join(EOL, lines);
    }

}
