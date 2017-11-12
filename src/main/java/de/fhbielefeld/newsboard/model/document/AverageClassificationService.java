package de.fhbielefeld.newsboard.model.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service to calculate average classification for documents.
 */
@RequiredArgsConstructor
@Service
public class AverageClassificationService implements de.fhbielefeld.newsboard.model.Service {

    private final ClassificationDao classificationDao;

    public Map<Document, ClassificationValue> calculateAverageFor(List<Document> documents) {
        Map<Document, ClassificationValue> mapping = new HashMap<>();
        for (Document document : documents) {
            ClassificationValue average = calculateAverageFor(document);
            mapping.put(document, average);
        }
        return mapping;
    }

    public ClassificationValue calculateAverageFor(Document document) {
        List<DocumentClassification> classifications = classificationDao.findForDocument(document);
        double average = classifications
                .stream()
                .mapToDouble(c -> c.getValues()
                        .map(ClassificationValue::effectiveValue)
                        .average()
                        .getOrElse(0.0))
                .average().orElse(0);
        return ClassificationValue.of(average);
    }
}
