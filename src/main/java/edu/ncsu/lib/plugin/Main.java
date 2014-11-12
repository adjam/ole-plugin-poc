package edu.ncsu.lib.plugin;

import java.util.ServiceLoader;

/**
 * Created by adamc on 11/11/14.
 */
public class Main {

    public static void main(String [] args) {
        ServiceLoader<Plugin> pluginLoader = ServiceLoader.load(Plugin.class);
        for( Plugin plugin : pluginLoader ) {
            plugin.init();
        }
        System.err.println("Done.");
    }
}
