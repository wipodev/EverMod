package net.evermod.client.gui.api.style;


/**
 * Composite interface for purely structural components like space and layout breaks.
 *
 * @param <T> The self type for fluent chaining.
 * @author Wipodev
 */
public interface SpaceStyleable<T extends SpaceStyleable<T>> extends
    Sizable<T>,
    Marginable<T> {
}
