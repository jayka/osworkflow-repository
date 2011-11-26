/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * FileManager
 *
 * @author Jason Carreira
 *         Created May 7, 2003 8:44:26 PM
 */
public class FileManager {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static Map files = Collections.synchronizedMap(new HashMap());
    protected static boolean reloadingConfigs = false;

    //~ Constructors ///////////////////////////////////////////////////////////

    private FileManager() {
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public static void setReloadingConfigs(boolean reloadingConfigs) {
        FileManager.reloadingConfigs = reloadingConfigs;
    }

    public static boolean isReloadingConfigs() {
        return reloadingConfigs;
    }

    public static boolean fileNeedsReloading(String fileName) {
        FileRevision revision = (FileRevision) files.get(fileName);

        if ((revision == null) && reloadingConfigs) {
            // no revision yet and we keep the revision history, so 
            // the file needs to be loaded for the first time
            return true;
        } else if (revision == null) {
            return false;
        }

        return revision.getLastModified() < revision.getFile().lastModified();
    }

    /**
     * Loads opens the named file and returns the InputStream
     *
     * @param fileName - the name of the file to open
     * @return an InputStream of the file contents or null
     * @throws IllegalArgumentException if there is no file with the given file name
     */
    public static InputStream loadFile(String fileName, Class clazz) {
        URL fileUrl = ClassLoaderUtil.getResource(fileName, clazz);

        if (fileUrl == null) {
            return null;
        }

        InputStream is;

        try {
            is = fileUrl.openStream();

            if (is == null) {
                throw new IllegalArgumentException("No file '" + fileName + "' found as a resource");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("No file '" + fileName + "' found as a resource");
        }

        if (isReloadingConfigs()) {
            File file = new File(fileUrl.getFile());
            long lastModified;

            if (!file.exists() || !file.canRead()) {
                file = null;
            }

            if (file != null) {
                lastModified = file.lastModified();
                files.put(fileName, new FileRevision(file, lastModified));
            }
        }

        return is;
    }

    //~ Inner Classes //////////////////////////////////////////////////////////

    private static class FileRevision {
        private File file;
        private long lastModified;

        public FileRevision(File file, long lastUpdated) {
            this.file = file;
            this.lastModified = lastUpdated;
        }

        public File getFile() {
            return file;
        }

        public void setLastModified(long lastModified) {
            this.lastModified = lastModified;
        }

        public long getLastModified() {
            return lastModified;
        }
    }
}
