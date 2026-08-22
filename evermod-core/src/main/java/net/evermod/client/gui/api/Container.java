package net.evermod.client.gui.api;

import java.util.List;
import java.util.Optional;
import net.evermod.client.gui.core.UINode;

/**
 * Interface representing a UI element capable of holding and managing child elements
 * within a hierarchical node tree.
 *
 * @author Wipodev
 */
public interface Container {

  /**
   * Adds a child element to this container.
   *
   * @param <T>   The specific type of the UI element being added.
   * @param child The element to add.
   * @return The added child instance to facilitate fluid chaining.
   */
  <T extends UINode> T addChild(T child);

  /**
   * Removes a specific child element from this container.
   *
   * @param child The child element to remove.
   * @return {@code true} if the element was present and removed, {@code false} otherwise.
   */
  boolean removeChild(UINode child);

  /**
   * Clears all child elements currently managed by this container.
   */
  void clearChildren();

  /**
   * Retrieves an unmodifiable view or copy of all direct children in this container.
   *
   * @return List of direct child elements.
   */
  List<UINode> getChildren();

  /**
   * Searches for a child element that matches the specified unique identifier.
   *
   * @param id The unique string identifier to search for.
   * @return An {@link Optional} containing the element if found, or empty if not present.
   */
  default Optional<UINode> findChildById(String id) {
    return Optional.empty();
  }
}
