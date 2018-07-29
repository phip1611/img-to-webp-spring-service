package de.phip1611.img_to_webp.lib.service.api.metadata;


import de.phip1611.img_to_webp.lib.service.util.Buildable;

import java.util.Arrays;
import java.util.Objects;

/**
 * The result of a successfully conversion of an image to webp.
 */
public class WebpConvertOutput {

    /**
     * Shows if the conversion was successfull.
     */
    private final boolean success;

    /**
     * The binary data of the webp image.
     */
    private final byte[] data;

    /**
     * Quality factor between 1 and 100.
     */
    private final int quality;

    public byte[] getData() {
        return data;
    }

    public int getQuality() {
        return quality;
    }

    public boolean isSuccess() {
        return success;
    }

    private WebpConvertOutput(boolean success, byte[] data, int quality) {
        this.success = success;
        this.data = data;
        this.quality = quality;
    }

    /**
     * Creates an output object for when there was a failure during conversion.
     *
     * @return object for when there was a failure
     */
    public static WebpConvertOutput failure() {
        return new WebpConvertOutput(false, new byte[0],-1);

    }

    /**
     * Builder for creating valid and successful conversion output objects.
     *
     * @return Builder for creating valid and successful conversion output objects.
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebpConvertOutput that = (WebpConvertOutput) o;
        return quality == that.quality &&
                Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(quality);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "WebpConvertOutput{" +
                "data=" + Arrays.toString(data) +
                ", quality=" + quality +
                '}';
    }

    /**
     * Constructs the webp conversion output for successful conversions.
     * If there was a failure during conversion then {@link #failure()} must be called instead of {@link #builder()}
     */
    public static class Builder implements Buildable<WebpConvertOutput> {

        /**
         * The binary data of the webp image.
         */
        private byte[] data;

        /**
         * Quality factor between 1 and 100.
         */
        private int quality;

        /**
         * Shows if the conversion was successfull.
         */
        private boolean success;

        public Builder() {
        }

        public Builder setData(byte[] data) {
            this.data = data;
            return this;
        }

        public Builder setQuality(int quality) {
            this.quality = quality;
            return this;
        }

        public Builder setSuccess(boolean success) {
            this.success = success;
            return this;
        }

        @Override
        public WebpConvertOutput build() {
            if (this.isValid()) {
                return new WebpConvertOutput(this.success, this.data, this.quality);
            } else {
                return null;
            }
        }

        @Override
        public boolean isValid() {
            return this.data != null
                    && this.data.length > 10
                    && this.quality > 0
                    && this.quality <= 100
                    && this.success; // if no success then .failure() must be called instead of .builder()
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "data=" + Arrays.toString(data) +
                    ", quality=" + quality +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Builder builder = (Builder) o;
            return quality == builder.quality &&
                    Arrays.equals(data, builder.data);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(quality);
            result = 31 * result + Arrays.hashCode(data);
            return result;
        }
    }
}
