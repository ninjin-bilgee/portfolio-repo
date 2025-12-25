package edu.cwru.csds293;

import java.util.Set;

/**
 * 
 * Functional interface used as a callback to set a {@link Set} of elements.
 *
 * <p>
 * Typically used with {@link AdjacencySetStatus#setStatus} to update
 * the internal sets of vertices or vertex pairs associated with specific graph errors.
 * </p>
 *
 * @param <T> the type of elements in the set
 */
@FunctionalInterface
interface Setter<T>{
    void set(Set<T> value);
}