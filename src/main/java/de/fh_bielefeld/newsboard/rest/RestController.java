package de.fh_bielefeld.newsboard.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for REST-Api.
 *
 * @author Lukas Taake
 */
@Controller
@RequestMapping("/rest")
public class RestController {

    @RequestMapping(path = "/document", method = RequestMethod.GET)
    @ResponseBody
    public String listDocuments() {
        return "Hello world!";
    }

    @RequestMapping(path = "/document", method = RequestMethod.PUT)
    @ResponseBody
    public String putDocument() {
        return "Hello world!";
    }

    @RequestMapping(path = "/document/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getDocument() {
        return "Hello world!";
    }

    @RequestMapping(path = "/unclassified", method = RequestMethod.GET)
    @ResponseBody
    public String getUnclassified() {
        return "Hello world!";
    }

    @RequestMapping(path = "/classify", method = RequestMethod.PUT)
    @ResponseBody
    public String classify() {
        return "Hello world!";
    }
}
