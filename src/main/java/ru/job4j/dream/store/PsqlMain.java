package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;

import java.util.Optional;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.savePost(new Post(0, "Java Job"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }

        System.out.println(System.lineSeparator());
        store.savePost(new Post(1, "New Java Job"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }

        System.out.println(System.lineSeparator());
        Optional<Post> testPost = store.findPostById(50);
        if (testPost.isPresent()) {
            System.out.println(testPost.get().getId() + " " + testPost.get().getName());
        } else {
            System.out.println("Post is not found");
        }
    }
}