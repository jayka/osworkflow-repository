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
import java.util.*;


/**
 * Exception to encapsulate many exceptions.
 *
 * <h3>Very conceptual example</h3>
 *
 * <pre>
 * class MyException extends MultipartException {
 *   // No class body needed as overrides default constructor.
 * }
 *
 * public void doStuff() {
 *   try {
 *     checkEverything();
 *     println("Everything went fine.");
 *   }
 *   catch (MultipartException error) {
 *     Iterator i = error.list();
 *     println("Failed:");
 *     while(i.hasNext()) {
 *       println(" - " + (String)i.next() );
 *     }
 *   }
 * }
 *
 * private void checkEverything() throws MyException {
 *   MyException m = new MyException;
 *   // add some exceptions if methods don't check out.
 *   if ( !verifySomething() ) m.add("Something not verified");
 *   if ( !verifySomethingElse() ) m.add("Something else not verified");
 *   // also (just for fun), add an exception if another exception is called.
 *   try {
 *     someMethodThatThrowsAnException()
 *   }
 *   catch(SomeWeirdException e) {
 *     m.add(e)
 *   }
 *   // check if any exceptions have been added and re-throw in one go if they have.
 *   if (m.hasErrors()) throw m;
 * }
 *
 * </pre>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 11 $
 *
 * @see java.lang.Exception
 * @see java.util.List
 */
public class MultipartException extends Exception {
    //~ Instance fields ////////////////////////////////////////////////////////

    /**
     * List used internally to store Strings of error messages.
     */
    protected List errors;

    //~ Constructors ///////////////////////////////////////////////////////////

    /**
     * Default constructor. Initialises internal list.
     */
    public MultipartException() {
        errors = new ArrayList();
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Return error msgs as String array.
     */
    public synchronized String[] getArray() {
        String[] result = new String[errors.size()];
        Iterator i = getIterator();

        for (int c = 0; i.hasNext(); c++) {
            result[c] = (String) i.next();
        }

        return result;
    }

    /**
     * Return Iterator of String error msgs.
     */
    public Iterator getIterator() {
        return errors.iterator();
    }

    /**
     * Return error msgs as List of Strings.
     */
    public List getList() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * Add an error message to the Exception.
     *
     * @param msg Error message.
     */
    public void add(String msg) {
        errors.add(msg);
    }

    /**
     * Add an Exception's error message to the Exception.
     *
     * @param exception Exception of which <code>getMessage()</code> is called to add the message.
     */
    public void add(Throwable exception) {
        add(exception.getMessage());
    }

    /**
     * Check whether any errors have been added.
     */
    public boolean hasErrors() {
        return errors.size() > 0;
    }

    /**
     * Return Iterator of error msgs.
     *
     * @deprecated Use getIterator() instead.
     * @return Iterator of {@link java.lang.String} representations of error message.
     */
    public Iterator list() {
        return getIterator();
    }
}
