package de.phip1611.img_to_webp.service.api;

import de.phip1611.img_to_webp.command.ImageConvertCommand;
import de.phip1611.img_to_webp.dto.ImageDto;
import de.phip1611.img_to_webp.input.ImageInput;

import java.io.File;
import java.util.Optional;

/**
 * Image-Service.
 */
public interface ImageService {

    /**
     * Das Verzeichnis im Temp-Ordner, in dem der Service arbeitet.
     */
    // "-" geht nicht zumindest auf windows
    String SERVICE_FOLDER = "spring_webp_converter_service";

    int MAX_FILE_SIZE = 10485760; // 10 MebiByte

    /**
     * Erstellt ein ImageConvertCommand aus einem ImageInput.
     *
     * @param input ImageInput
     * @return ImageConvertCommand
     */
    ImageConvertCommand inputToCommand(ImageInput input);

    /**
     * Konvertiert ein Bild zu WebP.
     *
     * @param input ImageInput
     * @return WebP Kodiertes Bild
     */
    ImageDto convert(ImageInput input);

    /**
     * Schreibt das Bild für die Konvertierung in ein Temp-Verzeichnis und gibt
     * zurück ob die Operation erfolgreich war.
     *
     * @param command ImageConvertCommand
     * @param destination Ziel-Verzeichnis
     * @return erfolg
     */
    boolean writeImageFileToTemp(ImageConvertCommand command, File destination);

    /**
     * Holt das eben konvertiere File aus dem Temp-Verzeichnis.
     *
     * @param webpFile Konvertiertes Bild im webp Format
     * @return  Optional<Byte[]> Binärdaten vom konvertieren WebP-Bild
     */
    byte[] getConvertedImageFromTemp(File webpFile);

    /**
     * Gibt das Temp-Verzeichnis zurück, in dem Bilder auf dem Server für die Konvertierung zwischengespeichert werden.
     * Wenn das Temp-Verzeichnis der Laufzeitumgebung vorhanden ist, wird versucht darin einen projektspezifischen
     * Ordner anzulegen.
     *
     * @return Temp-Verzeichnis, in dem Bilder auf dem Server zwischengespeichert werden.
     */
    File createAndGetTempDir();

    /**
     * Gibt den vollen Datei-System-Pfad zu einer Datei zurück bzw. kombiniert
     * ein Verzeichnis und eine Datei.
     *
     * @param dir Verzeichnis
     * @param file Datei
     * @return Komplettes File mit kompletten Pfad.
     */
    File getFullFile(File dir, File file);

    /**
     * Gibt den vollen Datei-System-Pfad zu einer Datei zurück bzw. kombiniert
     * ein Verzeichnis und eine Datei.
     *
     * @param dir Verzeichnis
     * @param file Datei
     * @return Komplettes File mit kompletten Pfad.
     */
    File getFullFile(File dir, String file);
}
