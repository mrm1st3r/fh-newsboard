package de.fh_bielefeld.newsboard.frontend;

import de.fh_bielefeld.newsboard.dao.DocumentDao;
import de.fh_bielefeld.newsboard.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for frontend requests
 */
@Controller
public class FrontendController {

    private static final long DOCUMENTS_PER_PAGE = 10;
    private DocumentDao documentDao;

    @Autowired
    public FrontendController(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    @RequestMapping("/")
    public String index(Model model) {
        List<Document> documentStubs = documentDao.getAllDocumentsOnlyWithMetaData();
        List<Document> documents = documentStubs.stream().limit(DOCUMENTS_PER_PAGE).map(
                document -> documentDao.getDocumentWithId(document.getId())).collect(Collectors.toList());
        model.addAttribute("documents", documents);
        return "index";
    }
}
