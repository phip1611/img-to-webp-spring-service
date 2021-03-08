/*
 * Copyright 2020 Philipp Schuster
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.phip1611.img_to_webp.lib.service.data;

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
