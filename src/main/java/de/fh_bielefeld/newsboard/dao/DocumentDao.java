package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Document;
import de.smartsquare.ddd.annotations.DDDRepository;

import java.util.List;

/**
 * Data access interface for classifiable documents.
 */
@DDDRepository
public interface DocumentDao {

    Document get(int id);

    int update(Document document);

    int create(Document document);

    List<Document> findAllStubs();

    List<Document> findUnclassifiedForModule(String externalModuleId);
}
