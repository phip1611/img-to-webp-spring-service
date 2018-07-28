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
        Optional<String> stdOut = Optional.empty();
        Optional<String> stdErr = Optional.empty();
        Optional<String> stackTrace = Optional.empty();

        try {
            System.out.println("Executing: '" + command + "' in '" + workingDirectory.getPath() + "'");
            Process process = runtime.exec(command, null, workingDirectory);
            int code = process.waitFor(); // Warten bis Prozess durchgelaufen ist, dannach geht es weiter.
            stdOut  = this.stdToString(process.getInputStream());
            stdErr  = this.stdToString(process.getErrorStream());
            if (code != 0) {
                System.err.println("Execution failed, Code: " + code);
            } else {
                success = true;
            }
        } catch (IOException e) {
            System.err.println("Fehler beim ausführen!");
            stackTrace = Optional.of(e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Fehler beim Warten auf den Prozess!");
            stackTrace = Optional.of(e.getMessage());
        }

        return new ProcessExecResult(
                success,
                stdOut.orElse(""),
                stdErr.orElse(""),
                stackTrace.orElse("")
        );
    }

    private Optional<String> stdToString(InputStream is) {
        return new BufferedReader(new InputStreamReader(is)).lines().reduce(String::concat);
    }
}