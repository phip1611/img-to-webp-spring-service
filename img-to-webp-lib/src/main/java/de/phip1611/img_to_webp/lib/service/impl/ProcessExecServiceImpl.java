package de.phip1611.img_to_webp.lib.service.impl;

import de.phip1611.img_to_webp.lib.service.api.metadata.ProcessExecResult;
import de.phip1611.img_to_webp.lib.service.api.ProcessExecService;
import de.phip1611.img_to_webp.lib.service.api.RuntimeEnvHelper;

import java.io.*;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
public class ProcessExecServiceImpl implements ProcessExecService {

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
            System.out.println("Executing: '" + command + "' in '" + workingDirectory.getPath() + "'");
            process = runtime.exec(command, null, workingDirectory);
            exitCode = process.waitFor(); // Warten bis Prozess durchgelaufen ist, dannach geht es weiter.
            if (exitCode != 0) {
                System.err.println("Execution failed, Code: " + exitCode);
            } else {
                success = true;
            }
        } catch (IOException e) {
            System.err.println("Failure during execution!");
            stackTrace = Optional.of(e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Failure during waiting for process to finish!!");
            stackTrace = Optional.of(e.getMessage());
        } finally {
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
                if (!stdOut.isPresent()) {
                    if (stdErr.isPresent()) {
                        stdOut = stdErr;
                        stdErr = Optional.empty();
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
            System.err.println("Command not allowed for testing! A-z, 0-9, no spaces, no &&!");
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
            System.out.println("Command is available!");
        } else {
            System.out.println("Command is NOT available!");
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
        if (command.length() < 1 || command.length() > 10) {
            return false;
        }
        return command.matches("(([A-z])*([0-9])*)*");
    }

    private Optional<String> stdToString(InputStream is) {
        return new BufferedReader(new InputStreamReader(is)).lines().reduce(String::concat);
    }
}