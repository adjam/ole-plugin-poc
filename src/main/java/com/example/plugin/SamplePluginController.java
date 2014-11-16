package com.example.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sample Controller class defined in plugin's <code>pluginConfig.xml</code> file.
 */
@Controller
public class SamplePluginController {

    private static final Logger logger = LoggerFactory.getLogger(SamplePluginController.class);

    @Autowired
    private DataSource dataSource;


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @RequestMapping(value={ "/plugin" }, method=RequestMethod.GET)
    public HttpEntity<String> getIndexPage() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");
        StringBuilder sb = new StringBuilder();
        for( Map<String,?> row : runQuery() ) {
            sb.append("\nid: ").append(row.get("id") )
                    .append("\nname: ").append(row.get("name") );
        }
        return new HttpEntity<String>("you have been served (by a plugin).\nHere is the data it inserted into " +
                "its very own table:\n" + sb.toString(), headers);
    }

    private List<Map<String,Object>> runQuery() {
        List<Map<String,Object>> results = new ArrayList<>();
        try( Connection conn = dataSource.getConnection() ) {
            try(Statement st = conn.createStatement() ) {
                try( ResultSet rs = st.executeQuery("SELECT * from plugin_table") ) {
                    while( rs.next() ) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("id", String.valueOf(rs.getInt("id")));
                        row.put("name", rs.getString("name"));
                        results.add(row);
                    }

                }
            }

        } catch( SQLException sqx ) {
            logger.warn("error accessing database", sqx);
        }
        return results;

    }
}
