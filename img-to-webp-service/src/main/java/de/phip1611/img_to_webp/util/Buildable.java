package de.phip1611.img_to_webp.util;

/**
 * Als Unterstützung für innere Builder-Klassen.
 *
 * @param <T> Generischer Typ
 */
public interface Buildable<T> {

    /**
     * Baut eine Klasse und nutzt dafür die {@link #isValid()}-Methode.
     *
     * @return Instanz von Typ T.
     */
    T build();

    /**
     * Zur Überprüfung in einem Build, ob eine Instanz gebaut werden kann.
     *
     * @return Objekt kann gebaut werden.
     */
    boolean isValid();
}
