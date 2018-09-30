package de.phip1611.img_to_webp.lib.service.api.metadata;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Everything we need to create the file in the file system,
 * locate it, convert it and find the target file.
 */
public class WebpConvertInput {

    /**
     * File-Extension for converted images.
     */
    public final static String WEBP_FILE_EXTENSION = "webp";

    /**
     * Minimal file size (below is either error or spamming the service with nonsense)
     */
    public final static int MIN_FILE_SIZE = 120; // no images has less bytes

    /**
     * Maximum file size.
     */
    public final static int MAX_FILE_SIZE = 10485760; // 10 MebiByte

    /**
     * Default Quality for conversion.
     */
    public final static int DEFAULT_QUALITY = 82;

    /**
     * Source-filenname in the working directory. Therefore there is no path
     * and just filename and extension in this.
     */
    private final String sourceFileName;

    /**
     * Target-filename in the working directory. Therefore there is no path
     * and just filename and extension in this.
     */
    private final String targetFileName;

    /**
     * The binary data of the source image.
     */
    private final byte[] data;

    /**
     * A quality factor for the converter between 1 and 100.
     */
    private final int quality;

    private WebpConvertInput(String fileExt,
                             byte[] data,
                             int quality) {
        String randomUuid = UUID.randomUUID().toString();
        this.sourceFileName = randomUuid + "." + fileExt;
        // -out, because we can also have .webp as input (to change quality e.g.)
        this.targetFileName = randomUuid + "-out." + WEBP_FILE_EXTENSION;

        this.quality = quality;
        this.data = data;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    /**
     * Returns a file object for the source File. This has no path,
     * it's only filename and extension!
     *
     * @return file object for the source File
     */
    public File getSourceFile() {
        return new File(sourceFileName);
    }

    /**
     * Returns a file object for the target File. This has no path,
     * it's only filename and extension!
     *
     * @return file object for the target File
     */
    public File getTargetFile() {
        return new File(targetFileName);
    }

    public byte[] getData() {
        return data;
    }

    public int getQuality() {
        return quality;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "WebpConvertInput{" +
                "sourceFileName='" + sourceFileName + '\'' +
                ", targetFileName='" + targetFileName + '\'' +
                ", data=" + Arrays.toString(data) +
                ", quality=" + quality +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebpConvertInput input = (WebpConvertInput) o;
        return quality == input.quality &&
                Objects.equals(sourceFileName, input.sourceFileName) &&
                Objects.equals(targetFileName, input.targetFileName) &&
                Arrays.equals(data, input.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(sourceFileName, targetFileName, quality);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    /**
     * Builder that helps us to generate a valid WebpConvertInput.
     */
    public static class Builder {

        /**
         * The binary data of the source image.
         */
        private byte[] data;

        /**
         * The file-extension of the source image. Lowercase and 3 to 4 characters.
         */
        private String fileExt;

        /**
         * A quality factor for the converter between 1 and 100.
         */
        private int quality = DEFAULT_QUALITY;

        /**
         * Returns null or a completely valid object.
         *
         * @return null or a completely valid object
         */
        public WebpConvertInput build() {
            if (this.isValid()) {
                return new WebpConvertInput(
                        this.fileExt,
                        this.data,
                        this.quality
                );
            } else {
                return null;
            }
        }

        /**
         * Checks if a valid POJO can be build.
         *
         * @return a valid POJO can be build
         */
        public boolean isValid() {
            return this.data != null
                    && this.data.length + 1 > MIN_FILE_SIZE // + 1 because array index starts at 0
                    && this.data.length + 1 < MAX_FILE_SIZE
                    && this.quality > 0
                    && this.quality <= 100
                    && this.fileExt != null
                    && ValidSourceImageType.isValid(this.fileExt);
        }

        public Builder setData(byte[] data) {
            this.data = data;
            return this;
        }

        public Builder setQuality(int quality) {
            this.quality = quality;
            return this;
        }

        public Builder setFileExt(String fileExt) {
            if (fileExt != null) {
                fileExt = fileExt.toLowerCase();
            }
            this.fileExt = fileExt;
            return this;
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "data=" + Arrays.toString(data) +
                    ", fileExt='" + fileExt + '\'' +
                    ", quality=" + quality +
                    '}';
        }
    }
}
