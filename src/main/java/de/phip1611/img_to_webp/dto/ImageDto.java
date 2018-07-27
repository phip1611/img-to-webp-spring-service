package de.phip1611.img_to_webp.dto;

/**
 * Ein ImageDTO ist das Endprodukt der Konvertierung und wird an den Nutzer zurück gegeben.
 */
public class ImageDto {

    private boolean success;

    private String imageBase64M;

    private byte quality;

    public ImageDto() {
    }

    public boolean isSuccess() {
        return success;
    }

    public String getImageBase64M() {
        return imageBase64M;
    }

    public double getQuality() {
        return quality;
    }

    public ImageDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public ImageDto setImageBase64M(String imageBase64M) {
        this.imageBase64M = imageBase64M;
        return this;
    }

    public ImageDto setQuality(byte quality) {
        this.quality = quality;
        return this;
    }
}
