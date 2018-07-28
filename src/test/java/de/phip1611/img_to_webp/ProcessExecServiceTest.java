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

    // serves as a default, always available command on (nearly) all systems
    private final String whichCommandForSystem;

    public ProcessExecServiceTest() {
        this.runsOnWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        this.whichCommandForSystem = this.runsOnWindows ? "where" : "which";
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

    // brauche ich vor allem um zu schauen ob richtige Ausgabe-Meldungen kommen
    @Test
    public void testBullshitCommandOnThisSystem() {
        String command = "bullshit";
        ProcessExecResult x = service.exec(command, System.getProperty("user.dir"));
        x.print(); // Wichtiger Output um Probleme auf Systemen zu debuggen, daher immer
        if (x.isSuccess()) {
            System.out.println("YOU cool motherfucker, amazing!");
            // das ist eh nur ein Test um zu schauen ob bei einem falschen Kommando die richtigen Debugmeldungen kommen
            // unwahrscheinlich dass jemand dieses Kommando auf seiner Maschine besitzt :D
        } else {
            Assert.assertFalse(x.isSuccess());
        }
    }

}
