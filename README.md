Newsboard 2
====
Jetzt mit Flügeln!

Building & Running
---
**Building** the newsboard can be done easily with maven:
`mvn clean install`. At the first time this might take a little longer,
as it will download all necessary dependencies.

**Running** can be done either via maven with `mvn spring-boot:run`.
Alternatively the generate jar file (found in the target directory)
can be started directly e.g.: `java -jar target/newsboard-0.0.1-SNAPSHOT.jar`


REST API
----
**The Format** for all programmatic interaction with the newsboard is XML based
and specified in the Schema located at `src/main/resources/document.xsd`.
Additionally, the sample files used to verify the schema can be found at `src/test/resources/`. 

**The Resources and Methods** provided by the newsboard will be as follows:

- `GET /rest/document/` Read a list of all documents
- `GET /rest/document/{id}/` Read a specific document
- `PUT /rest/document/` Put a new crawled document into the newsboard
- `GET /rest/unclassified/` Read all sentences from all documents that have
not been classified by the authenticated classifier module
- `PUT /rest/classify/` Submit new classifications to the newsboard

A whole request might look like:
`GET http://example.com:8080/rest/document/`

**Authentication** is planned to be done via `HTTP-Basic`
