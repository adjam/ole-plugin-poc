package com.example.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Sample Controller class defined in plugin.
 */
@Controller
public class SamplePluginController {

    private static final Logger logger = LoggerFactory.getLogger(SamplePluginController.class);

    @RequestMapping(value={ "/plugin" }, method=RequestMethod.GET)
    public HttpEntity<String> getIndexPage() {
        logger.info("HEY LOOK THE PLUGIN CONTROLLER HAS BEEN INVOKED");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");
        return new HttpEntity<String>("you have been served (by a plugin)", headers);
    }
}
