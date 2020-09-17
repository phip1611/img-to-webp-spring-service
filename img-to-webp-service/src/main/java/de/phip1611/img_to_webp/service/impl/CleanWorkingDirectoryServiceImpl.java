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

import de.phip1611.img_to_webp.service.api.CleanWorkingDirectoryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

import static de.phip1611.img_to_webp.config.WorkingDirectoryConfig.JOB_INTERVAL;
import static de.phip1611.img_to_webp.config.WorkingDirectoryConfig.MAX_AGE;
import static de.phip1611.img_to_webp.config.WorkingDirectoryConfig.WORKING_DIRECTORY;

@Service
public class CleanWorkingDirectoryServiceImpl implements CleanWorkingDirectoryService {

    @Scheduled(fixedRate = JOB_INTERVAL)
    @Override
    public void cleanWorkingDirectoryFromGarbage() {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(WORKING_DIRECTORY.getAbsolutePath()))) {
            for (Path path : directoryStream) {
                FileTime fileTime = Files.getLastModifiedTime(path);
                Instant instantOfFile = fileTime.toInstant();
                long fileSeconds = instantOfFile.getEpochSecond();
                long nowSeconds = Instant.now().getEpochSecond();
                if (nowSeconds - MAX_AGE > fileSeconds) {
                    System.out.println("Cleaning working directory: "
                            + path.toString()
                            + " - "
                            + (path.toFile().delete() ? "success" : "could not delete")
                    );
                }
            }
        } catch (IOException ignored) {}
    }
}
