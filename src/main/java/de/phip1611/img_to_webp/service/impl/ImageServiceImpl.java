package de.phip1611.img_to_webp.service.impl;


import de.phip1611.img_to_webp.command.ImageConvertCommand;
import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;
import de.phip1611.img_to_webp.service.api.ImageService;
import de.phip1611.img_to_webp.util.ImageType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public ImageDto convert(ImageInput input) {
        ImageConvertCommand command = this.inputToCommand(input);
        if (command == null) {
            System.err.println("ImageInput is not valid!");
            return ImageDto.failureDto();
        }

        File tmpDir = this.createAndGetTempDir();
        if (!tmpDir.isDirectory()) {
            System.err.println("Das Temp-Dir \"" + tmpDir + "\" ist nicht vorhanden!");
            return ImageDto.failureDto();
        }

        boolean success = this.writeImageFileToTemp(command, tmpDir);
        if (!success) {
            System.err.println("Die Datei konnte nicht ins Temp-Verzeichnis geschrieben werden.");
            return ImageDto.failureDto();
        }

        try {
            String execCommand = "cwebp -q ";
            execCommand += command.getQuality();
            execCommand += " " + command.getFullFileName();
            execCommand += " -o " + command.getFilename() + ".webp";
            System.out.println("Executing: " + execCommand);

            // letzter parameter is present working directory
            Process process = Runtime.getRuntime().exec(execCommand, null, tmpDir);
            int exidCode = process.waitFor();
            BufferedReader stdOutReader = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));
                    // ja auch wenn hier error steht, das ist stdout und stderr gemeinsam :/

            Optional<String> processOut = stdOutReader.lines().reduce(String::concat);
            if (exidCode != 0) {
                System.err.println("\"cwebp\" ist auf dem Host-System nicht vorhanden oder es gab andere Fehler bei der Konvertierung:");
                System.err.println(processOut.get());
                return ImageDto.failureDto();
            }

            byte[] webpData = this.getConvertedImageFromTemp(this.getFullFile(tmpDir, command.toFile()));
            if (webpData.length == 0) {
                System.err.println("Fehler bei der Konvertierung, Zieldatei ist leer!");
            }
            ImageDto dto = new ImageDto()
                    .setQuality(command.getQuality())
                    .setBase64String(Base64.getEncoder().encodeToString(webpData))
                    .setSize(webpData.length);

            return dto;

        } catch (IOException | InterruptedException e) {
            System.err.println("Fehler bei Konvertierung");
            e.printStackTrace();
        }

        return ImageDto.failureDto();
    }

    @Override
    public boolean writeImageFileToTemp(ImageConvertCommand command, File destination) {
        File destinationFile = this.getFullFile(destination, command.toFile());

        try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
            fos.write(command.getBinaryData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destinationFile.isFile();
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
                throw new IllegalStateException("Konnte kein Verzeichnis im TMP Dir anlegen!");
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
    public ImageConvertCommand inputToCommand(ImageInput input) {
        return ImageConvertCommand.getBuilder()
                .setBinaryData(Base64.getDecoder().decode(input.getBase64String().getBytes()))
                .setFileext(ImageType.getTypeByString(input.getFileExtension()))
                .setQuality(input.getQuality())
                .build();
    }
}
