package de.phip1611.img_to_webp.service.impl;

import de.phip1611.img_to_webp.service.api.ProcessExecResult;
import de.phip1611.img_to_webp.service.api.ProcessExecService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
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
            if (process != null) {
                stdOut  = this.stdToString(process.getInputStream());
                stdErr  = this.stdToString(process.getErrorStream());
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

    private Optional<String> stdToString(InputStream is) {
        return new BufferedReader(new InputStreamReader(is)).lines().reduce(String::concat);
    }
}