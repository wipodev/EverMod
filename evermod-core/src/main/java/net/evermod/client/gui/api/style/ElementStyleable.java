package net.evermod.client.gui.api.style;

public interface ElementStyleable<T extends ElementStyleable<T>> extends
    Sizable<T>,
    Paddable<T>,
    Marginable<T>,
    Borderable<T>,
    Backgroundable<T> {
}
