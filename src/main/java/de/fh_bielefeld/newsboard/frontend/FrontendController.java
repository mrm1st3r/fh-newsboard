package de.fh_bielefeld.newsboard.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for frontend requests
 */
@Controller
public class FrontendController {

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "Hello world!";
    }
}
