package com.tcb.auto.utils;

import org.apache.commons.collections4.map.AbstractHashedMap;
import org.apache.commons.collections4.map.LinkedMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashSet;
import java.util.Map;

public class LinkedCaseInsensitiveMap<K, V> extends LinkedMap<K, V> {
    public LinkedCaseInsensitiveMap() {
        super();
    }

    public LinkedCaseInsensitiveMap(int initialCapacity) {
        super(initialCapacity);
    }

    public LinkedCaseInsensitiveMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public LinkedCaseInsensitiveMap(Map<? extends K, ? extends V> map) {
        super(map);
    }

    //-----------------------------------------------------------------------
    /**
     * Overrides convertKey() from {@link AbstractHashedMap} to convert keys to
     * lower case.
     * <p>
     * Returns {@link AbstractHashedMap#NULL} if key is null.
     *
     * @param key  the key convert
     * @return the converted key
     */
    @Override
    protected Object convertKey(final Object key) {
        if (key != null) {
            final char[] chars = key.toString().toCharArray();
            for (int i = chars.length - 1; i >= 0; i--) {
                chars[i] = Character.toLowerCase(Character.toUpperCase(chars[i]));
            }
            return new String(chars);
        }
        return AbstractHashedMap.NULL;
    }

    //-----------------------------------------------------------------------
    /**
     * Clones the map without cloning the keys or values.
     *
     * @return a shallow clone
     */
    @Override
    public LinkedCaseInsensitiveMap<K, V> clone() {
        return (LinkedCaseInsensitiveMap<K, V>) super.clone();
    }

    /**
     * Write the map out using a custom routine.
     *
     * @param out  the output stream
     * @throws IOException if an error occurs while writing to the stream
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        doWriteObject(out);
    }

    /**
     * Read the map in using a custom routine.
     *
     * @param in the input stream
     * @throws IOException if an error occurs while reading from the stream
     * @throws ClassNotFoundException if an object read from the stream can not be loaded
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        doReadObject(in);
    }
}
