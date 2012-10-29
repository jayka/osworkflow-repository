/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;


/* ====================================================================
 * The OpenSymphony Software License, Version 1.1
 *
 * (this license is derived and fully compatible with the Apache Software
 * License - see http://www.apache.org/LICENSE.txt)
 *
 * Copyright (c) 2001 The OpenSymphony Group. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        OpenSymphony Group (http://www.opensymphony.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "OpenSymphony" and "The OpenSymphony Group"
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact license@opensymphony.com .
 *
 * 5. Products derived from this software may not be called "OpenSymphony"
 *    or "OSCore", nor may "OpenSymphony" or "OSCore" appear in their
 *    name, without prior written permission of the OpenSymphony Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */

/**
 * Lots of quick file utility methods
 *
 * @author <a href="mailto:mcannon@internet.com">Mike Cannon-Brookes</a>
 * @author <a href="mailto:pkan@internet.com">Patrick Kan</a>
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 11 $
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

import java.net.*;

import java.text.*;

import java.util.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 11 $
 */
public class FileUtils {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final Log log = LogFactory.getLog(FileUtils.class);

    //~ Constructors ///////////////////////////////////////////////////////////

    public FileUtils() {
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Get an InputStream from specified resource uri. URI can be absolute or relative filename, or complete
     * URL.
     */
    public final static InputStream getResource(String uri) throws IOException {
        try {
            return new URL(uri).openStream();
        } catch (MalformedURLException mue) {
            return new FileInputStream(uri);
        }
    }

    public final static File checkBackupDirectory(File file) {
        File backupDirectory = new File(file.getParent() + System.getProperty("file.separator") + "osedit_backup");

        if (!backupDirectory.exists()) {
            backupDirectory.mkdirs();
        }

        return backupDirectory;
    }

    public final static File createFile(String path) {
        File file = new File(path);

        try {
            file.createNewFile();
        } catch (IOException e) {
            log.error(e);
        }

        return file;
    }

    public final static String[] dirList(String path) {
        return dirList(new File(path));
    }

    public final static String[] dirList(File path) {
        String[] list;

        list = path.list();

        return list;
    }

    public final static String readFile(File file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));

            String s = new String();
            StringBuffer buffer = new StringBuffer();

            while ((s = in.readLine()) != null) {
                buffer.append(s + "\n");
            }

            in.close();

            return buffer.toString();
        } catch (FileNotFoundException e) {
            log.warn("File not found");
        } catch (IOException e) {
            log.error(e);
        }

        return null;
    }

    public final static void write(File file, String content) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));

            out.write(content);
            out.close();
        } catch (FileNotFoundException e) {
            log.warn("File not found", e);
        } catch (IOException e) {
            log.error(e);
        }
    }

    public final static void writeAndBackup(File file, String content) {
        try {
            DateFormat backupDF = new SimpleDateFormat("ddMMyy_hhmmss");

            File backupDirectory = checkBackupDirectory(file);

            File original = new File(file.getAbsolutePath());

            File backup = new File(backupDirectory, original.getName() + "." + backupDF.format(new Date()));

            if (log.isDebugEnabled()) {
                log.debug("Backup file is " + backup);
            }

            original.renameTo(backup);

            BufferedWriter out = new BufferedWriter(new FileWriter(file));

            out.write(content);
            out.close();
        } catch (FileNotFoundException e) {
            log.warn("File not found", e);
        } catch (IOException e) {
            log.error(e);
        }
    }
}
