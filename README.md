# BRO Conepenetrationtest Converter, README UNDER CONSTRUCTION!!

[![GitHub forks](https://img.shields.io/github/forks/badges/shields.svg?style=social&label=Fork)](https://github.com/BROprogramma/CPT_GEF_CONVERTER/network/)
[![License](https://img.shields.io/badge/License-EUPL%201.2-yellowgreen.svg)](https://opensource.org/licenses/EUPL-1.1)

* [Contributing] (#contributing)
* [Documentation](#documentation)
* [Requirements](#requirements)
* [Code Explanation](#code-explanation)
 * [Building the source code](#building-the-source-code)
 * [Generation of XML parser (JAXB)](#generation-of-the-xml-parser)
 * [Implementation)](#implementation-of-the-mapper)
 * [Stand alone version](#stand-alone-version)
* [Links](#links)

## Contributing

This project will not be released and neither be pushed (as Maven artefact) to a SonaType (Nexus) repository (unless there's a specific demand for this). 

However, if you do want to contribute, this project follows the _Fork & Pull_ development approach. To get started just fork this repository to your GitHub account and create a new topic branch 
for each change. Please start commits with the GitHub issue number `#<nr>`. Once you are done with your change, submit a pull request against the CPT_GEF_CONVERTER repo. 

## Documentation

Mapping documentation comes in 2 documents on this site: 

* The [Handreiking](https://github.com/BROprogramma/CPT_GEF_CONVERTER/blob/master/gef_doc/20170627%20GEF-CPT%20Report%20naar%20IMBRO-XML%20-%20Handreiking%20v10.docx) (starters guide) . It explains the basic principles and 
should be read prior to read the mapping itself.

* The [Mapping](https://github.com/BROprogramma/CPT_GEF_CONVERTER/blob/master/gef_doc/20170627%20BRO%20CPT%20F3B%20GEF-mapping%20v40.xlsx) This explains in full detail all fields on the GEF site, the rules to which they
are checked and the resulting IMBRO / IMBRO/A xml fields. Please *note* that rules concerning parties and geometry are not implemented since they do require on-line services.

## Requirements

* Java8 is required on the path.
* For building the code, [Maven](https://maven.apache.org/) version 3 or higher is required on the path.


## Code explanation

### Building the source code

The source code can be build by the `maven clean install` command on a linux / unix shell or in a windows command window.

### Generation of the XML parser

The gef-jaxb sub module contains the XSD files locally (good practice). A catalog file is (`src/main/resources/META-INF/catalog.xml`) used to 'connect' the (public) URL of the schema locations to 
the local files. The binding file (`src/main/resources/META-INF/binding.xjb`) determines how the xsd (`src/main/resources/schema`) is mapped to the generated java classes. 2 classes are put into
place manually to cope with observations [OGC observation and measurements](http://www.opengeospatial.org/standards/om). Finally, the [maven-jaxb2-plugin](https://github.com/highsource/maven-jaxb2-plugin)
 takes care of generating the java classes.

### Implementation of the mapper

The implementation consists out of several packages:

* package `nl.bro.cpt.gef.logic` is responsible for searching a file list for cone penetration test gef files. It relates them to dissipation tests. Validation is performed on the consistency (e.g.
all referred dissipation tests are present and are bound (not orphanaged) to a cone penetration test. This package supports `bulk handling` of gef files.

* package `nl.bro.cpt.gef.logic` is responsible for parsing the actual GEF file. It uses an [antlr](http://www.antlr.org/) definition file to generate the parser code. Initially the authors gauged
that it was possible to capture the GEF standard in its entirety in a [`GefLexer.g4`](https://github.com/BROprogramma/CPT_GEF_CONVERTER/blob/master/gef_impl/src/main/antlr4/nl/bro/gef/antlr/GefLexer.g4) 
and [`GefParser.g4`](https://github.com/BROprogramma/CPT_GEF_CONVERTER/blob/master/gef_impl/src/main/antlr4/nl/bro/gef/antlr/GefParser.g4). However, the GEF format proved to be too resilient to do
this. In the end, a minimal version has been put in place and the parser concept is used to populate the GEF data-transfer-objects.

* package `nl.bro.cpt.gef.dto` contains the the GEF model files as so called data-transfer-objects (DTO's). The term DTO is a bit misleading, because it suggests that data is actually transferred. However
their only purpose is an intermediate format. This intermediate format is validated against the business rules of the BRO as far as possible. Feedback is given to the user in GEF terminology. Bean 
validation [JSR303](http://beanvalidation.org/1.0/spec/) is used to check the business rules. As implementation technology [Hibernate Validation 4] (http://hibernate.org/validator/) is used, with some
customizations to support messages based on the "Entity.Attribute" concept. "Entity.Attribute" uniquely identfies an attribute in the BRO model. These "Entity.Attribute" coincide with the 
"Object.property" in the DTO model and form the keys in the `GefValidationMessages.properties`.

* package `nl.bro.dto.gef.validation` contains custom-written validation rules (Java annotations). 

* package `nl.bro.dto.gef.transform` contains the transformation to JAXB objects. It uses [mapstruct](http://mapstruct.org/) to map the GEF DTO's to the generated JAXB DTO's. An implementation is
generated for the `Mapstruct` annotated mappers.


### Stand alone version

The stand-alone version of the GEF reader just builds a small wrapper around the gef_impl classes. It creates a so called 'fat' jar file that contains all the dependencies. Apache's 
[commons-cli](https://commons.apache.org/proper/commons-cli/) is used to implement a self explaining command line interface. 

Just run the stand-alone with: `java -jar convertgef.jar -h` from the commandline.


## Links

* [Source code](https://github.com/BROprogramma/CPT_GEF_CONVERTER/)
* [Issue tracker](https://github.com/BROprogramma/CPT_GEF_CONVERTER/issues)

