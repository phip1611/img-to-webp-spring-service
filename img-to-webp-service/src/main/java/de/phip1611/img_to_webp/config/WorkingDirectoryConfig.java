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
            if (WORKING_DIRECTORY.mkdir()) {
                if (!WORKING_DIRECTORY.isDirectory()) {
                    System.err.println("Service can't access (or create) the Working Directory");
                    System.exit(-1);
                }
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