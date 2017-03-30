package de.fh_bielefeld.newsboard.frontend;

import de.fh_bielefeld.newsboard.dao.DocumentDao;
import de.fh_bielefeld.newsboard.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for frontend requests
 */
@Controller
public class FrontendController {

    private static final long DOCUMENTS_PER_PAGE = 10;

    private static final String INDEX_TEMPLATE = "index";
    private static final String DETAIL_TEMPLATE = "detail";
    private static final String ABOUT_TEMPLATE = "about";

    private final DocumentDao documentDao;

    @Autowired
    public FrontendController(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    /**
     * Start page displaying the latest 10 documents and their average classification.
     */
    @RequestMapping("/")
    public String index(Model model) {
        List<Document> documentStubs = documentDao.findAllStubs();
        List<Document> documents = documentStubs.stream().limit(DOCUMENTS_PER_PAGE).map(
                document -> documentDao.get(document.getId())).collect(Collectors.toList());
        model.addAttribute("documents", documents);
        return INDEX_TEMPLATE;
    }

    /**
     * Article page showing a single article including more details than the start page.
     */
    @RequestMapping(value = "/document/{id}")
    public String details(Model model, @PathVariable String id) {
        Document doc = documentDao.get(Integer.parseInt(id));
        model.addAttribute("doc", doc);
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
