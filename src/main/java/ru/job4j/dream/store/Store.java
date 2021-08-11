package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.Collection;
import java.util.Optional;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void savePost(Post post);

    void saveCandidate(Candidate candidate);

    Optional<Post> findPostById(int id);

    Optional<Candidate> findCandidateById(int id);

    void deleteCandidateById(int parseInt);

    Collection<User> findAllUsers();

    void saveUser(User user);

    Optional<User> findUserByEmail(String email);

    void deleteUserById(int id);

    void changePassword(int id, String password);
}