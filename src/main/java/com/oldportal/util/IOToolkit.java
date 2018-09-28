/*
* Utilites Library.
* Copyright (C) Dmitry Ognyannikov, 2005
* E-Mail: sirius_plus@yahoo.com , dmitry@oldportal.com
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/


package com.oldportal.util;

import java.io.*;
import java.security.MessageDigest;

/**
 *
 * @author Dmitry Ognyannikov
 */
public class IOToolkit {

    /** Creates a new instance of IOToolkit */
    public IOToolkit() {
    }

//    public static void testRelativePath()
//    {
//        File fileFrom = new File("E:\\Downloads\\Library\\Oracle\\10ganddbb2udbcwp.pdf");
//        File fileTo = new File("E:\\Downloads\\Library\\Oracle\\KeyFeaturesCRM.pdf");
//        String relativePath = getRelativePath(fileFrom, fileTo, File.separator);
//        System.out.println(relativePath);
//
//        File fileFrom2 = new File("E:\\Downloads\\Library\\Oracle\\");
//        File fileTo2 = new File("E:\\Downloads\\Library\\Oracle\\KeyFeaturesCRM.pdf");
//        String relativePath2 = getRelativePath(fileFrom2, fileTo2, File.separator);
//        System.out.println(relativePath2);
//
//        File fileFrom3 = new File("E:\\Downloads\\Java1.5\\langspec-1.0.pdf");
//        File fileTo3 = new File("E:\\Downloads\\Library\\Oracle\\KeyFeaturesCRM.pdf");
//        String relativePath3 = getRelativePath(fileFrom3, fileTo3, File.separator);
//        System.out.println(relativePath3);
//    }

    public static String getRelativePath(File fileFrom, File fileTo, String separator)
    {
        // from file directories path:
        java.util.Vector<File> fileFromParents = new java.util.Vector<File>();
        if (fileFrom.isDirectory())
            fileFromParents.add(fileFrom);

        File fromParent = fileFrom.getParentFile();

        while (fromParent!=null)
        {
            fileFromParents.add(0, fromParent);
            fromParent = fromParent.getParentFile();
        }

        if (fileFromParents.size() == 0)
            return null;

        // to file directories path:
        java.util.Vector<File> fileToParents = new java.util.Vector<File>();
        if (fileTo.isDirectory())
            fileToParents.add(fileTo);

        File toParent = fileTo.getParentFile();

        while (toParent!=null)
        {
            fileToParents.add(0, toParent);
            toParent = toParent.getParentFile();
        }

        if (fileToParents.size() == 0)
            return null;

        // compare paths and find shared parent directory:
        int pathLastEqualIndex = -1;  
        
        for (int i=0; i<fileFromParents.size() && i<fileToParents.size(); i++)
        {
            File fromParentDir = fileFromParents.get(i);
            File toParentDir = fileToParents.get(i);
            if (fromParentDir.getAbsolutePath().equals(toParentDir.getAbsolutePath()))
            {
                pathLastEqualIndex = i;
            }
            else break;
        }

        String path = "";

        int stepsBack = fileFromParents.size() - pathLastEqualIndex - 1;

        if (stepsBack == 0)
            path = "."+separator;
        else
        for (int i=0; i<stepsBack; i++)
        {
            path += ".." + separator;
        }

        for (int i=pathLastEqualIndex+1; i<fileToParents.size(); i++)
        {
            path += fileToParents.get(i).getName() + separator;
        }

        path += fileTo.getName();

        return path;
    }

    public static String getFileExtension(String fileName)
    {
        for (int i=fileName.length()-1; i>=0; i--)
        {
            if (fileName.charAt(i) == '.')
            if (i < fileName.length()-1)
                return fileName.substring(i+1);
            else
                return "";

            if (fileName.charAt(i) == java.io.File.separatorChar)
                return "";

            if (fileName.charAt(i) == '/')
                return "";
        }
        return "";
    }
    
    public static Strings loadTextFile(File file)
    {
        try{
        Strings ret = new Strings();
        
        java.io.FileReader fileReader = new java.io.FileReader(file);
        java.io.BufferedReader reader = new java.io.BufferedReader(fileReader);
        
        while (reader.ready())
        {
            String str = reader.readLine();
            ret.add(str);
        }
        
        reader.close();
        fileReader.close();
        
        return ret;
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean saveTextFile(File file, Strings text)
    {
        try{
        Strings ret = new Strings();
        
        java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(file));
        
        for (int i=0; i<text.size(); i++)
        {
            writer.write(text.get(i));
            if (i<(text.size()-1))
                writer.newLine();
        }
        
        writer.close();
        
        return true;
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    /**
    * Create text string from text file.
    */
    public static String loadFile(java.io.File file, String stringEncoding)
    {
        return loadFile(file.getAbsolutePath(), stringEncoding);
    }

   /**
    * Create text string from text file.
    */
    public static String loadFile(String filename, String stringEncoding)
    {
        try{
          java.io.InputStream in = new java.io.FileInputStream(filename);
          // charset settings:
          java.nio.charset.Charset charset = java.nio.charset.Charset.forName(stringEncoding);
          //TODO: set charset to reader
          java.io.InputStreamReader reader = new java.io.InputStreamReader(in, charset);
          char c;
          String ret = "";
          String temp = "";
          int nChar = 0;
          while ((nChar = reader.read()) >= 0)
          {
              //if ((char)nChar == '\r')
                  //continue;
            temp+=(char)nChar;
            if (temp.length() >= 8)
            {
              ret += temp;
              temp = "";
            }
          }
          ret += temp;
          return ret;

        } catch (Exception ex){
          ex.printStackTrace();
          return null;
        }
    }

    /**
    * Create text file from string with defined string end symbols.
    */
    public static boolean writeFile(java.io.File file, String string, String stringEncoding, String stringEndTemplate)
    {
        return writeFile(file.getAbsolutePath(), string, stringEncoding, stringEndTemplate);
    }

   /**
    * Create text file from string with defined string end symbols.
    */
    public static boolean writeFile(String filename, String string, String stringEncoding, String stringEndTemplate)
    {
        try{
          // charset settings:
          java.nio.charset.Charset charset = java.nio.charset.Charset.forName(stringEncoding);

          File outFile = new File(filename);
          if (!outFile.exists())
          {
              File parentDir = outFile.getParentFile();
              if (parentDir != null)
              {
	              if (!parentDir.exists())
	                parentDir.mkdirs();
	              
	              if (!outFile.createNewFile())
	                  return false;
              }
          }
          if (!outFile.canWrite())
              return false;

          java.io.OutputStream out = new java.io.FileOutputStream(outFile);
          com.oldportal.util.Strings strings = new com.oldportal.util.Strings();
          strings.loadFromString(string);
          for (int i=0; i<strings.size(); i++)
          {
            String line = strings.get(i);

            out.write(toBytes(charset, line));

            if (i < (strings.size()-1))
              out.write(toBytes(charset, stringEndTemplate));
          }
          out.close();
          return true;
        } catch (Exception ex){
          ex.printStackTrace();
          return false;
        }
    }

    static private byte[] toBytes(java.nio.charset.Charset charset, String string)
    {
        java.nio.ByteBuffer buffer = charset.encode(string);
        return buffer.array();
    }

    static public String getFileMD5(File file)
    {
        try{
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            FileInputStream in = new FileInputStream(file);
            byte buf1[] = new byte[1];
            byte buf16[] = new byte[16];
            while (in.available() > 0)
            {
                if (in.available() >= 16)
                {
                    in.read(buf16);
                    digest.update(buf16);
                }
                else
                {
                    in.read(buf1);
                    digest.update(buf1);
                }
            }
            byte result[] = digest.digest();
            in.close();

            // convert digest result to HEX string
            return StringToolkit.toHexString(result);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    static public String getFileSHA(File file)
    {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.reset();
            FileInputStream in = new FileInputStream(file);
            byte buf1[] = new byte[1];
            byte buf16[] = new byte[16];
            while (in.available() > 0)
            {
                if (in.available() >= 16)
                {
                    in.read(buf16);
                    digest.update(buf16);
                }
                else
                {
                    in.read(buf1);
                    digest.update(buf1);
                }
            }
            byte result[] = digest.digest();
            in.close();

            // convert digest result to HEX string
            return StringToolkit.toHexString(result);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    /** return true if files content is equal */
    static public boolean compareFilesByContent(File file1, File file2)
    {// сравнить 2 файла по их содержанию
        if (file1.length() != file2.length())
            return false;

        try{
            FileInputStream in1 = new FileInputStream(file1);
            FileInputStream in2 = new FileInputStream(file2);

            while (in1.available() > 0)
            {
                if (in1.read() != in2.read())
                    return false;
            }

            in1.close();
            in2.close();

            return true;
        }catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

}
