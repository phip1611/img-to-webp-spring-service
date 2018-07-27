package de.phip1611.img_to_webp.input;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * Ein Input für ein Image.
 */
public class ImageInput {

    /**
     * Standardqualität für den Converter.
     */
    public static final byte DEFAULT_WEBP_QUALITY = 82;

    /**
     * Dateiendung des Bildes.
     */
    @NotEmpty
    @Min(3)
    @Max(4)
    private String fileExtension;

    /**
     * Base64-Repräsentation der Binärdaten.
     */
    @NotEmpty
    private String base64String;

    /**
     * Die Ziel-Qualität zu der Konvertiert werden soll.
     */
    @Min(1)
    @Max(100)
    private byte quality = DEFAULT_WEBP_QUALITY;

    public ImageInput() {
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public ImageInput setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
        return this;
    }

    public String getBase64String() {
        return base64String;
    }

    public ImageInput setBase64String(String imageBase64) {
        this.base64String = imageBase64;
        return this;
    }

    public byte getQuality() {
        return quality;
    }

    public ImageInput setQuality(byte quality) {
        this.quality = quality;
        return this;
    }
}
