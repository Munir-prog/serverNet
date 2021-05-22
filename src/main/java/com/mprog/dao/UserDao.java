package com.mprog.dao;

import com.mprog.entity.User;
import com.mprog.utill.ConnectionManager;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;


@NoArgsConstructor(access = PRIVATE)
public class UserDao implements Dao<Integer, User> {

    public static final UserDao INSTANCE = new UserDao();
    public static final String SAVE_ENTITY_SQL = """
            INSERT INTO client (email, password)
            VALUES (?, ?)       
            """;
    private static final String FIND_BY_EMAIL_AND_PASSWORD = """
            SELECT id, email, password
            FROM client
            WHERE email = ?
            AND password = ?
            """;

    @SneakyThrows
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_AND_PASSWORD)) {

            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, password);

            var resultSet = preparedStatement.executeQuery();

            User user = null;
            if(resultSet.next()){
                user = buildEntity(resultSet);
            }

            return Optional.ofNullable(user);
        }
    }

    @SneakyThrows
    private User buildEntity(ResultSet resultSet) {
        return User.builder()
                .id(resultSet.getObject("id", Integer.class))
                .email(resultSet.getObject("email", String.class))
                .password(resultSet.getObject("password", String.class))
                .build();
    }

    @Override
    public User save(User entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_ENTITY_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, entity.getEmail());
            preparedStatement.setObject(2, entity.getPassword());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            entity.setId(generatedKeys.getObject("id", Integer.class));

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public void update(User entity) {

    }


    public static UserDao getInstance() {
        return INSTANCE;
    }
}
