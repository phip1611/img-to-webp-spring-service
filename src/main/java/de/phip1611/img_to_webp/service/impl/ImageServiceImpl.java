package de.phip1611.img_to_webp.service.impl;


import de.phip1611.img_to_webp.command.ImageConvertCommand;
import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;
import de.phip1611.img_to_webp.service.api.ImageService;
import de.phip1611.img_to_webp.service.api.ProcessExecResult;
import de.phip1611.img_to_webp.service.api.ProcessExecService;
import de.phip1611.img_to_webp.util.ImageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class ImageServiceImpl implements ImageService {

    private final ProcessExecService processExecService;

    @Autowired
    public ImageServiceImpl(ProcessExecService processExecService) {
        this.processExecService = processExecService;
    }

    @Override
    public ImageDto convert(ImageInput input) {
        ImageConvertCommand command = this.inputToCommand(input);
        if (command == null) {
            System.err.println("ImageInput is not valid!");
            return ImageDto.failureDto();
        }

        File tmpDir = this.createAndGetTempDir();
        if (!tmpDir.isDirectory()) {
            System.err.println("The Temp-Dir \"" + tmpDir + "\" is not available!");
            return ImageDto.failureDto();
        }

        System.out.println("The service is using the Temp-Dir:");
        System.out.println(tmpDir.toPath());

        System.out.print("Write file \"" + command.getFile().getPath() + "\" to Temp-Dir: ");
        boolean success = this.writeImageFileToTemp(command, tmpDir);
        if (!success) {
            System.err.print("File could not be written to Temp-Dir!\n");
            return ImageDto.failureDto();
        } else {
            System.out.print("Erfolg\n");
        }

        String execCommand = this.buildCommandString(command);

        ProcessExecResult x = this.processExecService.exec(execCommand, tmpDir);
        if (!x.isSuccess()) {
            System.err.println("Fehlschlag!");
            x.print();
            return ImageDto.failureDto();
        }

        byte[] webpData = this.getConvertedImageFromTemp(this.getFullFile(tmpDir, command.getOutFile()));
        if (webpData.length == 0) {
            System.err.println("Error during conversion, Target-File is empty!");
            return ImageDto.failureDto();
        }

        return new ImageDto()
                .setQuality(command.getQuality())
                .setBase64String(Base64.getEncoder().encodeToString(webpData))
                .setOldSize(command.getBinaryData().length)
                .setSize(webpData.length);
    }

    @Override
    public boolean writeImageFileToTemp(ImageConvertCommand command, File destination) {
        File destinationFile = this.getFullFile(destination, command.getFile());

        try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
            fos.write(command.getBinaryData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destinationFile.isFile() && destination.getTotalSpace() > 0;
    }

    @Override
    public byte[] getConvertedImageFromTemp(File webpFile) {
        if (webpFile.isFile()) {
            try {
                return Files.readAllBytes(webpFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    @Override
    public File createAndGetTempDir() {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        File tmpDirApp = this.getFullFile(tmpDir, SERVICE_FOLDER);
        if (tmpDir.isDirectory() && !tmpDirApp.isDirectory()) {
            boolean success = tmpDirApp.mkdir();
            if (!success) {
                throw new IllegalStateException("Could not create directory in Temp-ir!");
            }
        }
        return tmpDirApp;
    }

    @Override
    public File getFullFile(File dir, File file) {
        return new File(dir.getPath() + File.separator + file.getPath());
    }

    @Override
    public File getFullFile(File dir, String file) {
        return this.getFullFile(dir, new File(file));
    }

    @Override
    public String buildCommandString(ImageConvertCommand convertCommand) {
        String execCommand = "cwebp -q ";
        execCommand += convertCommand.getQuality();
        execCommand += " " + convertCommand.getFile().getPath();
        execCommand += " -o " + convertCommand.getOutFile().toPath();
        return execCommand;
    }

    @Override
    public ImageConvertCommand inputToCommand(ImageInput input) {
        return ImageConvertCommand.getBuilder()
                .setBinaryData(Base64.getDecoder().decode(input.getBase64String().getBytes()))
                .setFileext(ImageType.getTypeByString(input.getFileExtension()))
                .setQuality(input.getQuality())
                .build();
    }
}
