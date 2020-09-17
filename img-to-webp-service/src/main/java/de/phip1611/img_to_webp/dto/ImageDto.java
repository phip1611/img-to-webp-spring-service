/*
 * Copyright 2020 Philipp Schuster
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
                .setBase64String("");
    }
}
