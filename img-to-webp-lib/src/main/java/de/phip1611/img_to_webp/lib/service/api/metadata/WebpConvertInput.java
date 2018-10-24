package de.phip1611.img_to_webp.lib.service.api.metadata;

import org.immutables.value.Value;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.util.Preconditions.checkState;

/**
 * Everything we need to create the file in the file system,
 * locate it, convert it and find the target file.
 */
@Value.Immutable
public interface WebpConvertInput {

    /**
     * File-Extension for converted images.
     */
    String WEBP_FILE_EXTENSION = "webp";

    /**
     * Minimal file size (below is either error or spamming the service with nonsense)
     */
    int MIN_FILE_SIZE = 120; // no images has less bytes

    /**
     * Maximum file size.
     */
    int MAX_FILE_SIZE = 10485760; // 10 MebiByte

    /**
     * Default Quality for conversion.
     */
    int DEFAULT_QUALITY = 82;

    enum AllowedFileType {
        JPEG, JPG, PNG, TIFF;

        public static boolean isAllowed(String fileExt) {
            return List.of(AllowedFileType.values()).stream()
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .anyMatch(ext -> ext.equals(fileExt.toLowerCase()));
        }
    }

    /**
     * The binary data of the source image.
     */
    byte[] getData();

    /**
     * Get's the UUID for this input. Default is a Random UUID.
     *
     * @return UUID for this input.
     */
    @Value.Default
    default UUID getUuid() {
        return UUID.randomUUID();
    }

    /**
     * Returns the file extension of the original file.
     *
     * @return file extension of the original file.
     */
    String getFileExt();

    /**
     * A quality factor for the converter between 1 and 100.
     */
    @Value.Default
    default int getQuality() {
        return DEFAULT_QUALITY;
    };

    /**
     * Source-filenname in the working directory. Therefore there is no path
     * and just filename and extension in this.
     */
    default String getSourceFileName() {
        return this.getUuid().toString() + "-out." + this.getFileExt();
    }

    /**
     * Target-filename in the working directory. Therefore there is no path
     * and just filename and extension in this.
     */
    default String getTargetFileName() {
        return this.getUuid().toString() + "-out." + WEBP_FILE_EXTENSION;
    }

    /**
     * Returns a file object for the source File. This has no path,
     * it's only filename and extension!
     *
     * @return file object for the source File
     */
    default File getSourceFile() {
        return new File(this.getSourceFileName());
    }

    /**
     * Returns a file object for the target File. This has no path,
     * it's only filename and extension!
     *
     * @return file object for the target File
     */
    default File getTargetFile() {
        return new File(this.getTargetFileName());
    }

    @Value.Check
    default void check() {
        //checkState(this.getFileExt() != null, "'fileExt' must not be null!");
        // The generated Builder catches NPE automatically
        checkState(!this.getFileExt().isEmpty(), "'fileExt' must not be empty!");
        checkState(this.getFileExt().length() <= 4, "'fileExt' must be four or less characters long!");
        checkState(AllowedFileType.isAllowed(this.getFileExt()), "'fileExt' has wrong type! Must be one of: " + Arrays.toString(AllowedFileType.values()));


        //checkState(this.getData() != null, "'data' must not be null!");
        // the generated Builder catches NPE automatically
        checkState(this.getData().length >= MIN_FILE_SIZE, "'filesize' should be at least "+MIN_FILE_SIZE+" Bytes!");
        checkState(this.getData().length < MAX_FILE_SIZE, "'filesize' should be less than "+MAX_FILE_SIZE+" Bytes!");

        checkState(this.getQuality() > 0, "'quality' should be larger than '0'!");
        checkState(this.getQuality() <= 100, "'quality' should be less than or equals to '100'!");
    }
}

