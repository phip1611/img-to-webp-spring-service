package de.phip1611.img_to_web.service.impl;


import de.phip1611.img_to_web.dto.ImageDto;
import de.phip1611.img_to_web.input.ImageInput;
import de.phip1611.img_to_web.service.api.ImageService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public ImageDto convert(ImageInput input) {
        this.assertValidate(input);
        String fileName = this.writeToTmpFile(input);
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("cwebp");
            BufferedReader stdInputReader = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

            BufferedReader stdErrorReader = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));

            String stdIn  = stdInputReader.lines().reduce(String::concat).get();
            String stdOut = stdErrorReader.lines().collect(Collectors.joining());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String writeToTmpFile(ImageInput input) {
        String tmpDirPath = System.getProperty("java.io.tmpdir");
        File tmpDir = new File(tmpDirPath);
        UUID uniqueTempFileName = UUID.randomUUID();
        String fileExt = input.getImageType().name().toLowerCase();
        String fileName = uniqueTempFileName + "." + fileExt;

        if (tmpDirPath == null
                || tmpDirPath.isEmpty()) {
            throw new IllegalStateException("Temp-Dir ist nicht vorhanden oder konnte nicht erkannt werden.");
        }
        if (!tmpDir.isDirectory()
                || !tmpDir.canRead()
                || !tmpDir.canExecute()
                || !tmpDir.canWrite()) {
            throw new IllegalStateException("Keine Lese-, Ausführ- und Schreibberechtigung für das Temp-Dir.");
        }

        byte[] byteArray = Base64.getDecoder().decode(input.getImageBase64().getBytes());
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(byteArray);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    @Override
    public void assertValidate(ImageInput input) {
    }
}
