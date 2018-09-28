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

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package com.oldportal.util;

public class ServiceFunctions {
  public ServiceFunctions() {
  }

  public static boolean isWindowsOS()
  {
    if (java.io.File.separatorChar == '\\')
      return true;
    else
      return false;
  }
  public static boolean isUnixOS()
  {
    if (java.io.File.separatorChar == '/')
      return true;
    else
      return false;
  }

   public static void sort(java.util.Vector objects)
   {
       Object array[] = objects.toArray();
       java.util.Arrays.sort(array);
       objects.clear();
       for (int i=0; i<array.length; i++)
       {
           objects.add(array[i]);
       }
   }

    public static void copyFile(java.io.File from, java.io.File to) throws Exception
    {
        if (!from.exists())
            throw new IllegalArgumentException("Source file must exist. file: " + from.getCanonicalPath());

        if (to.exists())
            to.delete();
        java.io.FileInputStream in = new java.io.FileInputStream(from);
        java.io.FileOutputStream out = new java.io.FileOutputStream(to);
        while (in.available() > 0)
        {
            out.write(in.read());
        }
        in.close();
        out.close();
    }
}
