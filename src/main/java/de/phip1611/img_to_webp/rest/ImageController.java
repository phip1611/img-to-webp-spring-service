package de.phip1611.img_to_webp.rest;

import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;
import de.phip1611.img_to_webp.service.impl.ImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ImageController {
    private ImageServiceImpl service;

    @Autowired
    public ImageController(ImageServiceImpl service) {
        this.service = service;
    }

    @PostMapping("convert")
    public ImageDto convert(@Valid @RequestBody ImageInput input) {
        return this.service.convert(input);
    }
}
