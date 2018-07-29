package de.phip1611.img_to_webp.lib.service.api.metadata;

import java.util.Objects;

/**
 * The result of an executed process.
 */
public class ProcessExecResult {

    /**
     * Shows if there was no error.
     */
    private final boolean success;

    /**
     * Process-Output.
     */
    private final String stdOut;

    /**
     * Error-Output.
     */
    private final String stdErr;

    /**
     * Stack-Trace if a Java-Exception occurred.
     */
    private final String stackTrace;

    /**
     * Exit-Code of the process.
     */
    private final int exitCode;

    public ProcessExecResult(boolean success, String stdOut, String stdErr, String stackTrace, int exitCode) {
        this.success = success;
        this.stdOut = stdOut;
        this.stdErr = stdErr;
        this.stackTrace = stackTrace;
        this.exitCode = exitCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getStdOut() {
        return stdOut;
    }

    public String getStdErr() {
        return stdErr;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void print() {
        if (this.success) {
            System.out.println(this.toString());
        } else {
            System.err.println(this.toString());
        }
    }

    @Override
    public String toString() {
        return "ProcessExecResult{" +
                "success=" + success +
                ", exitCode='" + exitCode + '\'' +
                ", stdOut='" + stdOut + '\'' +
                ", stdErr='" + stdErr + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessExecResult that = (ProcessExecResult) o;
        return success == that.success &&
                exitCode == that.exitCode &&
                Objects.equals(stdOut, that.stdOut) &&
                Objects.equals(stdErr, that.stdErr) &&
                Objects.equals(stackTrace, that.stackTrace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, stdOut, stdErr, stackTrace, exitCode);
    }
}
