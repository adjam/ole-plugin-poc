package edu.ncsu.lib.plugin;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Interface to be implemented by all plugins.  This interface is a proof of concept,
 * and could probably bear to be richer and to break operations apart.
 */
public interface Plugin {

    /**
     * Sets the parent Spring context for this plugin.  Any beans available
     * in the parent context will be made available to the plugin's classes.
     * @param parentContext
     */
    public void setParentContext(ApplicationContext parentContext);

    /**
     * Initializes the plugin and gets bean definitions from the plugin's
     * Spring context.
     * @return a map of bean names to BeanDefinitions.
     */
    public Map<String,? extends BeanDefinition> init();

    /**
     * Frees any resources (e.g. database connections, etc.) owned by the plugin.
     */
    public void shutdown();

}
