/*
 * HTMLFile.java
 *
 * Created on 8 September 2005 ã., 16:51
 *
 * Copyright (C) Dmitry Ognyannikov
 */

package sitebuilder;

import com.oldportal.util.TreeNode;
import com.oldportal.util.StringToolkit;
import java.io.File;

/**
 *
 * @author Dmitry Ognyannikov
 */
public class HTMLFile extends com.oldportal.util.TreeNode {
    // constructors:
    /** Creates a new instance of HTMLFile */
    public HTMLFile(File src, String _linkText, TreeNode parent){
        super(parent);
        srcFile = src;
        linkText = _linkText;
    }
    
    // members:
    String html;
    
    File srcFile;
    File destFile;
    
    String linkText;
    
    String commentBefore = null;
    
    // methods:
    
    public String getTitle()
    {
        return StringToolkit.getTextBetweenKeysIgnoreCase(html, "<title>", "</title>");
    }
    
    public static String setTitle(String html, String title)
    {
        return StringToolkit.replaceTextBetweenKeysIgnoreCase(html, title, "<title>", "</title>");
    }
    
    public String getKeywords()
    {
        String ret = StringToolkit.getTextBetweenKeysIgnoreCase(html, "<meta name=\"Keywords\" content=\"", "\">");
        if (ret.equals(""))
            ret = StringToolkit.getTextBetweenKeysIgnoreCase(html, "<meta  name=\"Keywords\"  content=\"", "\">");
        return ret;
    }
    
    public static String setKeywords(String html, String keywords)
    {
        String ret = StringToolkit.replaceTextBetweenKeysIgnoreCase(html, keywords, "<meta name=\"Keywords\" content=\"", "\">");
        if (ret.equals(""))
            ret = StringToolkit.replaceTextBetweenKeysIgnoreCase(html, keywords, "<meta  name=\"Keywords\"  content=\"", "\">");
        return ret;
    }
    
    public String getDescription()
    {
        String ret = StringToolkit.getTextBetweenKeysIgnoreCase(html, "<meta name=\"Description\" content=\"", "\">");
        if (ret.equals(""))
            ret = StringToolkit.getTextBetweenKeysIgnoreCase(html, "<meta  name=\"Description\"  content=\"", "\">");
        return ret;
    }
    
    public static String setDescription(String html, String description)
    {
        String ret = StringToolkit.replaceTextBetweenKeysIgnoreCase(html, description, "<meta name=\"Description\" content=\"", "\">");
        if (ret.equals(""))
            ret = StringToolkit.replaceTextBetweenKeysIgnoreCase(html, description, "<meta  name=\"Description\"  content=\"", "\">");
        return ret;
    }
    
    public String getAuthor()
    {
        String ret = StringToolkit.getTextBetweenKeysIgnoreCase(html, "<meta name=\"author\" content=\"", "\">");
        if (ret.equals(""))
            ret = StringToolkit.getTextBetweenKeysIgnoreCase(html, "<meta  name=\"author\"  content=\"", "\">");
        if (ret.equals(""))
            ret = StringToolkit.getTextBetweenKeysIgnoreCase(html, "<meta  name=\"author\"  content=\"", "\">");
        return ret;
    }
    
    public static String setAuthor(String html, String author)
    {
        String ret = StringToolkit.replaceTextBetweenKeysIgnoreCase(html, author, "<meta name=\"author\" content=\"", "\">");
        if (ret.equals(""))
            ret = StringToolkit.replaceTextBetweenKeysIgnoreCase(html, author, "<meta  name=\"author\"  content=\"", "\">");
        return ret;
    }
    
    
    
    
   

}
