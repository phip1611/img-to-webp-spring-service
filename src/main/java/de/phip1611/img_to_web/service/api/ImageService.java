package de.phip1611.img_to_web.service.api;

import de.phip1611.img_to_web.dto.ImageDto;
import de.phip1611.img_to_web.input.ImageInput;

public interface ImageService {
    ImageDto convert(ImageInput input);

    void assertValidate(ImageInput input);
}
