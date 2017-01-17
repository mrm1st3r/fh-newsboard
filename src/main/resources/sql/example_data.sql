-- Clean up
DELETE FROM classification;
DELETE FROM sentence;
DELETE FROM document;
DELETE FROM authentication_token;
DELETE FROM extern_module;

-- Crawler and classifier
INSERT INTO extern_module(id, name, author, description) VALUES
  ("test-crawler", "test-crawler", "Hans Wurst", "Crawler zum testen"),
  ("test-classifier", "test-classifier", "Peter Lustig", "Classifier zum testen");

-- Documents
INSERT INTO document(id, title, author, source, creation_time, crawl_time, module_id) VALUES
  (1, "Lorem Ipsum", "Wuppi Fluppi", "http://example.com/", "2016-12-06", "2016-12-21", "test-crawler"),
  (2, "Hier Titel einfügen", "Max Mustermann", "http://www.example.com/article/42", "2012-12-21", "2017-01-07", "test-crawler");

-- Sentences
INSERT INTO sentence(id, number, text, document_id) VALUES
  (1, 1, "Lorem Ipsum dolor sit amet.", 1),
  (2, 2, "Romani ite domum.", 1),
  (3, 1, "Hallo, Welt.", 2),
  (4, 2, "Wichtige Nachrichten gibt's hier nicht.", 2);

-- Classifications
INSERT INTO classification(sent_id, module_id, value, confidence) VALUES
  (4, "test-classifier", 0.8, NULL);

-- Authentication tokens
INSERT INTO authentication_token(module_id, token) VALUES
  ("test-crawler", "omglol123"),
  ("test-classifier", "abc123");
