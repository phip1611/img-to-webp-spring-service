/* 
   Copyright ©: Philipp Schuster (2018)
   https://phip1611.de / phip1611@gmail.com
*/
package de.phip1611.img_to_web.util;

import java.util.Optional;

/**
 * Class created: 2018-07-27 01:03
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

    public static Optional<ImageType> getTypeByString(String type) {
        if (type != null && !type.isEmpty() && type.length() <= 4) {
            for (ImageType imageType : ImageType.values()) {
                if (imageType.name().toLowerCase().equals(type.toLowerCase())) {
                    return Optional.of(imageType);
                }
            }
        }
        return Optional.empty();
    }
}
