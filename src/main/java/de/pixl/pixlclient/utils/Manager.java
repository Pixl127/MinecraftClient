package de.pixl.pixlclient.utils;

import de.pixl.pixlclient.command.AbstractCommand;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Manager<T, S> {

    private final ConcurrentHashMap<T, S> entries = new ConcurrentHashMap<>();

    public boolean register(T key, S value) {
        return entries.putIfAbsent(key, value) == null;
    }

    public boolean unregister(T key) {
        return entries.remove(key) != null;
    }

    public S get(T key) {
        return entries.get(key);
    }

    public boolean contains(T key) {
        return entries.containsKey(key);
    }

    public Collection<S> values() {
        return entries.values();
    }

    public Set<Map.Entry<T, S>> entries() {
        return entries.entrySet();
    }

    public Set<T> keys() {
        return entries.keySet();
    }
}
