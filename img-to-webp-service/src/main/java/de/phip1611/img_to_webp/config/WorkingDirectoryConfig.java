/*
 * Copyright 2020 Philipp Schuster
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.phip1611.img_to_webp.config;

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Holds the directory where all the operations will be made.
 * Also it checks if the working directory is ready for using.
 * Otherwise this class crashes the service.
 */
@Component // so that Spring creates it and executes the constructor
public class WorkingDirectoryConfig {

    public final static int MAX_AGE = 60 * 5; // 5 Minutes

    public final static int JOB_INTERVAL = 60 * 1000; // once per minute

    private static final String SERVICE_NAME = "spring_webp_converter_service";

    public static final File WORKING_DIRECTORY = new File(
        System.getProperty("java.io.tmpdir") + File.separator + SERVICE_NAME
    );

    public WorkingDirectoryConfig() {
        if (!WORKING_DIRECTORY.isDirectory()) {
            if (!WORKING_DIRECTORY.mkdir()) {
                System.err.println("Service can't access (or create) the Working Directory");
                System.exit(-1);
            }
        }

        if (!WORKING_DIRECTORY.canRead()) {
            System.err.println("Service can't read the Working Directory");
            System.exit(-1);
        }

        if (!WORKING_DIRECTORY.canWrite()) {
            System.err.println("Service can't write to the Working Directory");
            System.exit(-1);
        }

        System.out.println("Service will be using the following Working Directory: " + WORKING_DIRECTORY);
    }
}