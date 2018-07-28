package de.phip1611.img_to_webp.service.api;

public class ProcessExecResult {

    private final boolean success;

    private final String stdOut;

    private final String stdErr;

    private final String stackTrace;

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
}
