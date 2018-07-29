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
        var x = ValidSourceImageType.getTypeByString(fileExtension);
        if (x.isPresent()) {
            return true;
        } else {
            return false;
        }
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
