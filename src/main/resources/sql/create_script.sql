CREATE TABLE IF NOT EXISTS extern_document (
  id  INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(50),
  html LONGTEXT,
  module_id VARCHAR(50) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS extern_module (
  id VARCHAR(50) NOT NULL,
  name VARCHAR(50) NOT NULL,
  author VARCHAR(50),
  description TEXT,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS authentication_token (
  id INT NOT NULL AUTO_INCREMENT,
  module_id VARCHAR(50) NOT NULL,
  token VARCHAR(30) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS sentence (
  id INT NOT NULL AUTO_INCREMENT,
  number INT NOT NULL,
  text TEXT,
  document_id INT NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS document (
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(50) NOT NULL,
  author VARCHAR(50),
  source TINYTEXT,
  creation_time DATETIME,
  crawl_time DATETIME,
  module_id VARCHAR(50) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS classification (
  sent_id INT,
  document_id INT NOT NULL,
  module_id VARCHAR(50) NOT NULL,
  value DECIMAL(13, 10) NOT NULL,
  confidence DECIMAL(13, 10),
  PRIMARY KEY (sent_id, document_id, module_id)
);

ALTER TABLE extern_document
  ADD CONSTRAINT fk_extern_document_extern_module
  FOREIGN KEY (module_id)
  REFERENCES extern_module (id)
  ON DELETE RESTRICT;

ALTER TABLE token
  ADD CONSTRAINT fk_token_extern_module
  FOREIGN KEY (module_id)
  REFERENCES extern_module (id)
  ON DELETE CASCADE;

ALTER TABLE sentence
  ADD CONSTRAINT fk_sentence_document
  FOREIGN KEY (document_id)
  REFERENCES document (id)
  ON DELETE CASCADE;

ALTER TABLE document
  ADD CONSTRAINT fk_document_extern_module
  FOREIGN KEY (module_id)
  REFERENCES extern_module (id)
  ON DELETE RESTRICT;

ALTER TABLE classification
  ADD CONSTRAINT fk_classification_sentence
  FOREIGN KEY (sent_id)
  REFERENCES sentence (id)
  ON DELETE CASCADE;

ALTER TABLE classification
  ADD CONSTRAINT fk_classification_document
  FOREIGN KEY (document_id)
  REFERENCES document (id)
  ON DELETE CASCADE;

ALTER TABLE classification
  ADD CONSTRAINT fk_classification_extern_document
  FOREIGN KEY (module_id)
  REFERENCES extern_module (id)
  ON DELETE RESTRICT;