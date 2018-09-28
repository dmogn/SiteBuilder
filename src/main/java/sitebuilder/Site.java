/*
 * Site.java
 *
 * Created on 8 September 2005, 16:53
 * 
 * SiteBuilder is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov
 */

package sitebuilder;

import com.oldportal.util.*;
import java.io.File;

/**
 *
 * @author Dmitry Ognyannikov, 2005
 */
public class Site {
    // constructors:
    /** Creates a new instance of Site */
    public Site() {
    }
    
    // members:
    File inDirectrory = null;
    File outDirectrory = null;
    java.io.File rootConfigFile = null;
    
    String defaultBodyBeginKey = "[body]";
    String defaultBodyEndKey = "[/body]";
    
    String defaultFrameBodyReplacyKey = "[#frame_body]";
    String defaultLastModifiedDateReplacyKey = "[#last_modified]";
    
    String defaultLinksReplacyKey = "[#links]";
    String defaultPathLinksReplacyKey = "[#path_links]";
    
    String defaultRootFileFrame = "";
    String defaultFileFrame = "";
    
    String linkBackgrounds = "rgb(204, 204, 255)";
    
    java.io.File logoImageFile = null;
    String defaultLogoImageReplacyKey = "[#logo_image]";
    
    java.nio.charset.Charset encoding = java.nio.charset.Charset.defaultCharset();
    
    
    HTMLFile siteTree = null;
    
    // methods:
    
    public void process(String siteConfigurationFileName)
    {
        File configFile = new File(siteConfigurationFileName);
        if (!configFile.exists())
        {
            System.err.println("fatal errot: cannot read site configuration file");
            return;
        }
        
        parseSiteConfigurationFile(configFile);
    }
    
    void parseSiteConfigurationFile(File siteConfigurationFile)
    {
        String srcDirKey = "SOURCE_DIR";
        String destDirKey = "DESTINATION_DIR";
        String indexFileTemplateKey = "INDEX_FILE_TEMPLATE";
        String otherFilesTemplateKey = "OTHER_FILES_TEMPLATE";
        
        String srcDir = "";
        String destDir = "";
        String indexFileTemplate = "";
        String otherFilesTemplate = "";
        
        String configText = IOToolkit.loadFile(siteConfigurationFile, encoding.displayName());
        Strings configStrings = new Strings(configText);

        for (int i=0; i<configStrings.size(); i++)
        {
            ConfigLineParser line = new ConfigLineParser(configStrings.get(i));
         
            
            if (line.isComment())
            {
                continue;
            }                
            
            String key = line.parseToken();
            String keyValue = line.parseToken();
            
            if (key.equals(srcDirKey))
            {
                srcDir = keyValue;
            }
            else if (key.equals(destDirKey))
            {
                destDir = keyValue;
            }
            else if (key.equals(indexFileTemplateKey))
            {
                indexFileTemplate = keyValue;
            }
            else if (key.equals(otherFilesTemplateKey))
            {
                otherFilesTemplate = keyValue;
            }
        }
        if (srcDir.equals("") || destDir.equals("") || indexFileTemplate.equals("") || otherFilesTemplate.equals(""))
        {
            System.err.println("fatal errot: not all fields present in site configuration file");
            return;
        }
        
        loadTemplates(otherFilesTemplate, indexFileTemplate);
        process(srcDir, destDir);
    }
    
    public void process(String inputDirectory, String outputDirectory)
    {
        inDirectrory = new File(inputDirectory);
        if (!inDirectrory.exists())
        {
            System.err.println("fatal errot: cannot read root directory");
            return;
        }
        
        siteTree = parseRootIndexFile(inputDirectory);
        
        parseDirectory(siteTree, inDirectrory);
        
        outDirectrory = new File(outputDirectory);
        
        if (!outDirectrory.exists())
            outDirectrory.mkdirs();
        
        updateDestinationFiles(siteTree);
        
        generateSiteFile(siteTree);
    }
    
    public void loadTemplates(String frameTemplateFilename, String rootFrameTemplateFilename)
    {
        defaultFileFrame = IOToolkit.loadFile(frameTemplateFilename, encoding.displayName());
        defaultRootFileFrame = IOToolkit.loadFile(rootFrameTemplateFilename, encoding.displayName());
    }
    
    HTMLFile parseRootIndexFile(String inputDirectory)
    {
        File directory = new File(inputDirectory);
        String config = IOToolkit.loadFile(directory.getAbsolutePath()+File.separator+"index.config", encoding.displayName());
        if (config == null)
        {
            System.err.println("fatal errot: cannot read root index.config file.");
            return null;
        }
        
        com.oldportal.util.Strings configStrings = new com.oldportal.util.Strings(config);
        String commentBeforeNextFile = null;
        for (int i=0; i<configStrings.size(); i++)
        {
            ConfigLineParser line = new ConfigLineParser(configStrings.get(i));
            
            if (line.isComment())
            {
                commentBeforeNextFile = line.getComment();
                continue;
            }                
            
            String filename = line.parseToken();
            String linkText = line.parseToken();
            
            if (filename.equals("index.html") || filename.equals("index.htm"))
            {
                File file = new File(inputDirectory + File.separator + filename);
                return parseFile(null,  file, linkText, commentBeforeNextFile);
            }
            
            commentBeforeNextFile = null;
        }
        return null;
    }
    
    void parseDirectory(TreeNode parent, File directory)
    {
        String config = IOToolkit.loadFile(directory.getAbsolutePath()+File.separator+"index.config", encoding.displayName());
        
        if (config == null)
        {
            System.err.println("fatal errot: cannot read root index.config file.");
            return;
        }
        
        com.oldportal.util.Strings configStrings = new com.oldportal.util.Strings(config);
        String commentBeforeNextFile = null;
        for (int i=0; i<configStrings.size(); i++)
        {
            ConfigLineParser line = new ConfigLineParser(configStrings.get(i));
            
            if (line.isComment())
            {
                commentBeforeNextFile = line.getComment();
                continue;
            } 
            
            String filename = line.parseToken();
            
            if (filename == null || filename.equals("index.html") || filename.equals("index.htm"))
            {
                commentBeforeNextFile = null;
                continue;// index file must be described in parent directory configuration file.
            }
            
            String linkText = line.parseToken();
            
            String option = line.parseToken();
            
            if (filename == null || linkText == null)
                continue;
            
            File file = new File(directory.getAbsolutePath()+File.separator+filename);
            if (file.isFile())
            {
                parseFile(parent,  file, linkText, commentBeforeNextFile);
            }
            else if (file.isDirectory())
            {
                File indexFile = new File(file.getAbsolutePath()+File.separator+"index.html");
                if (!indexFile.exists())
                    indexFile = new File(file.getAbsolutePath()+File.separator+"index.htm");
                if (!indexFile.exists())
                    continue;
                
                HTMLFile indexNode = parseFile(parent,  indexFile, linkText, commentBeforeNextFile);
                parseDirectory(indexNode, file);
            }
            commentBeforeNextFile = null;
        }
    }

    HTMLFile parseFile(TreeNode parent, File file, String linkText, String commentBefore)
    {
        HTMLFile html = new HTMLFile(file, linkText, parent);
        if (parent != null)
            parent.add(html);
        
        html.html = IOToolkit.loadFile(file.getAbsolutePath(), encoding.displayName());
        
        html.commentBefore = commentBefore;
        
        return html;
    }

    void updateDestinationFiles(HTMLFile node)
    {
        String relativePath = com.oldportal.util.IOToolkit.getRelativePath(inDirectrory, node.srcFile, File.separator);
        node.destFile = new File(outDirectrory + File.separator + relativePath);
        for (int i=0; i<node.getChildsCount(); i++)
        {
            updateDestinationFiles((HTMLFile)node.getChildByIndex(i));
        }
    }
    
    void generateSiteFile(HTMLFile file)
    {
        try{
            if ((file.html != null)||(!file.html.equals("")))
            {
                String body = null;

                if (file.getParentNode() == null)
                    body = defaultRootFileFrame;
                else
                    body = defaultFileFrame;

                String title = file.getTitle();
                String keywords = file.getKeywords();
                String description = file.getDescription();

                body = StringToolkit.replaceFirst(body, defaultFrameBodyReplacyKey, getBody(file.html));
                
                body = file.setDescription(body, description);
                body = file.setKeywords(body, keywords);
                body = file.setTitle(body, title);

                long lastModification = file.srcFile.lastModified();
                java.util.Date lastModificationDate = new java.util.Date();
                lastModificationDate.setTime(lastModification);
                body = StringToolkit.replaceFirst(body, defaultLastModifiedDateReplacyKey, lastModificationDate.toString());

                String links = generateSiteFileLinks(file, true);
                body = StringToolkit.replaceFirst(body, defaultLinksReplacyKey, links);
                String pathLinks = generateSiteFilePathLinks(file);
                body = StringToolkit.replaceFirst(body, defaultPathLinksReplacyKey, pathLinks);

                if (logoImageFile != null)
                    body = StringToolkit.replaceFirst(body, defaultLogoImageReplacyKey, com.oldportal.util.IOToolkit.getRelativePath(file.destFile, logoImageFile, "/"));
                else
                    body = StringToolkit.replaceFirst(body, defaultLogoImageReplacyKey, "&nbsp;");
                
                String fileName = file.destFile.getAbsolutePath();

                IOToolkit.writeFile(file.destFile.getAbsolutePath(), body, encoding.displayName(), "\n");
            }
            else
            {
                file.destFile.mkdirs();
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
            System.err.println("Can't generate file: " + file.destFile.getAbsolutePath());
        }
        for (int i=0; i<file.getChildsCount(); i++)
        {
            generateSiteFile((HTMLFile)file.getChildByIndex(i));
        }
    }
    
    String getBody(String html)
    {
        String ret = StringToolkit.getTextBetweenKeysIgnoreCase(html, defaultBodyBeginKey, defaultBodyEndKey);
        
        if (ret.equals(""))
            ret = StringToolkit.getTextBetweenKeysIgnoreCase(html, "<body>", "</body>");
            
        return ret;
    }
    
    String generateSiteFilePathLinks(HTMLFile file)
    {
        String pathLinks = "";
        
        java.util.Vector<HTMLFile> path = new java.util.Vector<HTMLFile>();
        HTMLFile parent = (HTMLFile)file.getParentNode();
        while (parent != null)
        {
            path.add(parent);
            parent = (HTMLFile)parent.getParentNode();
        }
        
        for (int i=path.size()-1; i>=0; i--)
        {
            HTMLFile levelFile = path.get(i);
            pathLinks += createLink(file, levelFile) + "&nbsp;>&nbsp;";
        }
        
        pathLinks += createLink(file, file);
        
        return pathLinks;
    }
    
    String generateSiteFileLinks(HTMLFile file, boolean addEmptyStrings)
    {
        int linksCount = 0;
        String levelReplaceKey = "[#current_level]";
        String links = levelReplaceKey;
        if (file.getParentNode() == null)
        {// list only top files:
            String levelLinks = createLink(file, file, 0);
            
            for (int i=0; i<file.getChildsCount(); i++)
            {
                levelLinks += createLink(file, (HTMLFile)file.getChildByIndex(i), 0);
                linksCount++;
            }
            
            if (addEmptyStrings)
            for (int i=linksCount; i<30; i++)
            {
                levelLinks += "<br>";
            }
            
            return levelLinks;
        }
        
        java.util.Vector<HTMLFile> path = new java.util.Vector<HTMLFile>();
        //HTMLFile parent = (HTMLFile)file.getParentNode();
        HTMLFile parent = file;
        while (parent != null)
        {
            path.add(parent);
            parent = (HTMLFile)parent.getParentNode();
        }
        
        int tabulationLevel = 0;
        
        String indexLink = createLink(file, path.get(path.size()-1), 0);
        links = indexLink + links;
        linksCount++;
        
        // create links tree from top to down
        for (int i=path.size()-1; i>=0; i--)
        {
            String levelLinks = "";
            HTMLFile levelFile = path.get(i);
            
            // add files links:
            for (int g=0; g<levelFile.getChildsCount(); g++)
            {
                HTMLFile fileForLink = (HTMLFile)levelFile.getChildByIndex(g);
                
                String link = createLink(file, fileForLink, tabulationLevel);
                
                levelLinks += link;
                //levelLinks += "\n<br>";
                linksCount++;
                
                if (i > 0)
                    if (fileForLink == path.get(i-1))
                        levelLinks += levelReplaceKey;
            }
            
            links = StringToolkit.replaceFirst(links, levelReplaceKey, levelLinks);

            tabulationLevel++;
        }
        
        // delete levelReplaceKey from text:
        //links = StringToolkit.replaceFirst(links, levelReplaceKey, "");
        if (addEmptyStrings)
        for (int i=linksCount; i<30; i++)
        {
            links += "<br>";
        }
        
        return links;
    }
    
    static String coverStringToLine(String string, String lineColor)
    {
        String template = "<table style=\"width: 100%; text-align: right;\" border=\"0\"><tbody><tr><td style=\"vertical-align: center; background-color:  [color];\">[body]</td></tr></tbody></table>";
        template = StringToolkit.replaceFirst(template, "[color]", lineColor);
        return StringToolkit.replaceFirst(template, "[body]", string);
    }
    
    static String coverStringToLineWithWidth(String string, int width)
    {
        //String template = "<table style=\"width: " + Integer.toString(width) +"%; text-align: left;\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr><td>[body]</td></tr></tbody></table>";
        String templateBegin = "<table style=\"width: 100%; text-align: left;\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr>";
        
        String sizedColumn = "<td style=\"width: " + Integer.toString(100-width) + "%;\"><br></td>";
        String textColumn = "<td style=\"vertical-align: top;\">" + string + "</td>";

        String templateEnd = "</tr></tbody></table>";
        return templateBegin + sizedColumn + textColumn + templateEnd;//StringToolkit.replaceFirst(template, "[body]", string);
    }
    
    static String coverStringToFont(String string, String fontName)
    {
        //TODO:
        return string;
    }
    
    static String createLink(HTMLFile fromFile, HTMLFile toFile, int level)
    {
        String preline = "";
        if (toFile.commentBefore != null)
        {
            preline = toFile.commentBefore;
            preline = coverStringToLineWithWidth(preline, 100-level*10);
            preline = coverStringToLine(preline, "rgb(255, 255, 255)");
        }
        
        
        String linkText = toFile.linkText;
        linkText = "<span style=\"color: rgb(0, 0, 0); text-decoration: none;\">" + linkText + "</span>";
        String link =  "<b>" + createLink(fromFile.destFile, toFile.destFile, linkText) + "</b>";
        //String link = "<b>" + createLink(fromFile, toFile) + "</b>";
        link = coverStringToLineWithWidth(link, 100-level*10);
        int color = 215 + 15*level;
        if (color > 255)
            color = 255;
        String colorString = "rgb(" + Integer.toString(color) + ", " + Integer.toString(color) + ", 255)";
        link = coverStringToLine(link, colorString);
        
        return preline+link;
    }
    
    static String createLink(HTMLFile fromFile, HTMLFile toFile)
    {
        String linkText = toFile.linkText;
        linkText = "<span style=\"color: rgb(0, 0, 0);\">" + linkText + "</span>";
        return createLink(fromFile.destFile, toFile.destFile, linkText);
    }
    
    static String createLink(File fromFile, File toFile, String linkText)
    {
        if (fromFile != toFile)
        {
            String link = "<a href=\"";
            link += com.oldportal.util.IOToolkit.getRelativePath(fromFile, toFile, "/");
            link += "\">";

            link += linkText;
            link += "</a>";

            return link;
        }
        else
        {// replace himself link with simple text:
            return linkText;
        }
    }
    
    
    
    /*
     * Site instruction file sample:
     * file "index.config" :
     * <NewSite>
     * filename "fileLinkDescription" 
     * filename "file2LinkDescription"
     * directory "directoryLinkDescription"
     * directory "directoryLinkDescription"
     *
     * options:
     * file = simple HTML file
     * directory = directory with index.htm or index.html file
     * NO = not process file with replacing, only insert links
     */
    
    
    
    
    
}

