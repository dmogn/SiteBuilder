/*
 * PDA_Pablisher.java
 *
 * Created on 8 September 2005, 17:29
 * 
 * SiteBuilder is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov
 */

package sitebuilder;

import java.io.File;

/**
 *
 * @author Dmitry Ognyannikov, 2005
 */
public class PDA_Pablisher {
    
    /** Creates a new instance of PDA_Pablisher */
    public PDA_Pablisher() {
    }
    
    public static void process(File directory)
    {
        File dirFiles[] = directory.listFiles();
        for (int i=0; i<dirFiles.length; i++)
        {
            if (dirFiles[i].isFile())
            {
                processFile(dirFiles[i]);
            }
            else if (dirFiles[i].isDirectory())
            {
                process(dirFiles[i]);
            }
        }
    }
    
    static void processFile(File file)
    {
        String ext = com.oldportal.util.IOToolkit.getFileExtension(file.getAbsolutePath());
                if (ext.equalsIgnoreCase("cpp") || ext.equalsIgnoreCase("c") || ext.equalsIgnoreCase("cxx")
                || ext.equalsIgnoreCase("cc")
                || ext.equalsIgnoreCase("h") || ext.equalsIgnoreCase("hpp") || ext.equalsIgnoreCase("hxx")
                || ext.equalsIgnoreCase("hh")
                || ext.equalsIgnoreCase("java") || ext.equalsIgnoreCase("asm") || ext.equalsIgnoreCase("sh") 
                || ext.equalsIgnoreCase("pas") || ext.equalsIgnoreCase(""))
                {
                    File dest = new File(file.getAbsolutePath()+".txt");
                    file.renameTo(dest);
                    convertStringsDelimiter(dest);
                }
                else if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || 
                        ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png") || 
                        ext.equalsIgnoreCase("htm") || ext.equalsIgnoreCase("html") ||
                        ext.equalsIgnoreCase("pdf") || ext.equalsIgnoreCase("rtf"))
                {
                    ;
                }
                else if (ext.equalsIgnoreCase("txt"))
                {
                    convertStringsDelimiter(file);
                }
                else
                    file.delete();
    }
    
    static void convertStringsDelimiter(File file)
    {
        com.oldportal.util.Strings fileStrings = com.oldportal.util.IOToolkit.loadTextFile(file);
        com.oldportal.util.IOToolkit.saveTextFile(file, fileStrings);
    }
}
