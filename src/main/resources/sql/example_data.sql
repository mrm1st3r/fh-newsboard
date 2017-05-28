-- Clean up
DELETE FROM external_document;
DELETE FROM classification_value;
DELETE FROM classification;
DELETE FROM sentence;
DELETE FROM document;
DELETE FROM module;
DELETE FROM access;

-- Access for modules
INSERT INTO access(access_id, role_id, passphrase, hash_type, enabled) VALUES
  ('test-crawler', 'crawler', 'omglol123', 'plain', true),
  ('test-classifier', 'classifier', 'abc123', 'plain', true);

-- Crawler and classifier
INSERT INTO module(module_id,access_id, title, author, description) VALUES
('test-crawler', 'test-crawler', 'test-crawler', 'Hans Wurst', 'Crawler zum testen'),
('test-classifier', 'test-classifier', 'test-classifier', 'Peter Lustig', 'Classifier zum testen');

-- Documents
INSERT INTO document(document_id, title, author, source_url, creation_time, crawl_time, module_id) VALUES
(1, 'Lorem Ipsum', 'Wuppi Fluppi', 'http://example.com/', '2016-12-06', '2016-12-21', 'test-crawler'),
(2, 'Hier Titel einfügen', 'Max Mustermann', 'http://www.example.com/article/42', '2012-12-21', '2017-01-07', 'test-crawler');

-- Sentences
INSERT INTO sentence(sentence_id, document_seq, content, document_id) VALUES
(1, 1, 'Lorem Ipsum dolor sit amet.', 1),
(2, 2, 'Romani ite domum.', 1),
(3, 1, 'Hallo, Welt.', 2),
(4, 2, 'Wichtige Nachrichten gibt\'s hier nicht.', 2);

-- Classifications
INSERT INTO classification(classification_id, document_id, module_id, created) VALUES
(4, 2, 'test-classifier', '2017-05-23');

-- Classification values
INSERT INTO classification_value(classification_id, order_seq, classification, confidence) VALUES
(4, 1, 1, 1),
(4, 2, -1, 1);
