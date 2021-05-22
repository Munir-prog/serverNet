package com.mprog.dao;

import com.mprog.entity.Message;
import com.mprog.utill.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
public class MessageDao implements Dao<Integer, Message> {

    private static final MessageDao INSTANCE = new MessageDao();
    private static final String SAVE_MESSAGE_SQL = """
            INSERT INTO messages (message)
            VALUES (?)
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id, message
            FROM messages
            ORDER BY id DESC
            LIMIT 20
            """;

    @Override
    public List<Message> findAll() {
        try (var connection = ConnectionManager.get();
            var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)){

            var resultSet = preparedStatement.executeQuery();
            List<Message> messages = new ArrayList<>();
            while (resultSet.next()){
                messages.add(buildMessage(resultSet));
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @SneakyThrows
    private Message buildMessage(ResultSet resultSet) {
        return Message.builder()
                .id(resultSet.getObject("id", Integer.class))
                .message(resultSet.getObject("message", String.class))
                .build();
    }

    @Override
    public Optional<Message> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public void update(Message entity) {

    }

    @Override
    public Message save(Message entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_MESSAGE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, entity.getMessage());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            entity.setId(generatedKeys.getObject("id", Integer.class));

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static MessageDao getInstance() {
        return INSTANCE;
    }
}
