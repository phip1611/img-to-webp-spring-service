/*
 * Copyright 2020 Philipp Schuster
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.phip1611.img_to_webp.lib.service.data;

import java.io.PrintStream;
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
        PrintStream ps = this.success ? System.out : System.err;
        ps.println(this.toString());
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
