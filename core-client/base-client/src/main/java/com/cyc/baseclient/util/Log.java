package com.cyc.baseclient.util;

/*
 * #%L
 * File: Log.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Provides the behavior and attributes of a event log for Base Client.<p>
 * 
 * TODO: replace this with SLF4J. - nwinant, 2015-06-25
 * 
 * Class Log provides a local log facility for Base Client agents.  Messages can be
 * written to a file, displayed to stdout, stderr.<p>
 *
 * user -Dcom.cyc.baseclient.util.log=file  to enable writing to a file.
 *
 * @version $Id: Log.java 163356 2016-01-04 20:55:47Z nwinant $
 * @author Stephen L. Reed
 */

import java.io.*;

@Deprecated
public class Log {
    /**
     * Current log instance.  For convenience in calling, an instance of the Log
     * is kept at by the class.  Static methods are forwarded to the current instance
     * so that users of the log need not keep track of the log instance themselves.
     */
    public static volatile Log current = null;

    /**
     * If true, write messages to the log file.
     */
    protected boolean writeToFile;

    /**
     * If true, write messages to System.out.
     */
    protected boolean writeToOut;

    /**
     * File pathname for the log file.
     */
    protected String logFilePath;

    /**
     * PrintWriter object for the log file.
     */
    protected PrintWriter printWriter;

    /**
     * BufferedWriter object for the log file.
     */
    protected BufferedWriter writer;

    /**
     * Default file name for the log file.
     */
    protected static final String DEFAULT_LOG_FILENAME = "cyc-api.log";

    /**
     * Constructs a new Log object according to java properties and store a reference
     * to it at the Log class.
     */
    public static void makeLog() {
        makeLog(DEFAULT_LOG_FILENAME);
    }

    /**
     * Constructs a new Log object according to java properties and store a reference
     * to it at the Log class.
     *
     * @param logName the pathname of the log file
     */
    public static void makeLog(String logName) {
        if (current != null) {
            return;
        }
        boolean writeToFile = false;
        boolean writeToOut = true;
        try {
            String logProperty = System.getProperty("com.cyc.baseclient.util.log", "default");
            if (logProperty.equalsIgnoreCase("file")) {
                writeToFile = true;
            }
            else if (! logProperty.equalsIgnoreCase("default"))
                System.err.println("Invalid value for property com.cyc.baseclient.util.log " +
                                   logProperty + " substituting default");
        }
        catch (SecurityException e) {
            writeToFile = false;
        }
        current = new Log(logName, writeToFile, writeToOut);
    }


    /**
     * Constructs a new Log object.  Display all messages only to
     * the default log file only if the runtime property is file.
     */
    public Log() {
        this(DEFAULT_LOG_FILENAME, false, false);
    }

    /**
     * Constructs a new Log object given the path.  Display all messages.
     *
     * @param logFilePath specifies the path for the log file.
     */
    public Log(String logFilePath) {
        this(logFilePath, false, true);
    }

    /**
     * Constructs a new Log object given all parameters.
     *
     * @param logFilePath specifies the path for the log file.
     * @param writeToFile if true, write messages to the log file.
     * @param writeToOut if true, write messages to System.out.
     */
    public Log(String logFilePath,
               boolean writeToFile,
               boolean writeToOut) {
        this.logFilePath = logFilePath;
        this.writeToFile = writeToFile;
        this.writeToOut = writeToOut;
        printWriter = null;
    }

    /**
     * Sets the log file path to the specified location.
     *
     * @param location the log file path
     */
    public void setStorageLocation(String location) throws IOException {
        if (printWriter != null)
            close();
        logFilePath = location;
    }

    /**
     * Returns the path of the log file.
     */
    public String getStorageLocation() {
        return logFilePath;
    }

    /**
     * Writes the int message to the log.
     *
     * @param message the int message to be logged.
     */
    public void println(int message) {
        println(Integer.toString(message));
    }

    /**
     * Writes a newline to the log.
     */
    public void println() {
        if (writeToOut)
            System.out.print("\n");
        if (writeToFile) {
            checkLogFile();
            printWriter.print("\n");
            printWriter.flush();
        }
    }

    /**
     * Writes a char to the log.
     *
     * @param c the char to be logged.
     */
    public void print(char c) {
        if (writeToOut)
            System.out.print(c);
        if (writeToFile) {
            checkLogFile();
            printWriter.print(c);
            printWriter.flush();
        }
    }

    /**
     * Writes the object's string representation to the log.
     *
     * @param object the object whose string representation is to be logged.
     */
    public void print(Object object) {
        print(object.toString());
    }

    /**
     * Writes the String message to the log.
     *
     * @param message the String message to be logged.
     */
    public void print(String message) {
        if (writeToOut)
            System.out.print(message);
        if (writeToFile) {
            checkLogFile();
            printWriter.print(message);
            printWriter.flush();
        }
    }

    /**
     * Writes the object's string representation to the log, followed by
     * a newline.
     *
     * @param object the object whose string representation is to be logged.
     */
    public void println(Object object) {
        this.println(object.toString());
    }

    /**
     * Writes the object's string representation to the log, followed by
     * a newline.
     *
     * @param message the String message to be logged.
     */
    public void println(String message) {
        if (writeToOut)
            System.out.println(message);
        if (writeToFile) {
            checkLogFile();
            printWriter.println(message);
            printWriter.flush();
        }
    }

    /**
     * Writes the error message to the log.
     *
     * @param errorMessage the error message to be logged.
     */
    public void errorPrintln(String errorMessage) {
        System.err.println(errorMessage);
        if (writeToFile) {
            checkLogFile();
            printWriter.println(errorMessage);
            printWriter.flush();
        }
    }

    /**
     * Writes the exception stack trace to the log.
     *
     * @param exception the exception to be reported.
     */
    public void printStackTrace(Exception exception) {
        exception.printStackTrace();
        if (writeToFile) {
            checkLogFile();
            exception.printStackTrace(printWriter);
            printWriter.flush();
        }
    }

    /**
     * Closes the log file.
     */
    public void close() {
        if (writeToFile && printWriter != null) {
            printWriter.close();
            printWriter = null;
        }
    }

    /**
     * Sets the current log instance.
     *
     * @param log a log object.
     */
    public static void setCurrent(Log log) {
        current = log;
    }

    /**
     * Opens the log file if not open and a log file was
     * specified.
     */
    protected void checkLogFile() {
        if (printWriter != null)
            return;
        try {
            printWriter = new PrintWriter( new BufferedWriter(new FileWriter(logFilePath)));
        }
        catch (IOException e) {
            System.err.println("Error creating log file " + logFilePath);
            System.err.println(e);
        }
        catch (SecurityException e) {
            System.out.println("Security policy does not permit a log file.");
            writeToFile = false;
        }
    }
}
