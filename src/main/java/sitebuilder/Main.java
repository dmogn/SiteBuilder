/*
 * Main.java
 *
 * Created on 30 October 2005, 1:08
 * 
 * SiteBuilder is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov
 */

package sitebuilder;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;

/**
 * The console application start point. Arguments parsing, embedded help. 
 * 
 * @author Dmitry Ognyannikov, 2005
 */
public class Main {
    // constructors:
    /** Creates a new instance of Main */
    public Main() {
    }
    
    // members:
    
    // methods:
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            JSAP jsap = new JSAP();

            FlaggedOption optSource = new FlaggedOption("config")
                                    .setStringParser(JSAP.STRING_PARSER)
                                    .setDefault("site.config") 
                                    .setRequired(true) 
                                    .setShortFlag('c') 
                                    .setLongFlag("config");
            optSource.setHelp("Site config file path");
            jsap.registerParameter(optSource);
            
            FlaggedOption optEncoding = new FlaggedOption("encoding")
                                    .setStringParser(JSAP.STRING_PARSER)
                                    .setDefault("UTF8") 
                                    .setRequired(false) 
                                    .setShortFlag('E') 
                                    .setLongFlag("encoding");
            optSource.setHelp("Files encoding");
            jsap.registerParameter(optEncoding);
            
            Switch swHelp = new Switch("help")
                        .setShortFlag('h')
                        .setLongFlag("help");
            swHelp.setHelp("Print help and exit.");
            jsap.registerParameter(swHelp);
            
            JSAPResult config = jsap.parse(args);
            
            if (!config.success() || config.getBoolean("help")) {
                // print help
                System.err.println();
                System.err.println("Usage 1: java -jar SiteBuilder.jar -c path/site.config");
                System.err.println("Usage 2: java " + Main.class.getName());
                System.err.println("                " + jsap.getUsage());
                System.err.println();
                // show full help as well
                System.err.println(jsap.getHelp());
                
                if (config.getBoolean("help")) {
                    System.exit(0);
                } else {
                    System.exit(1); 
                }
            }
            
            // work mode
            String encoding = config.getString("encoding");

            String projectConfigs[] = config.getStringArray("config");

            for (String projectConfig : projectConfigs) {
                sitebuilder.Site site = new sitebuilder.Site();
                System.out.println("start process project: " + projectConfig);
                site.process(projectConfig);
            }
            
        } catch (Exception ex) {
            System.err.println("Arguments parsing exception: " + ex.getLocalizedMessage());
        }
    }    
}
