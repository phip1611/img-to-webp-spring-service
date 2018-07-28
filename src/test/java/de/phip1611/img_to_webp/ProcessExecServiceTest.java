package de.phip1611.img_to_webp;

import de.phip1611.img_to_webp.service.api.ProcessExecService;
import de.phip1611.img_to_webp.service.api.ProcessExecResult;
import de.phip1611.img_to_webp.service.impl.ProcessExecServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProcessExecServiceTest {

    private ProcessExecService service;

    private final boolean runsOnWindows;

    private final String defaultCommandForSystem;

    public ProcessExecServiceTest() {
        this.runsOnWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        this.defaultCommandForSystem = this.runsOnWindows ? "where" : "which";
        // else assume we are on a unix system (that has which installed)
    }

    @Before
    public void setUp() {
        this.service = new ProcessExecServiceImpl();
    }

    @Test
    public void testHasCwebpCommandOnThisSystem() {
        ProcessExecResult x = service.exec("cwebp -version", System.getProperty("user.dir"));
        if (!x.isSuccess()) {
            x.print();
            Assert.fail();
        }
    }

    @Test
    public void testCanExecuteAnyCommandOnThisSystem() {
        String command = defaultCommandForSystem + " " + defaultCommandForSystem;
        ProcessExecResult x = service.exec(command, System.getProperty("user.dir"));
        if (!x.isSuccess()) {
            x.print();
            Assert.fail();
        }
    }

}
