/* 
   Copyright ©: Philipp Schuster (2018)
   https://phip1611.de / phip1611@gmail.com
*/
package de.phip1611.img_to_webp.service.api;

/**
 * Tests on startup if the Machine supports the "cwebp" command!
 *
 * @author Philipp Schuster (phip1611.de)
 */
public interface TestMachineService {

    /**
     * Let's the service crashes, if "cwebp" is not installed!
     */
    public void assertCwebpAvailable();
}
