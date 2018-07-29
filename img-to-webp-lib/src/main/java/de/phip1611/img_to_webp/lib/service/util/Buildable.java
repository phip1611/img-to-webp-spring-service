package de.phip1611.img_to_webp.lib.service.util;

/**
 * A support class for the Builders of POJOs.
 *
 * @param <T> The type to build.
 */
public interface Buildable<T> {

    /**
     * Builds a new instance if {@link #isValid()} returns true.
     *
     * @return Instanz von Typ T.
     */
    T build();

    /**
     * Checks if the builder's properties are valid so that
     * we can construct the POJO.
     *
     * @return Objekt kann gebaut werden.
     */
    boolean isValid();
}
