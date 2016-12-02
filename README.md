Newsboard 2
====
Jetzt mit Flügeln!

REST API
----
**The Format** for all programmatic interaction with the newsboard is XML based
and specified in the Schema located at `src/main/resources/document.xsd`.
Additionally, the sample files used to verify the schema can be found at `src/test/resources/`. 

**The Resources and Methods** provided by the newsboard will be as follows:

- `GET /document/` Read a list of all documents
- `GET /document/{id}/` Read a specific document
- `PUT /document/` Put a new crawled document into the newsboard
- `GET /unclassified/` Read all sentences from all documents that have
not been classified by the authenticated classifier module
- `PUT /classify/` Submit new classifications to the newsboard

**Authentication** is planned to be done via `HTTP-Basic`
