package de.phip1611.img_to_webp.service.api;

import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;

/**
 * Image-Service.
 */
public interface ImageService {

    /**
     * Konvertiert ein Bild zu WebP.
     *
     * @param input ImageInput
     * @return WebP Kodiertes Bild
     */
    ImageDto convert(ImageInput input);
}
