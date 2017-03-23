/*
 * Create Tables
 */

CREATE TABLE IF NOT EXISTS external_document (
  ext_document_id INT         NOT NULL AUTO_INCREMENT,
  title           VARCHAR(50) NOT NULL,
  html            LONGTEXT    NOT NULL,
  module_id       VARCHAR(50) NOT NULL,
  PRIMARY KEY(ext_document_id)
);

CREATE TABLE IF NOT EXISTS module (
  module_id   VARCHAR(50) NOT NULL,
  access_id   VARCHAR(30) NOT NULL,
  name        VARCHAR(50) NOT NULL,
  author      VARCHAR(50) NOT NULL,
  description TEXT        NULL,
  PRIMARY KEY (module_id)
);

CREATE TABLE IF NOT EXISTS access (
  access_id   VARCHAR(30)   NOT NULL,
  role_id     VARCHAR(20)   NOT NULL,
  passphrase  VARCHAR(255)  NOT NULL,
  hash_type   VARCHAR(10)   NOT NULL,
  enabled     BOOLEAN       NOT NULL,
  PRIMARY KEY(access_id)
);

CREATE TABLE IF NOT EXISTS access_role (
  role_id     VARCHAR(20) NOT NULL,
  PRIMARY KEY (role_id)
);

CREATE TABLE IF NOT EXISTS sentence (
  sentence_id INT   NOT NULL AUTO_INCREMENT,
  number      INT   NOT NULL,
  text        TEXT  NOT NULL,
  document_id INT   NOT NULL,
  PRIMARY KEY(sentence_id)
);

CREATE TABLE IF NOT EXISTS document (
  document_id   INT         NOT NULL AUTO_INCREMENT,
  title         VARCHAR(50) NOT NULL,
  author        VARCHAR(50) NULL,
  source        TINYTEXT    NOT NULL,
  creation_time DATETIME    NULL,
  crawl_time    DATETIME    NOT NULL,
  module_id     VARCHAR(50) NOT NULL,
  PRIMARY KEY(document_id)
);

CREATE TABLE IF NOT EXISTS classification (
  sentence_id INT             NOT NULL,
  module_id   VARCHAR(50)     NOT NULL,
  value       DECIMAL(13, 10) NOT NULL,
  confidence  DECIMAL(13, 10) NULL,
  PRIMARY KEY (sentence_id, module_id)
);

/*
 * Add foreign keys
 */

ALTER TABLE external_document
  ADD CONSTRAINT fk_external_document_module
  FOREIGN KEY (module_id)
  REFERENCES module (module_id)
  ON DELETE RESTRICT;

ALTER TABLE module
  ADD CONSTRAINT fk_module_access
  FOREIGN KEY (access_id)
  REFERENCES access (access_id)
  ON DELETE CASCADE;

ALTER TABLE access
  ADD CONSTRAINT fk_access_access_role
  FOREIGN KEY (role_id)
  REFERENCES access_role (role_id)
  ON DELETE RESTRICT;

ALTER TABLE sentence
  ADD CONSTRAINT fk_sentence_document
  FOREIGN KEY (document_id)
  REFERENCES document (document_id)
  ON DELETE CASCADE;

ALTER TABLE document
  ADD CONSTRAINT fk_document_module
  FOREIGN KEY (module_id)
  REFERENCES module (module_id)
  ON DELETE RESTRICT;

ALTER TABLE classification
  ADD CONSTRAINT fk_classification_sentence
  FOREIGN KEY (sent_id)
  REFERENCES sentence (sentence_id)
  ON DELETE CASCADE;

ALTER TABLE classification
  ADD CONSTRAINT fk_classification_module
  FOREIGN KEY (module_id)
  REFERENCES module (module_id)
  ON DELETE RESTRICT;

/*
 * Add base data
 */

INSERT INTO access_role (role_id) VALUES
  ('crawler'),
  ('classifier'),
  ('external-source'),
  ('admin-user');