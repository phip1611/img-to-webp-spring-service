/* 
   Copyright ©: Philipp Schuster (2018)
   https://phip1611.de / phip1611@gmail.com
*/
package de.phip1611.img_to_webp.service.api;

/**
 * Deletes old files in the temp directory.
 *
 * @author Philipp Schuster (phip1611.de)
 */
public interface CleanWorkingDirectoryService {

    /**
     * Regularly deletes old files from the working directory.
     */
    void cleanWorkingDirectoryFromGarbage();
}
