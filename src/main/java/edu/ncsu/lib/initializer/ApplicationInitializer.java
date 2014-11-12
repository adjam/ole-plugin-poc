package edu.ncsu.lib.initializer;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;

/**
 * Created by adamc on 11/11/14.
 */
public class ApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        /* Create the 'root' Spring application context
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();

        appContext.setConfigLocation("/WEB-INF/plugin-test-servlet.xml");
        appContext.refresh();
         ServletRegistration.Dynamic dispatcher =
           container.addServlet("dispatcher", new DispatcherServlet(appContext));
         dispatcher.setLoadOnStartup(1);
         dispatcher.addMapping("/");
        container.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, appContext);
       */}

}
