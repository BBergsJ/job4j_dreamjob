package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final Store INST = new Store();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "New", "19.07.21"));
        posts.put(2, new Post(2, "Middle Java Job", "New", "18.07.21"));
        posts.put(3, new Post(3, "Senior Java Job", "New", "17.07.21"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}