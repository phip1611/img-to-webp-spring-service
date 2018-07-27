/* 
   Copyright ©: Philipp Schuster (2018)
   https://phip1611.de / phip1611@gmail.com
*/
package de.phip1611.img_to_webp.util;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;

/**
 * Enum aller erlaubten Dateitypen.
 *
 * @author Philipp Schuster (phip1611.de)
 */
public enum ImageType {
    JPG,
    JPEG,
    PNG,
    GIF,
    TIFF,
    WEBP;

    /**
     * Holt den entsprechenden Typ, der zu einem Dateiendung gehört.
     *
     * @param fileExtension Dateiendung des Bildes
     * @return ImageType
     */
    public static ImageType getTypeByString(String fileExtension) {
        if (fileExtension != null && !fileExtension.isEmpty() && fileExtension.length() <= 4) {
            for (ImageType imageType : ImageType.values()) {
                if (imageType.name().toLowerCase().equals(fileExtension.toLowerCase())) {
                    return imageType;
                }
            }
        }
        return null;
    }

    /**
     * Gibt den Namen des Bildtyps in Kleinbuchstaben zurück.
     *
     * @return Name des Bildtyps in Kleinbuchstaben
     */
    public String getName() {
        return this.name().toLowerCase();
    }

    public static boolean isValidType(String fileExtension) {
        if (!fileExtension.isEmpty() && fileExtension.length() <= 4) {
            var names = Arrays.stream(ImageType.values()).map(ImageType::getName).collect(toList());
            return names.contains(fileExtension);
        }
        return false;
    }
}
