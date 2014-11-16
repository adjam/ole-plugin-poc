package com.example.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample plugin.  This one uses @PostConstruct and @PreDestroy annotations to invoke any startup and
 * shutdown tasks, respectively.
 */
@Component("samplePlugin")
@Order(1)
public class SamplePlugin {

    private ApplicationContext parentContext;

    @Autowired
    private DataSource dataSource;

    private Logger logger = LoggerFactory.getLogger(SamplePlugin.class);

    public void setParentContext(ApplicationContext parentContext) {
        this.parentContext = parentContext;
    }


    @PostConstruct
    public void initialize() {
        logger.info("Initialize; dataSource {}", dataSource);
        if ( dataSource != null ) {
            initializeDataSource();
        }
    }

    /**
     * Sample initialization code; shows how to pull a bean from a parent context,
     * and makes some assertions to ensure it's working as designed.
     */
    private void initializeDataSource() {
        logger.info("Initializing data source with new table");
        try( Connection conn = dataSource.getConnection() ) {
            try (Statement st = conn.createStatement()) {
                logger.info("Checking for table (should not exist)");
                st.execute("SELECT * FROM plugin_table");
                throw new IllegalStateException("Should not have been able to select from a nonexistent table");
            } catch (SQLException sqx) {
                logger.info("Table does not exist, creating");
                // normal and expected
            }
            try (Statement cts = conn.createStatement()) {
                logger.info("create table");
                cts.execute("CREATE TABLE plugin_table( id INT PRIMARY KEY, name VARCHAR(32) )");
                logger.info("inserting sample row");
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

    @PreDestroy
    public void shutdown() {
        logger.info("Shutting down");
        // nop
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
