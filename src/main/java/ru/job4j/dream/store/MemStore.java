package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("CheckStyle")
public class MemStore implements Store {

    private static final MemStore INST = new MemStore();
    private Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private Map<Integer, User> users = new ConcurrentHashMap<>();
    private static AtomicInteger postID = new AtomicInteger();
    private static AtomicInteger candidateID = new AtomicInteger();
    private static AtomicInteger userID = new AtomicInteger();

    private MemStore() {

    }

    public static MemStore instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            post.setId(postID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    @Override
    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(candidateID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    @Override
    public Optional<Post> findPostById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Candidate> findCandidateById(int id) {
        return Optional.empty();
    }

    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(postID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public Post findPostByIdd(int id) {
        return posts.get(id);
    }

    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(candidateID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    public Candidate findCandidateByIdd(int id) {
        return candidates.get(id);
    }

    public void deleteCandidateById(int id) {
        candidates.keySet().removeIf(k -> k.equals(id));
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public void saveUser(User user) {
        if (user.getId() == 0) {
            user.setId(userID.incrementAndGet());
        }
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void deleteUserById(int id) {

    }

    @Override
    public void changePassword(int id, String password) {

    }

    @Override
    public Collection<City> findAllCities() {
        return null;
    }

    @Override
    public Optional<City> findCityById(int id) {
        return Optional.empty();
    }

    @Override
    public Collection<Candidate> findCandidatesByDay() {
        return null;
    }

    @Override
    public Collection<Post> findPostsByDay() {
        return null;
    }
}