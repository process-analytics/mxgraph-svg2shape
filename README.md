# mxgraph-svg2shape
[![Build](https://github.com/process-analytics//mxgraph-svg2shape/workflows/Build/badge.svg)](https://github.com/process-analytics/mxgraph-svg2shape/actions)
[![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/process-analytics/mxgraph-svg2shape?color=orange&include_prereleases)](https://github.com/process-analytics/mxgraph-svg2shape/releases)



A set of tool to convert SVG files into mxGraph resources.

This repository is based on [mxGraph svg2xml tooling](https://github.com/jgraph/svg2xml):
```
SVG to XML mxGraph stencil definition translation tool.
This was created for internal use, so there are lots of things unfinished.
```

It contains the original svg2xml tool; it aims to improve it and to add the following features
- modernize [svg2xml](#svg2xml) and contribute improvements to the upstream repository (if it is [still active and accept
 contributions](https://github.com/jgraph/svg2xml/pull/13#issuecomment-619573225)): CLI, UI look&feel updates, implement to complete features....
- add [xml2js](#xml2js): translate an XML mxGraph stencil definition into a set of corresponding JavaScript/TypeScript commands for an
easy integration in JS programs
- add [svg2js](#svg2js): convert an SVG file into a set of `mxGraph` JavaScript/TypeScript commands



## Build

> Requirements: JDK 8+
> The build relies on Maven; the project uses the Maven Wrapper, no need to install Maven as the wrapper manages this
> for you.

```
./mvnw package
```

Note about branches in this repository
- `master` is not intended to receive any changes except updates from the upstream repository
- the `develop` branch is the target for new features, fixes, .... and merge commits from `master`  


## `svg2xml`

### Run

You can run `svg2xml` with or without GUI.
- with the GUI (more details below)
```
java -jar target/mxgraph-svg2shape-*-jar-with-dependencies.jar
```
- without the GUI
  - `<path_to_source>` path to the svg file to convert or path to a folder to walk through and find svg files (see the
  GUI explanation below)
  - `<path_to_destination>` 
```
java -jar target/mxgraph-svg2shape-*-jar-with-dependencies.jar <path_to_source> <path_to_destination>
```

### GUI Quick start guide

**Note**: the following is taken from the [upstream repository](https://github.com/jgraph/svg2xml)

The left file system defines what files or folders you want to convert. The right one, defines the destination.

If you select one file, a single stencil XML file will be generated for just that one stencil. 

If you select multiple files, a single stencil XML file will be generated for the selected stencils. So those stencils will be one library.

If you select a folder, all the files in the folder and all subfolders will be processed. Every folder will get one library created. So at the destination, all folders from the source path will be recreated and libraries will be named after folder names.

### Options

NOTE: most of the options aren't implemented yet (as noted in the UI). Also some of the options are not thoroughly tested.

#### Calculate border

If checked, stencil borders will be calculated based on content. If unchecked, the borders or viewpoint defined in the SVG will be used.

#### Use relative scaling

If other than 1.00, the resulting stencils will be bigger or smaller compared to the source.

#### Round coordinates

If you want to reduce the size, with some compromise to precision, use rounding. For stencils bigger than 100x100 rounding to 2 decimal points is usually a decent choice.


## `xml2js`

**DISCLAIMER**: this tool is at its early stage and misses a lot of features. See the [GitHub issues](https://github.com/process-analytics/mxgraph-svg2shape/issues)
(create one for any questions and prior submitting a Pull Request for discussions). It is mainly developed to provide the foundation for `svg2js`
and won't probably never support the whole mxGraph stencil format.


**GOAL**: 
- translate an XML mxGraph stencil definition into a set of corresponding javascript calls for an easy integration in JS programs
- the stencil definition and the JavaScript functions are very close; the names are almost the same but the arguments order is not the same
(have a look at `curveTo` for instance). So the tool can save you a lot of time and avoid mistakes (you would get strange painting) compared to manual
transformations
  - xml curve: `<curve x1='85.52' x2='39.66'..`
  - mxAbstractCanvas2D curveTo: `canvas.curveTo(x1, y1, x2,...`


### Run

`xml2js` required the following arguments
- `<path_to_source>` path to the xml file containing the mxGraph stencil shape to convert to javascript code
```
java -cp target/mxgraph-svg2shape-*-jar-with-dependencies.jar com.mxgraph.xml2js.Xml2Js <path_to_source>
```

### Generated code

The tool writes the generated code in the console
``` javascript
// foreground
canvas.begin();
canvas.moveTo(19.87, 0);
canvas.curveTo(11.98, 0, 5.55, 6.42, 5.55, 14.32);
canvas.lineTo(5.55, 18.4);

canvas.close();
canvas.fillAndStroke();
```

## `svg2js`

**GOAL**:
- convert an SVG file into a set of `mxGraph` javascript commands, by calling `svg2xml` then `xml2js` 
- this is the fastest and safest way to integrate SVG into `mxGraph` shapes. That's the way used by [bpmn-visualization](https://github.com/process-analytics/bpmn-visualization-js)
to manage the [BPMN](http://www.bpmn.org/) icons


### Run

`svg2js` required the following arguments
- `<path_to_source>` path to the SVG file to convert to javascript code
```
java -cp target/mxgraph-svg2shape-*-jar-with-dependencies.jar com.mxgraph.svg2js.Svg2Js <path_to_source>
```

