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

        System.out.println("Der Spring Img-To-WebP-Service nutzt das Temp-Verzeichnis:");
        System.out.println(tmpDir.toPath());

        System.out.print("Schreibe Datei \"" + command.getFullFileName() + "\" in Temp-Verzeichnis: ");
        boolean success = this.writeImageFileToTemp(command, tmpDir);
        if (!success) {
            System.err.print("Die Datei konnte nicht ins Temp-Verzeichnis geschrieben werden!\n");
            return ImageDto.failureDto();
        } else {
            System.out.print("Erfolg\n");
        }

        String execCommand = "cwebp -q ";
        execCommand += command.getQuality();
        execCommand += " " + command.getFullFileName();
        execCommand += " -o " + command.getFilename() + ".webp";
        System.out.println("Executing: \"" + execCommand + "\" in Temp-Verzeichnis");

        Process process = null;
        int exidCode;
        try {
            process = Runtime.getRuntime().exec(execCommand, null, tmpDir);
            exidCode = process.waitFor();
        } catch (IOException e) {
            System.out.println("Fehlschlag, konnte Kommando nicht ausführen.");
            e.printStackTrace();
            return ImageDto.failureDto();
        } catch (InterruptedException e) {
            System.out.println("Fehlschlag, konnte nicht auf Prozess warten.");
            e.printStackTrace();
            return ImageDto.failureDto();
        }

        if (exidCode != 0) {
            System.out.println("Der Prozess gibt einen Fehler als Rückmeldung: EXIT_CODE=" + exidCode);
            return ImageDto.failureDto();
        } else {
            System.out.println("Erfolgreich konvertiert.");
            System.out.println(this.getOutStreamContent(process.getErrorStream()));
        }

        byte[] webpData = this.getConvertedImageFromTemp(this.getFullFile(tmpDir, command.getWebpFile()));
        if (webpData.length == 0) {
            System.err.println("Fehler bei der Konvertierung, Zieldatei ist leer!");
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

    private String getOutStreamContent(InputStream inputStream) {
        BufferedReader stdOutReader = new BufferedReader(new
                InputStreamReader(inputStream));
        return stdOutReader.lines().reduce(String::concat).orElse("");
    }
}
