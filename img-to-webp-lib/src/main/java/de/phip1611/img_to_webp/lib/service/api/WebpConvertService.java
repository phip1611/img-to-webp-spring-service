package de.phip1611.img_to_webp.lib.service.api;

import de.phip1611.img_to_webp.lib.service.api.metadata.WebpConvertInput;
import de.phip1611.img_to_webp.lib.service.api.metadata.WebpConvertOutput;

import java.io.File;

/**
 * This class helps us to convert an image to webp. It needs to know
 * a folder where to do the conversion. The service needs write and read access
 * to the directory. Probably this will be your systems tmp dir.
 */
public interface WebpConvertService {

    /**
     * Converts an image to webp.
     *
     * @param input Conversion-Input
     * @param workingDirectory Working Directory (where to store the files temporarily)
     * @return Conversion-Output
     */
    WebpConvertOutput convert(WebpConvertInput input, String workingDirectory);

    /**
     * Converts an image to webp.
     *
     * @param input Conversion-Input
     * @param workingDirectory Working Directory (where to store the files temporarily)
     * @return Conversion-Output
     */
    WebpConvertOutput convert(WebpConvertInput input, File workingDirectory);

}
