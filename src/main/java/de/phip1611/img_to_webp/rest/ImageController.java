package de.phip1611.img_to_webp.rest;

import de.phip1611.img_to_webp.service.impl.ImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    private ImageServiceImpl service;

    @Autowired
    public ImageController(ImageServiceImpl service) {
        this.service = service;
    }


}
