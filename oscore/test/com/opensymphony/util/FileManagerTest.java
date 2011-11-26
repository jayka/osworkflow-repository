/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import junit.framework.TestCase;


/**
 * FileManagerTest
 * @author Jason Carreira
 * Created May 8, 2003 3:35:46 PM
 */
public class FileManagerTest extends TestCase {
    //~ Instance fields ////////////////////////////////////////////////////////

    private boolean wasReloading;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setUp() {
        wasReloading = FileManager.isReloadingConfigs();
        FileManager.setReloadingConfigs(true);
    }

    //TODO Hani commented this out. This test makes assumptions about
    //the file timestamp resolution on the underlying FS. which just
    //don't hold true on non-Windows. On OSX this test will always fail,
    //and on linux it'll fail periodically. Writing to a file is NOT
    //guaranteed to change its timestamp
    public void errorTestFileChanged() {
        try {
            File file = getFile();
            String fileName = file.getName();
            assertNotNull(FileManager.loadFile(fileName, FileManagerTest.class));

            Thread.sleep(50);

            OutputStream out = new FileOutputStream(file);
            out.write(32);
            out.close();
            assertTrue(FileManager.fileNeedsReloading(fileName));
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void tearDown() {
        FileManager.setReloadingConfigs(wasReloading);
    }

    public void testFileUnchanged() {
        try {
            File file = getFile();
            String fileName = file.getName();
            assertNotNull(FileManager.loadFile(fileName, FileManagerTest.class));
            assertFalse(FileManager.fileNeedsReloading(fileName));
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private File getFile() throws IOException {
        URL xworkUrl = ClassLoaderUtil.getResource("somefile.xml", FileManagerTest.class);
        File xworkFile = new File(xworkUrl.getFile());
        File dirFile = xworkFile.getParentFile();
        File tmpFile = File.createTempFile("FileManagerTest", ".txt", dirFile);

        return tmpFile;
    }
}
