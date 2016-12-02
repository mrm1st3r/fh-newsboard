package de.fh_bielefeld.newsboard.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for REST-Api.
 *
 * @author Lukas Taake
 */
@RestController
@RequestMapping("/rest")
public class RestApiController {

    @RequestMapping(path = "/document", method = RequestMethod.GET)
    public String listDocuments() {
        return "Hello world!";
    }

    @RequestMapping(path = "/document", method = RequestMethod.PUT)
    public String putDocument() {
        return "Hello world!";
    }

    @RequestMapping(path = "/document/{id}", method = RequestMethod.GET)
    public String getDocument() {
        return "Hello world!";
    }

    @RequestMapping(path = "/unclassified", method = RequestMethod.GET)
    public String getUnclassified() {
        return "Hello world!";
    }

    @RequestMapping(path = "/classify", method = RequestMethod.PUT)
    public String classify() {
        return "Hello world!";
    }
}
