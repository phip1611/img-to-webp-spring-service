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
