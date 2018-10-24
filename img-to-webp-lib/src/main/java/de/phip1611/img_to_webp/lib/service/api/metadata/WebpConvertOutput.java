package de.phip1611.img_to_webp.lib.service.api.metadata;

import java.util.Arrays;
import java.util.Objects;

/**
 * The result of a successfully conversion of an image to webp.
 */
public final class WebpConvertOutput {

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

    public WebpConvertOutput(byte[] data, int quality) {
        this(true, data, quality);
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
}
