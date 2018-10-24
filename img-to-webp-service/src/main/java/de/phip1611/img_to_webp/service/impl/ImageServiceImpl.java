package de.phip1611.img_to_webp.service.impl;


import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;
import de.phip1611.img_to_webp.lib.service.api.WebpConvertService;
import de.phip1611.img_to_webp.lib.service.api.metadata.ImmutableWebpConvertInput;
import de.phip1611.img_to_webp.lib.service.api.metadata.WebpConvertInput;
import de.phip1611.img_to_webp.lib.service.api.metadata.WebpConvertOutput;
import de.phip1611.img_to_webp.service.api.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

import static de.phip1611.img_to_webp.config.WorkingDirectoryConfig.WORKING_DIRECTORY;

@Service
public class ImageServiceImpl implements ImageService {

    private final WebpConvertService webpConvertService;

    @Autowired
    public ImageServiceImpl(WebpConvertService webpConvertService) {
        this.webpConvertService = webpConvertService;
    }

    @Override
    public ImageDto convert(ImageInput input) {
        WebpConvertInput webpInput;
        try {
            webpInput = this.transformInputForWebp(input);
        } catch (IllegalStateException ex) {
            System.err.println("Input is not valid! It's: " + input.toString());
            System.err.println("Exception message: " + ex.getMessage());
            return ImageDto.failureDto();
        }

        WebpConvertOutput output = this.webpConvertService.convert(webpInput, WORKING_DIRECTORY);

        return new ImageDto()
                .setQuality((byte)output.getQuality())
                .setBase64String(Base64.getEncoder().encodeToString(output.getData()))
                .setOldSize(webpInput.getData().length)
                .setSize(output.getData().length);
    }

    public WebpConvertInput transformInputForWebp(ImageInput input) {
        return ImmutableWebpConvertInput.builder()
                .data(Base64.getDecoder().decode(input.getBase64String().getBytes()))
                .fileExt(input.getFileExtension())
                .quality(input.getQuality())
                .build();
    }
}
