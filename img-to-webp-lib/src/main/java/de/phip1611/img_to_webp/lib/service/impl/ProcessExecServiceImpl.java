/*
 * Copyright 2020 Philipp Schuster
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.phip1611.img_to_webp.lib.service.impl;

import de.phip1611.img_to_webp.lib.service.api.ProcessExecService;
import de.phip1611.img_to_webp.lib.service.api.RuntimeEnvHelper;
import de.phip1611.img_to_webp.lib.service.data.ProcessExecResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
public class ProcessExecServiceImpl implements ProcessExecService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessExecServiceImpl.class);

    private final Runtime runtime;

    public ProcessExecServiceImpl() {
        this.runtime = Runtime.getRuntime();
    }

    @Override
    public ProcessExecResult exec(String command, String workingDirectory) {
        return this.exec(command, new File(workingDirectory));
    }

    @Override
    public ProcessExecResult exec(String command, File workingDirectory) {
        boolean success = false;
        int exitCode = -1;
        Optional<String> stdOut = Optional.empty();
        Optional<String> stdErr = Optional.empty();
        Optional<String> stackTrace = Optional.empty();

        Process process = null;
        try {
            LOGGER.debug("Executing: '" + command + "' in '" + workingDirectory.getPath() + "'");
            process = runtime.exec(command, null, workingDirectory);
            exitCode = process.waitFor(); // Warten bis Prozess durchgelaufen ist, dannach geht es weiter.
            if (exitCode != 0) {
                LOGGER.error("Execution failed, Code: " + exitCode);
            } else {
                success = true;
            }
        } catch (IOException e) {
            LOGGER.error("Failure during execution!");
            stackTrace = Optional.of(e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.error("Failure during waiting for process to finish!!");
            stackTrace = Optional.of(e.getMessage());
        } finally {
            // when there was no exception
            if (process != null) {
                // hier unten erst die jeweiligen Ausgaben holen
                // da wir sonst das oben für den try und jeden catch block einzeln machen müsssten!
                stdOut  = this.stdToString(process.getInputStream());
                stdErr  = this.stdToString(process.getErrorStream());

                // in some cases (for example when running webp)
                // the output lands in stdErr even if everything was successfull
                // at least on windows
                // therefore let's try the following (swap)
                // not really a bug but it's confusing
                if (exitCode == 0) {
                    if (stdOut.isEmpty()) {
                        if (stdErr.isPresent()) {
                            stdOut = stdErr;
                            stdErr = Optional.empty();
                        }
                    }
                }
            }
        }

        return new ProcessExecResult(
                success,
                stdOut.orElse(""),
                stdErr.orElse(""),
                stackTrace.orElse(""),
                exitCode
        );
    }

    @Override
    public boolean commandIsAvailable(String command) {
        command = command.trim();
        boolean isAllowed = this.checkTestCommandAllowed(command);
        if (!isAllowed) {
            System.err.println("Command not allowed for testing! A-z, 0-9, 1 <= length <= 20, no spaces, no &&!");
            return false;
        }

        String someDir = System.getProperty("user.dir");
        if (someDir == null) {
            someDir = System.getProperty("java.io.tmpdir");
            if (someDir == null) {
                System.err.println("Can't retrieve any directory on the system to run a command in!");
            }
            return false;
        }

        File someDirFile = new File(someDir);
        if (!someDirFile.isDirectory()) {
            System.err.println("Can't retrieve any directory on the system to run a command in!");
            return false;
        }

        // will be e.g. "which which", "which webp", "where whatever" (where on Windows)
        String commandToExecute = RuntimeEnvHelper.getInstance().getWhichForMachine() + " " + command;
        ProcessExecResult result = this.exec(
                commandToExecute,
                someDirFile
        );

        if (result.isSuccess()) {
            LOGGER.debug("Command is available!");
        } else {
            LOGGER.debug("Command is NOT available!");
        }
        result.print();

        return result.isSuccess();
    }

    /**
     * Validates if the command that should be checked on this machine is
     * a command without any parameters, e.g. no "rm -rf ..." but only "rm"
     * @param command
     * @return
     */
    private boolean checkTestCommandAllowed(String command) {
        if (command == null) {
            return false;
        }
        if (command.length() < 1 || command.length() > 20) {
            return false;
        }
        return command.matches("(([A-z])*([0-9])*)*");
    }

    private Optional<String> stdToString(InputStream is) {
        return new BufferedReader(new InputStreamReader(is)).lines().reduce(String::concat);
    }
}