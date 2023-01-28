package de.snowii.packeto.util;

import java.util.Objects;

public class Pair<X, Y> {
    private final X key;
    private Y value;

    public Pair(X key, Y value) {
        this.key = key;
        this.value = value;
    }

    public X key() {
        return key;
    }

    public Y value() {
        return value;
    }

    @Deprecated/*(forRemoval = true)*/
    public X getKey() {
        return key;
    }

    @Deprecated/*(forRemoval = true)*/
    public Y getValue() {
        return value;
    }

    /**
     * @deprecated don't count on this continuing to be mutable
     */
    @Deprecated/*(forRemoval = true)*/
    public void setValue(Y value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Pair{" + key + ", " + value + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        if (!Objects.equals(key, pair.key)) return false;
        return Objects.equals(value, pair.value);
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
