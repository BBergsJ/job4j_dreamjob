package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();
    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("SELECT candidate.id, candidate.name, candidate.cityid,"
                + " c.name AS city FROM candidate LEFT JOIN city c ON candidate.cityid = c.id")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"),
                            it.getString("name"),
                            new City(it.getInt("cityId"), it.getString("city"))
                    ));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        return candidates;
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO post(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        return post;
    }

    private Candidate create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO candidate(name, cityId) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCity().getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        return candidate;
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("UPDATE post SET name = ? WHERE id = ?")) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE candidate SET name = ?, cityid = ? WHERE id = ?")) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCity().getId());
            ps.setInt(3, candidate.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Post> findPostById(int id) {
        Optional<Post> post = Optional.empty();
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("SELECT name FROM post WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    post = Optional.of(new Post(id, it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        return post;
    }

    @Override
    public Optional<Candidate> findCandidateById(int id) {
        Optional<Candidate> candidate = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT candidate.id, candidate.name, candidate.cityid, "
                     + "c.name AS city FROM candidate LEFT JOIN city c ON candidate.cityid = c.id "
                    + "WHERE candidate.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    candidate = Optional.of(new Candidate(it.getInt("id"),
                            it.getString("name"),
                            new City(it.getInt("cityId"), it.getString("city"))
                    ));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        return candidate;
    }


    public void deleteCandidateById(int id) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("DELETE FROM candidate WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public Collection<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("SELECT * from reguser")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    users.add(new User(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        return users;
    }

    @Override
    public void saveUser(User user) {
        if (user.getId() == 0) {
            create(user);
        } else {
            update(user);
        }
    }

    private User create(User user) {
        try (Connection cn = pool.getConnection();
        PreparedStatement preparedStatement = cn.prepareStatement(
                "INSERT INTO reguser(name, email, password) VALUES (?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
            try (ResultSet id = preparedStatement.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        return user;
    }

    private void update(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(
                     "UPDATE reguser SET name = ?, email = ?, password = ? WHERE id = ?")) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        Optional<User> user = Optional.empty();
        try (Connection cn = pool.getConnection();
        PreparedStatement preparedStatement = cn.prepareStatement("SELECT * FROM reguser WHERE email = ?")) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = Optional.of(new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"))
                    );
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        return user;
    }

    @Override
    public void deleteUserById(int id) {
        try (Connection cn = pool.getConnection();
        PreparedStatement preparedStatement = cn.prepareStatement("DELETE FROM reguser WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public void changePassword(int id, String password) {
        try (Connection cn = pool.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement("UPDATE reguser SET password = ? WHERE id = ?")) {
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public Collection<City> findAllCities() {
        List<City> cities = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * from city")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    cities.add(new City(resultSet.getInt("id"),
                            resultSet.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        return cities;
    }

    @Override
    public Optional<City> findCityById(int id) {
        Optional<City> city = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM city WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    city = Optional.of(new City(id, it.getString("name")
                    ));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        return city;
    }

    @Override
    public Collection<Post> findPostsByDay() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE "
                     + "dateCreated BETWEEN now() - INTERVAL '1 day' AND now()")
        ) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(new Post(resultSet.getInt("id"),
                            resultSet.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findCandidatesByDay() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT candidate.id, candidate.name, candidate.cityid, c.name AS city "
                             + "FROM candidate LEFT JOIN city c ON candidate.cityid = c.id "
                             + "WHERE dateCreated BETWEEN now() - INTERVAL '1 day' AND now()")
        ) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    candidates.add(new Candidate(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            new City(resultSet.getInt("cityId"), resultSet.getString("city"))
                    ));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred: " + e.getMessage(), e);
        }
        return candidates;
    }
}