package de.phip1611.img_to_webp.lib;

import de.phip1611.img_to_webp.lib.service.data.ProcessExecResult;
import de.phip1611.img_to_webp.lib.service.api.ProcessExecService;
import de.phip1611.img_to_webp.lib.service.impl.ProcessExecServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class ProcessExecServiceTest {

    private ProcessExecService service;

    // serves as a default, always available command on (nearly) all systems
    private final String whichCommandForSystem;

    public ProcessExecServiceTest() {
        boolean runsOnWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        this.whichCommandForSystem = runsOnWindows ? "where" : "which";
        // else assume we are on a unix system (that has which installed)
    }

    @Before
    public void setUp() {
        this.service = new ProcessExecServiceImpl();
    }

    @Test
    public void testHasCwebpCommandOnThisSystem1() {
        ProcessExecResult x = service.exec(whichCommandForSystem + " cwebp", System.getProperty("user.dir"));
        x.print(); // Wichtiger Output um Probleme auf Systemen zu debuggen, daher immer
        Assert.assertTrue(x.isSuccess());
        Assert.assertFalse(x.getStdOut().isEmpty()); // Unix gibt "" zurück, wenn es bspw cwebp nicht gibt
    }

    @Test
    public void testHasCwebpCommandOnThisSystem2() {
        ProcessExecResult x = service.exec("cwebp -version", System.getProperty("user.dir"));
        x.print(); // Wichtiger Output um Probleme auf Systemen zu debuggen, daher immer
        Assert.assertTrue(x.isSuccess());
    }

    @Test
    public void testCanExecuteAnyCommandOnThisSystem() {
        String command = whichCommandForSystem + " " + whichCommandForSystem;
        ProcessExecResult x = service.exec(command, System.getProperty("user.dir"));
        x.print(); // Wichtiger Output um Probleme auf Systemen zu debuggen, daher immer
        Assert.assertTrue(x.isSuccess());
    }

    // good test to check proper debug output
    @Test
    public void testInvalidCommand() {
        // assuming no machine ever has this command
        String command = "foobarfoobar12345678";
        ProcessExecResult x = service.exec(command, System.getProperty("user.dir"));
        x.print(); // Wichtiger Output um Probleme auf Systemen zu debuggen, daher immer
        Assert.assertFalse(x.isSuccess());
    }

    @Test
    public void testCommandIsAvailable() {
        var x = service.commandIsAvailable(this.whichCommandForSystem);
        Assert.assertTrue(x);
    }

    @Test
    public void testMalformedCommandIsAvailable() {
        // Command is malformed
        var x = service.commandIsAvailable(UUID.randomUUID().toString());
        Assert.assertFalse(x);

        // Command is malformed
        x = service.commandIsAvailable(this.whichCommandForSystem + " && " + this.whichCommandForSystem);
        Assert.assertFalse(x);
    }

}
