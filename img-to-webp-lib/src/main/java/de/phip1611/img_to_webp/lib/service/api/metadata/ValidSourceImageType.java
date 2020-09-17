/*
 * Copyright 2020 Philipp Schuster
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.phip1611.img_to_webp.lib.service.api.metadata;

import java.util.Optional;

/**
 * All image formats, that "cwebp" can convert to webp.
 */
public enum ValidSourceImageType {
    JPG,
    JPEG,
    PNG,
    TIFF,
    WEBP;

    /**
     * Resolves a file-extension string to the corresponding enum type.
     *
     * @param fileExtension File Extension of image
     * @return ImageType
     */
    public static Optional<ValidSourceImageType> getTypeByString(String fileExtension) {
        if (fileExtension != null && !fileExtension.isEmpty() && fileExtension.length() <= 4) {
            for (ValidSourceImageType type : ValidSourceImageType.values()) {
                if (type.getNameLowercase().equals(fileExtension.toLowerCase())) {
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Checks if a file extension string is valid.
     *
     * @param fileExtension File Extension of image
     * @return ImageType
     */
    public static boolean isValid(String fileExtension) {
        return ValidSourceImageType.getTypeByString(fileExtension).isPresent();
    }

    /**
     * Returns the ENUM name in lowercase.
     *
     * @return name in lowercase
     */
    public String getNameLowercase() {
        return this.name().toLowerCase();
    }

    @Override
    public String toString() {
        return this.getNameLowercase();
    }
}
