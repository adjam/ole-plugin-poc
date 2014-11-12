package com.example.plugin;

import edu.ncsu.lib.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Sample plugin showing how to load a Spring XML file and
 */
public class SamplePlugin implements Plugin {

    private ApplicationContext parentContext;

    private Logger logger = LoggerFactory.getLogger(SamplePlugin.class);
    @Override
    public void setParentContext(ApplicationContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public Map<String,? extends BeanDefinition> init() {
        logger.trace("For lo, I am initialized.");
       GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.setParent(parentContext);
        Resource r = new UrlResource(getClass().getResource("pluginConfig.xml"));
        logger.info("Searching for plugin configuration at " + r.toString());
        if ( r.exists() ) {
            logger.info("Reading plugin bean definitions from {}", r.toString());
            XmlBeanDefinitionReader bReader = new XmlBeanDefinitionReader(ctx);
            bReader.loadBeanDefinitions(r);
        } else {
            logger.error("Bean definition file for plugin not found");
        }

        initializeDataSource(ctx);

        Map<String,BeanDefinition> defs = new HashMap();
                for ( String bdn : ctx.getBeanDefinitionNames() ) {
                    defs.put(bdn, ctx.getBeanDefinition(bdn));
                }


        return defs;
    }

    /**
     * Sample initialization code; shows how to pull a bean from a parent context,
     * and makes some assertions to ensure it's working as designed.
     * @param ctx
     */
    private void initializeDataSource(GenericXmlApplicationContext ctx) {
        DataSource ds = (DataSource)ctx.getBean("dataSource");
        try( Connection conn = ds.getConnection() ) {
            try (Statement st = conn.createStatement()) {
                st.execute("SELECT * FROM plugin_table");
                throw new IllegalStateException("Should not have been able to select from a nonexistent table");
            } catch (SQLException sqx) {
                // normal and expected
            }
            try (Statement cts = conn.createStatement()) {
                cts.execute("CREATE TABLE plugin_table( id INT PRIMARY KEY, name VARCHAR(32) )");
                cts.execute("INSERT INTO plugin_table(id,name) VALUES(1,'broos wain')");
            }
            try (Statement count = conn.createStatement()) {
                try (ResultSet rs = count.executeQuery("SELECT count(1) from plugin_table")) {
                    Assert.isTrue(rs.next());
                    Assert.isTrue(new Integer(1).equals(rs.getInt(1)));
                }
            }
        } catch( SQLException sqx ) {
            throw new RuntimeException("Plugin initialization SQL failed", sqx);

        }
    }

    public void shutdown() {
        // nop
    }
}
