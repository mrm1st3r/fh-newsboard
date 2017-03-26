-- Clean up
DELETE FROM classification;
DELETE FROM sentence;
DELETE FROM document;
DELETE FROM access;
DELETE FROM module;

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
INSERT INTO classification(sentence_id, module_id, result, confidence) VALUES
(4, 'test-classifier', 0.8, NULL);
