/*
 * Copyright 2020 Philipp Schuster
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.phip1611.img_to_webp.service.impl;

import de.phip1611.img_to_webp.lib.service.api.ProcessExecService;
import de.phip1611.img_to_webp.service.api.TestMachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Tests on startup if the Machine supports the "cwebp" command!
 *
 * @author Philipp Schuster (phip1611.de)
 */
@Service
public class TestMachineServiceImpl implements TestMachineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestMachineServiceImpl.class);

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
            LOGGER.debug("cwebp is available on this machine, great!");
        } else {
            LOGGER.error("cwebp is NOT available on this machine, yikes!");
            System.exit(-1);
        }
    }
}
