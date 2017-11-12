package de.fhbielefeld.newsboard.frontend;

import de.fhbielefeld.newsboard.model.document.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * Controller for frontend requests
 */
@Controller
@RequiredArgsConstructor
public class FrontendController {

    private static final int DOCUMENTS_PER_PAGE = 10;

    private static final String INDEX_TEMPLATE = "index";
    private static final String DETAIL_TEMPLATE = "detail";
    private static final String ABOUT_TEMPLATE = "about";

    private final DocumentDao documentDao;
    private final AverageClassificationService classificationService;

    /**
     * Start page displaying the latest 10 documents and their average classification.
     */
    @RequestMapping("/")
    public String index(Model model) {
        List<Document> documents = documentDao.findLatest(DOCUMENTS_PER_PAGE);
        Map<Document, ClassificationValue> classificationMapping = classificationService.calculateAverageFor(documents);
        model.addAttribute("documents", classificationMapping);
        return INDEX_TEMPLATE;
    }

    /**
     * Article page showing a single article including more details than the start page.
     */
    @RequestMapping(value = "/document/{id}")
    public String details(Model model, @PathVariable String id) {
        Document doc = documentDao.get(new DocumentId(Integer.parseInt(id)));
        model.addAttribute("doc", doc);
        model.addAttribute("average", classificationService.calculateAverageFor(doc));
        return DETAIL_TEMPLATE;
    }

    /**
     * About page showing static text about the newsboard including copyright information.
     */
    @RequestMapping("/about")
    public String about() {
        return ABOUT_TEMPLATE;
    }
}
