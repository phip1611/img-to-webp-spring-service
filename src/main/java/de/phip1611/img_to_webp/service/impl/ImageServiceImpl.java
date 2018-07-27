package de.phip1611.img_to_webp.service.impl;


import de.phip1611.img_to_webp.command.ImageConvertCommand;
import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;
import de.phip1611.img_to_webp.service.api.ImageService;
import de.phip1611.img_to_webp.util.ImageType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public ImageDto convert(ImageInput input) {
        ImageConvertCommand command = this.inputToCommand(input);
        if (command == null) {
            throw new IllegalArgumentException("ImageInput is not valid!");
        }

        File tmpDir = this.createAndGetTempDir();
        if (!tmpDir.isDirectory()) {
            throw new IllegalStateException(
                    "Das Temp-Dir \"" + tmpDir + "\" ist nicht da!!"
            );
        }

        boolean success = this.writeImageFileToTemp(command, tmpDir);
        if (!success) {
            throw new IllegalStateException("Die Datei konnte nicht ins Temp-Verzeichnis geschrieben werden.");
        }

        try {
            String execCommand = "cwebp -q ";
            execCommand += command.getQuality();
            execCommand += " " + command.getFullFileName();
            execCommand += " -o " + command.getFilename() + ".webp";
            System.out.println("Executing: " + execCommand);
            Process process = Runtime.getRuntime().exec(execCommand);
            BufferedReader stdInputReader = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

            BufferedReader stdErrorReader = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));

            Optional<String> stdOut = stdInputReader.lines().reduce(String::concat);
            Optional<String> stdErr = stdErrorReader.lines().reduce(String::concat);


            stdOut.ifPresent(System.out::println);

            if (stdErr.isPresent()) {
                throw new IllegalStateException("\"cwebp\" ist auf dem Host-System nicht vorhanden oder es gab andere Fehler bei der Konvertierung");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean writeImageFileToTemp(ImageConvertCommand command, File destination) {
        File destinationFile = new File(destination.getPath() + File.separator + command.getFullFileName());

        try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
            fos.write(command.getBinaryData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destinationFile.isFile();
    }

    @Override
    public File createAndGetTempDir() {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        File tmpDirApp = new File(
                tmpDir.getPath() + File.separator + SERVICE_FOLDER
        );
        if (tmpDir.isDirectory() && !tmpDirApp.isDirectory()) {
            boolean success = tmpDirApp.mkdir();
            if (!success) {
                throw new IllegalStateException("Konnte kein Verzeichnis im TMP Dir anlegen!");
            }
        }
        return tmpDirApp;
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
