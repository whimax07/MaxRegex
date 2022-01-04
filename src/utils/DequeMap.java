package utils;

import java.util.ArrayDeque;
import java.util.HashMap;

@SuppressWarnings("UnusedReturnValue")
public class DequeMap<K, V> {

    private HashMap<K, V> map = new HashMap<>();

    private ArrayDeque<K> deque = new ArrayDeque<>();



    public V get(K key) {
        return map.get(key);
    }

    public V getFirst() {
        return map.get(deque.peekFirst());
    }

    public V putFirst(K key, V value) {
        if (map.get(key) == null) {
            deque.addFirst(key);
        }
        return map.put(key, value);
    }

    public Entry<K, V> removeFirst() {
        K key = deque.removeFirst();
        Entry<K, V> entry = new Entry<>(key, map.get(key));
        map.remove(key);
        return entry;
    }

    public boolean isEmpty() {
        return deque.isEmpty();
    }



    public record Entry<K, V>(K key, V value) {}

}
