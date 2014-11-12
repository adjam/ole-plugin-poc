package edu.ncsu.lib.initializer;

import edu.ncsu.lib.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Loads plugins using the ServiceLoader mechanism and publishes any bean definitions
 * contained therein to the current Spring context.
 * @author adjam
 */
public class PluginLoader implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

    private Logger logger = LoggerFactory.getLogger(PluginLoader.class);

    private boolean pluginsLoaded = false;

    /**
     * holds bean definitions from plugin-specifed Spring contexts *
     */
    private Map<String, BeanDefinition> childBeanDefs = new HashMap<>();

    /**
     * Collects BeanDefinitions from plugin Spring config files.  Need to check whether this
     * can support
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.debug("Searching for plugins");
        ServiceLoader<Plugin> pluginLoader = ServiceLoader.load(Plugin.class);
        for (Plugin plugin : pluginLoader) {
            plugin.setParentContext(applicationContext);
            logger.info("Plugin {} loaded", plugin.getClass().getName());
            Map<String, ? extends BeanDefinition> pluginBeanDefs = plugin.init();
            if (pluginBeanDefs != null) {
                childBeanDefs.putAll(pluginBeanDefs);
            }
        }

        pluginsLoaded = true;
    }

    /**
     * Adds all collected plugin BeanDefinitions to the current BeanDefinitionRegistry,
     * making them members of the current application context.
     **/
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        logger.info("postProcessBeanDefinitionRegistry");
        for ( Map.Entry<String,BeanDefinition> bde : childBeanDefs.entrySet() ) {
            logger.info("registering {}", bde.getKey());
            beanDefinitionRegistry.registerBeanDefinition(bde.getKey(), bde.getValue());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        logger.info("postProcessBeanFactory");

    }
}
