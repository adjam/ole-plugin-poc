package edu.ncsu.lib.controller;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * Created by adamc on 11/12/14.
 */
@Controller
@RequestMapping(value={"/"})
public class DefaultController {



    private Resource pageResource = new ClassPathResource("index.html");

    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    public DefaultController() {
        logger.info("DefaultController checking in");
    }

    @RequestMapping(method=RequestMethod.GET)
    public HttpEntity<String> getIndex() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html");
        try {
            String content = IOUtils.toString(pageResource.getInputStream(), "utf-8");
            return new HttpEntity<String>(content, headers);
        } catch( IOException iox ) {
            throw new RuntimeException("argh", iox);
        }
    }
}
