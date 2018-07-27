package de.phip1611.img_to_webp.dto;

/**
 * Ein ImageDTO ist das Endprodukt der Konvertierung und wird an den Nutzer zurück gegeben.
 */
public class ImageDto {

    private boolean success;

    private String base64String;

    private byte quality;

    private int size;

    public ImageDto() {
        this.success = true;
        this.size = 0;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getBase64String() {
        return base64String;
    }

    public double getQuality() {
        return quality;
    }

    public ImageDto setSize(int size) {
        this.size = size;
        return this;
    }

    public ImageDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public ImageDto setBase64String(String base64String) {
        this.base64String = base64String;
        return this;
    }

    public ImageDto setQuality(byte quality) {
        this.quality = quality;
        return this;
    }

    public int getSize() {
        return size;
    }

    public static ImageDto failureDto() {
        return new ImageDto()
                .setSuccess(false)
                .setQuality((byte)0)
                .setBase64String(null);
    }
}
