package de.fh_bielefeld.newsboard.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for frontend requests
 */
@Controller
public class FrontendController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
