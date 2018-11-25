/*
 * SourceEncodingConverter.java
 *
 * Created on 30 September 2005, 15:49
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
public class SourceEncodingConverter {

    static String fromEncoding = "cp1251";//;
    static String toEncoding = "UTF-8";
    static String stringEndDelimiter = "\n";

    public static void process(File directory) {
        File dirFiles[] = directory.listFiles();
        for (int i = 0; i < dirFiles.length; i++) {
            if (dirFiles[i].isFile()) {
                processFile(dirFiles[i]);
            } else if (dirFiles[i].isDirectory()) {
                process(dirFiles[i]);
            }
        }
    }

    static void processFile(File file) {
        String ext = com.oldportal.util.IOToolkit.getFileExtension(file.getAbsolutePath());
        if (ext.equalsIgnoreCase("cpp") || ext.equalsIgnoreCase("c") || ext.equalsIgnoreCase("cxx")
                || ext.equalsIgnoreCase("cc")
                || ext.equalsIgnoreCase("h") || ext.equalsIgnoreCase("hpp") || ext.equalsIgnoreCase("hxx")
                || ext.equalsIgnoreCase("hh")
                || ext.equalsIgnoreCase("java") || ext.equalsIgnoreCase("asm") || ext.equalsIgnoreCase("sh")
                || ext.equalsIgnoreCase("pas") || ext.equalsIgnoreCase("")) {
            convertFile(file);
        } else 
                    ;
    }

    static void convertFile(File file) {
        //com.oldportal.util.Strings fileStrings = com.oldportal.util.IOToolkit.loadTextFile(file);
        String fileText = com.oldportal.util.IOToolkit.loadFile(file, fromEncoding);

//        com.oldportal.util.Strings fileStrings = new com.oldportal.util.Strings(fileText);
//        
//        com.oldportal.util.Strings fileStringsOut = new com.oldportal.util.Strings();
//        for (int i=fileStrings.size()-1; i>=1; i--)
//        {
//            if (fileStrings.get(i).length() > 0)
//                fileStringsOut.insert(fileStrings.get(i), 0);
//            else if (fileStrings.get(i-1).length() == 0)
//                fileStringsOut.insert(fileStrings.get(i), 0);
//        }
//        com.oldportal.util.IOToolkit.saveTextFile(file, fileStringsOut);
        com.oldportal.util.IOToolkit.writeFile(file, fileText, toEncoding, stringEndDelimiter);
        //com.oldportal.util.IOToolkit.saveTextFile(file, fileStrings);
    }

}
