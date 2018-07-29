package de.phip1611.img_to_webp.lib.service.api;

import java.io.File;

/**
 * Service that helps to run processes on the machine.
 */
public interface ProcessExecService {

    /**
     * Executes a command in the working directory.
     *
     * @param command Command to execute
     * @param workingDirectory Working Directory
     * @return Result of Command Execution
     */
    ProcessExecResult exec(String command, String workingDirectory);

    /**
     * Executes a command in the working directory.
     *
     * @param command Command to execute
     * @param workingDirectory Working Directory
     * @return Result of Command Execution
     */
    ProcessExecResult exec(String command, File workingDirectory);

    /**
     * Checks if a command is available on the machine.
     *
     * @param command to execute
     * @return command is available on the machine
     */
    boolean commandIsAvailable(String command);

}