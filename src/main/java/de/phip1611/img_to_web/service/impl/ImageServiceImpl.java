package de.phip1611.img_to_web.service.impl;


import de.phip1611.img_to_web.dto.ImageDto;
import de.phip1611.img_to_web.input.ImageInput;
import de.phip1611.img_to_web.service.api.ImageService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public ImageDto convert(ImageInput input) {
        this.assertValidate(input);
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

    @Override
    public void assertValidate(ImageInput input) {
    }
}
