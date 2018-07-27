package de.phip1611.img_to_web.input;

import de.phip1611.img_to_web.util.ImageType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ImageInput {

    public static final byte DEFAULT_WEBP_QUALITY = 87;

    @NotNull
    private ImageType imageType;

    @NotEmpty
    private String imageBase64M;

    @Min(1)
    @Max(100)
    private byte quality = DEFAULT_WEBP_QUALITY;

    public ImageInput() {
    }

    public ImageInput setImageType(ImageType imageType) {
        this.imageType = imageType;
        return this;
    }

    public ImageInput setImageBase64(String imageBase64M) {
        this.imageBase64M = imageBase64M;
        return this;
    }

    public ImageInput setQuality(byte quality) {
        this.quality = quality;
        return this;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public String getImageBase64() {
        return imageBase64M;
    }

    public byte getQuality() {
        return quality;
    }
}
