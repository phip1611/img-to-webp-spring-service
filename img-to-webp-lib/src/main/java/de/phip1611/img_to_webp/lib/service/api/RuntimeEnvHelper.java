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

/**
 * Helper class for runtime/machine specific stuff.
 *
 * @author Philipp Schuster (phip1611.de)
 */
public class RuntimeEnvHelper {

    private static RuntimeEnvHelper instance;

    private final String whichCommandForMachine;

    private final boolean runsOnWindows;

    public static RuntimeEnvHelper getInstance() {
        if (RuntimeEnvHelper.instance == null) {
            RuntimeEnvHelper.instance = new RuntimeEnvHelper();
        }
        return RuntimeEnvHelper.instance;
    }

    public RuntimeEnvHelper() {
        this.runsOnWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        this.whichCommandForMachine = this.runsOnWindows ? "where" : "which";
    }

    /**
     * Returns "where" (command) on Windows and "which" else.
     * The service is assuming, that this will be available on all
     * relevant systems where this code may ever run.
     *
     * @return "where" on windows and "which" else
     */
    public String getWhichForMachine() {
        return this.whichCommandForMachine;
    }

    /**
     * Returns if the code is executed on Windows.
     *
     * @return Code is executed on Windows
     */
    public boolean runsOnWindows() {
        return runsOnWindows;
    }
}
