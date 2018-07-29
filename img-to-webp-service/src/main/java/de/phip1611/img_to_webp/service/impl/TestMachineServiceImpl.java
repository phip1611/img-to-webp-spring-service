/* 
   Copyright ©: Philipp Schuster (2018)
   https://phip1611.de / phip1611@gmail.com
*/
package de.phip1611.img_to_webp.service.impl;

import de.phip1611.img_to_webp.lib.service.api.ProcessExecService;
import de.phip1611.img_to_webp.service.api.TestMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Tests on startup if the Machine supports the "cwebp" command!
 *
 * @author Philipp Schuster (phip1611.de)
 */
@Service
public class TestMachineServiceImpl implements TestMachineService {

    private final ProcessExecService processExecService;

    @Autowired
    public TestMachineServiceImpl(ProcessExecService processExecService) {
        this.processExecService = processExecService;
        this.assertCwebpAvailable();
    }

    @Override
    public void assertCwebpAvailable() {
        var res = this.processExecService.commandIsAvailable("cwebp");
        if (res) {
            System.out.println("cwebp is available on this machine, great!");
        } else {
            System.err.println("cwebp is NOT available on this machine, yikes!");
            System.exit(-1);
        }
    }
}
