# FH Newsboard
A Java application for storing documents (news articles by original intend)
along with AI-generated sentiment classifications.
It was originally developed at the FH Bielefeld.

## What does it do?
The newsboard itself doesn't collect news articles or other documents nor can it
classify any documents.
Instead it provides a REST-API to allow other applications to perform those actions.
Such applications may be either some kind of crawler that inserts new documents into the newsboard,
or a Classifier that first reads some documents from the Newsboard and then inserts any calculated
classifications back into the newsboard.

On the other hand, end users might browse the stored documents using either the integrated Web-UI
or another Application that is based on the REST-API, such as a mobile application.

## How does it work?
The newsboard is built as a Spring Boot application with an embedded Tomcat-Webserver.
To store the documents and classifications it uses a MySQL-Database, but is designed to support
other storages (other relational as well as NoSQL databases) with relatively little additional effort. 

When new documents are provided via the REST API, they will be split up into separate sentences,
before they are put into the database, using Apache OpenNLP. This is necessary as only single sentences are processed by most kinds of classifiers
and it needs to be unambiguous which classification belongs to which sentence from which document.

## Installation
To build and run the Newsboard you only need a MySQL database, a Java 8 JDK and Maven.
The database has to be configured before building, as a set of
integration tests is executed during the build process. 

The database connection can be configured in a spring-boot configuration file.
Individual connection details can be stored in custom configuration files as `src/main/resources/application-PROFILENAME.properties`.
To use such a configuration, append `-Dspring.profiles.active=PROFILENAME` to the build and launch commands.

Building the newsboard is done easily with maven:

    mvn clean install
    
At the first time this might take a little longer, as it will download all necessary dependencies.
This will also run unit- and integration-Tests.

## Usage
To run the newsboard one you can simply use maven again: 

    mvn spring-boot:run

Alternatively the generated jar file (found in the target directory)
can be started directly e.g.:

    java -jar target/newsboard-0.0.1-SNAPSHOT.jar

If a custom database connection is used, the appropriate spring profile has to be used regardless
how the Newsboard is started.

To open the Newsboards Web-UI simple go to  `http://hostname:8080/`.

## REST API
**The Format** for all programmatic interaction with the newsboard is XML based
and specified in the Schema located at [src/main/resources/document.xsd](src/main/resources/document.xsd).
Additionally, the sample files used to verify the schema can be found at [src/test/resources/](src/test/resources/). 

**The Resources and Methods** provided by the newsboard will be as follows:

- `GET /rest/document/` Read a list of all documents
- `GET /rest/document/{id}/` Read a specific document
- `PUT /rest/document/` Put a new crawled document into the newsboard
- `GET /rest/unclassified/` Read all sentences from all documents that have
not been classified by the authenticated classifier module
- `PUT /rest/classify/` Submit new classifications to the newsboard

A whole request might look like:
`GET http://example.com:8080/rest/document/`

**Authentication** is done via `HTTP-Basic`

## License
Licensed under the MIT License.

You may use and modify the Newsboard for commercial or non-commercial work.
The full license is included in the [license file](LICENSE).
