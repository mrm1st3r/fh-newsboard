package de.fhbielefeld.newsboard.model;

import de.fhbielefeld.newsboard.dao.ClassificationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service to calculate average classification for documents.
 */
@Service
public class AverageClassificationService {

    private final ClassificationDao classificationDao;

    @Autowired
    public AverageClassificationService(ClassificationDao classificationDao) {
        this.classificationDao = classificationDao;
    }

    public Map<Document, ClassificationValue> calculateAverageFor(List<Document> documents) {
        Map<Document, ClassificationValue> mapping = new HashMap<>();
        for (Document document : documents) {
            ClassificationValue average = calculateAverageFor(document);
            mapping.put(document, average);
        }
        return mapping;
    }

    public ClassificationValue calculateAverageFor(Document document) {
        List<DocumentClassification> classifications = classificationDao.forForDocument(document);
        double average = classifications
                .stream()
                .mapToDouble(c -> c.getValues()
                        .stream()
                        .mapToDouble(ClassificationValue::effectiveValue)
                        .average().orElse(0))
                .average().orElse(0);
        return ClassificationValue.of(average);
    }
}
