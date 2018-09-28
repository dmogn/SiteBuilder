/*
 * ConfigLineParser.java
 *
 * Created on 8 September 2005, 23:16
 * 
 * SiteBuilder is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov
 */

package sitebuilder;

/**
 *
 * @author Dmitry Ognyannikov, 2005
 */
class ConfigLineParser {
    // constructors:
    /** Creates a new instance of ConfigLineParser */
    public ConfigLineParser(String _text) {
        text = _text;
    }
    
    // members:
    int lastIndex = 0;
    String text;
    
    // methods:
    
    boolean isComment()
    {
        for (int i=0; i<text.length(); i++)
        {
            char c = text.charAt(i);
            
            if (Character.isSpaceChar(c))
                continue;
            
            if (text.charAt(i) == '#')
                return true;
            else
                return false; 
        }
        return false;
    }
    
    String getComment()
    {
        for (int i=1; i<text.length(); i++)
        {
            char c = text.charAt(i);
            if (c == '#')
                continue;
            if (!Character.isSpaceChar(c))
                return text.substring(i);
        }
        return "";
    }
    
    String parseToken()
    {
        if (lastIndex >= text.length() - 1);
        boolean tokenInQuotes = false;
                
        int beginIndex = -1;
        for (int i=lastIndex; i<text.length(); i++)
        {
            char c = text.charAt(i);
            if (c == '\"')
            {
                beginIndex = i+1;
                tokenInQuotes = true;
                break;
            }
            if (!Character.isSpaceChar(c))
            {
                beginIndex = i;
                break;
            }
        }
        
        if (beginIndex == -1)
            return null;
        
        int endIndex = -1;
        for (int i = beginIndex; i<text.length(); i++)
        {
            char c = text.charAt(i);
            if (tokenInQuotes)
            {
                if (c == '\"')
                {
                    endIndex = i;
                    break;
                }
                else
                    continue;
            }
            else
            {
                if (Character.isSpaceChar(c))
                {
                    endIndex = i;
                    break;
                }
            }
        }
        
        if (endIndex == -1)
            return null;
        
        if (beginIndex >= endIndex)
            return null;
        
        lastIndex = endIndex;
        
        return text.substring(beginIndex, endIndex);        
    }
}
