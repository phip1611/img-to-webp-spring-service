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
