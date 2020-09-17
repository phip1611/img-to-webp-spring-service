/*
 * Copyright 2020 Philipp Schuster
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.phip1611.img_to_webp.lib.service.api;

import de.phip1611.img_to_webp.lib.service.api.metadata.ProcessExecResult;

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