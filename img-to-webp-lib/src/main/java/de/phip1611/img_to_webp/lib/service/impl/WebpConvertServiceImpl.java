package de.phip1611.img_to_webp.lib.service.impl;

import de.phip1611.img_to_webp.lib.service.api.ProcessExecService;
import de.phip1611.img_to_webp.lib.service.api.WebpConvertService;
import de.phip1611.img_to_webp.lib.service.api.metadata.ProcessExecResult;
import de.phip1611.img_to_webp.lib.service.api.metadata.WebpConvertInput;
import de.phip1611.img_to_webp.lib.service.api.metadata.WebpConvertOutput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * {@inheritDoc}
 */
public class WebpConvertServiceImpl implements WebpConvertService {

    private final ProcessExecService execService;

    public WebpConvertServiceImpl() {
        this.execService = new ProcessExecServiceImpl();
    }

    @Override
    public WebpConvertOutput convert(WebpConvertInput input, String workingDirectory) {
        return this.convert(input, new File(workingDirectory));
    }

    @Override
    public WebpConvertOutput convert(WebpConvertInput input, File workingDirectory) {
        if (input == null) {
            System.err.println("Input is null!");
            return WebpConvertOutput.failure();
        }

        var res = this.checkWorkingDirectoryWorks(workingDirectory);
        if (!res) {
            System.err.println("Working directory is not useable! Is: " + workingDirectory.getPath());
            return WebpConvertOutput.failure();
        } else {
            System.out.println("Using working directory: " + workingDirectory.getPath());
        }

        boolean writeSuc = this.writeSourceFileToWorkingDirectory(input, workingDirectory);
        if (!writeSuc) {
            System.err.println("Could not write source file to working directory!");
            return WebpConvertOutput.failure();
        } else {
            System.out.println("Wrote source file successfully to working directory!");
        }

        ProcessExecResult execResult = this.execService.exec(this.buildCommandString(input), workingDirectory);
        execResult.print();
        if (!execResult.isSuccess()) {
            return WebpConvertOutput.failure();
        }

        byte[] data = this.readTargetFileFromWorkingDirectory(input, workingDirectory);
        WebpConvertOutput.Builder builder = WebpConvertOutput.builder()
                .setData(data)
                .setSuccess(true)
                .setQuality(input.getQuality());

        if (!builder.isValid()) {
            // this should be impossible to reach
            System.err.println("Something went really wrong, this should not happen! Out-File is not valid!");
            return WebpConvertOutput.failure();
        } else {
            return builder.build();
        }
    }

    private byte[] readTargetFileFromWorkingDirectory(WebpConvertInput input, File workingDirectory) {
        File webpOut = this.combineWorkingDirectoryAndFile(workingDirectory, input.getTargetFile());
        try {
            return Files.readAllBytes(webpOut.toPath());
        } catch (IOException e) {
            System.err.println("Failure reading target file: " + webpOut.getPath());
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * Writes the input as a file to the working directory so the compiler can access it
     * .
     * @param input Input
     * @param workingDirectory Working Directory
     * @return operation succeeded
     */
    private boolean writeSourceFileToWorkingDirectory(WebpConvertInput input, File workingDirectory) {
        File destinationFile = this.combineWorkingDirectoryAndFile(workingDirectory, input.getSourceFile());
        try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
            fos.write(input.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destinationFile.isFile() && destinationFile.getTotalSpace() > 0;
    }

    /**
     * Combines working directory path and source file to a full, usable, and valid path.
     *
     * @param sourceFile Source File
     * @param workingDirectory Working Directory
     * @return Combined paths as file object
     */
    private File combineWorkingDirectoryAndFile(File workingDirectory, File sourceFile) {
        return new File(workingDirectory.getPath() + File.separator + sourceFile.getPath());
    }

    /**
     * Checks if the working directory exists and is read- and writeable.
     *
     * @param workingDirectory Working Directory
     * @return working directory exists and is read- and writeable
     */
    private boolean checkWorkingDirectoryWorks(File workingDirectory) {
        return workingDirectory.isDirectory() && workingDirectory.canRead() && workingDirectory.canWrite();
    }

    private String buildCommandString(WebpConvertInput input) {
        String execCommand = "cwebp -q ";
        execCommand += input.getQuality();
        execCommand += " " + input.getSourceFileName();
        execCommand += " -o " + input.getTargetFileName();
        return execCommand;
    };

}
