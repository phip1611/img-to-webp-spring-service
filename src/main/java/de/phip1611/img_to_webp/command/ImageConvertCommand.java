package de.phip1611.img_to_webp.command;

import de.phip1611.img_to_webp.util.Buildable;
import de.phip1611.img_to_webp.util.ImageType;

import java.util.UUID;

import static de.phip1611.img_to_webp.service.api.ImageService.MAX_FILE_SIZE;

/**
 * Ein ImageConvertCommand, das alle Properties hat, die der Konverter benötigt.
 */
public class ImageConvertCommand {

    private final String filename;

    private ImageType fileext;

    private byte[] binaryData;

    private byte quality;

    private ImageConvertCommand() {
        this.filename = UUID.randomUUID().toString();
    }

    /**
     * Gibt den Dateinamen ohne Dateiendung zurück.
     *
     * @return Dateinamen ohne Dateiendung
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Gibt die Dateierweiterung zurück.
     *
     * @return Dateierweiterung
     */
    public ImageType getFileext() {
        return fileext;
    }

    private ImageConvertCommand setFileext(ImageType fileext) {
        this.fileext = fileext;
        return this;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    private ImageConvertCommand setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
        return this;
    }

    public byte getQuality() {
        return quality;
    }

    /**
     * Gibt Dateinamen mit Extension aus.
     *
     * @return Dateiname mit Extension
     */
    public String getFullFileName() {
        return this.filename + "." + this.fileext.getName();
    }

    private ImageConvertCommand setQuality(byte quality) {
        this.quality = quality;
        return this;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder implements Buildable<ImageConvertCommand> {

        private ImageType fileext;

        private byte[] binaryData;

        private byte quality;

        private Builder() {
        }

        public Builder setFileext(ImageType fileext) {
            this.fileext = fileext;
            return this;
        }

        public Builder setBinaryData(byte[] binaryData) {
            this.binaryData = binaryData;
            return this;
        }

        public Builder setQuality(byte quality) {
            this.quality = quality;
            return this;
        }

        @Override
        public ImageConvertCommand build() {
            if (!this.isValid()) {
                return null;
            }
            ImageConvertCommand command = new ImageConvertCommand()
                    .setBinaryData(this.binaryData)
                    .setFileext(this.fileext)
                    .setQuality(this.quality);
            return command;
        }


        @Override
        public boolean isValid() {
            return this.quality > 0
                    && this.quality <= 100
                    && this.binaryData.length > 100 // Es gibt keine Bilder unter 100 bytes
                    && this.binaryData.length <= MAX_FILE_SIZE
                    && this.fileext != null;
        }
    }
}
