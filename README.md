# mxgraph-svg2shape

A set of tool to convert SVG files into mxGraph resources.

This repository is based on mxGraph svg2xml tooling: `SVG to XML mxGraph stencil definition translation tool. This was
created for internal use, so there are lots of things unfinished.`

It contains the original svg2xml tool; it aims to improve it and to add the following features
- modernize `svg2xml` and contribute improvements to the upstream repository (if it is still active and accept
 contributions): CLI, UI look&feel updates, implement to completed features....
- add `xml2js` tool: translate an XML mxGraph stencil definition into a set of corresponding javascript command for an
easy integration in JS programs
- add `svg2js`: directly convert an SVG file into a set of javascript commands


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
