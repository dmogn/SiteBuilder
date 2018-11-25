/* 
 * OldPortal Utilites Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.oldportal.util;

public class ServiceFunctions {

    public ServiceFunctions() {
    }

    public static boolean isWindowsOS() {
        if (java.io.File.separatorChar == '\\') {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isUnixOS() {
        if (java.io.File.separatorChar == '/') {
            return true;
        } else {
            return false;
        }
    }

    public static void sort(java.util.Vector objects) {
        Object array[] = objects.toArray();
        java.util.Arrays.sort(array);
        objects.clear();
        for (int i = 0; i < array.length; i++) {
            objects.add(array[i]);
        }
    }

    public static void copyFile(java.io.File from, java.io.File to) throws Exception {
        if (!from.exists()) {
            throw new IllegalArgumentException("Source file must exist. file: " + from.getCanonicalPath());
        }

        if (to.exists()) {
            to.delete();
        }
        java.io.FileInputStream in = new java.io.FileInputStream(from);
        java.io.FileOutputStream out = new java.io.FileOutputStream(to);
        while (in.available() > 0) {
            out.write(in.read());
        }
        in.close();
        out.close();
    }
}
