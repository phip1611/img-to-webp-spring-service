package de.phip1611.img_to_webp.dto;

/**
 * Ein ImageDTO ist das Endprodukt der Konvertierung und wird an den Nutzer zurück gegeben.
 */
public class ImageDto {

    private boolean success;

    private byte quality;

    private int size;

    private int oldSize;

    private double savingsInPercent;

    private String base64String;

    public ImageDto() {
        this.success = true;
        this.size = 0;
        this.oldSize = 0;
    }

    public boolean isSuccess() {
        return success;
    }

    public double getQuality() {
        return quality;
    }

    public int getSize() {
        return size;
    }

    public int getOldSize() {
        return oldSize;
    }

    public double getSavingsInPercent() {
        return this.savingsInPercent;
    }

    public String getBase64String() {
        return base64String;
    }

    public ImageDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public ImageDto setBase64String(String base64String) {
        this.base64String = base64String;
        return this;
    }

    public ImageDto setSize(int size) {
        this.size = size;
        this.updateSavings();
        return this;
    }

    public ImageDto setQuality(byte quality) {
        this.quality = quality;
        return this;
    }

    public ImageDto setOldSize(int oldSize) {
        this.oldSize = oldSize;
        this.updateSavings();
        return this;
    }

    private void updateSavings() {
        if (oldSize != 0) {
            this.savingsInPercent = (1d - ((double)this.size / (double)this.oldSize)) * 100d;
        }
    }

    public static ImageDto failureDto() {
        return new ImageDto()
                .setSuccess(false)
                .setQuality((byte)0)
                .setBase64String(null);
    }
}
