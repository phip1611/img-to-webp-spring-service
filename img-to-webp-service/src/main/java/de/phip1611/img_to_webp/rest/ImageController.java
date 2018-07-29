package de.phip1611.img_to_webp.rest;

import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;
import de.phip1611.img_to_webp.service.impl.ImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

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
