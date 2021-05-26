package com.mprog.dao;

import com.mprog.entity.User;
import com.mprog.utill.TestDataSource;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

class UserDaoTest {

    private UserDao userDao;
    private Connection connection;
    private TestDataForFindAll dataForFindAll;

    @BeforeEach
    void prepare() {
        var dataSource = new TestDataSource();
        connection = dataSource.getConnection();
        fillDatabase(connection);
        userDao = UserDao.getInstance();
        userDao.setObject(connection);
        dataForFindAll = new TestDataForFindAll();
    }

    @SneakyThrows
    @AfterEach
    void shutDown()  {
        connection.close();
    }

    @SneakyThrows
    private void fillDatabase(Connection connection)  {

        try (var resourceAsStream = getClass().getClassLoader().getResourceAsStream("test_db.sql")) {
            Optional.ofNullable(resourceAsStream).ifPresentOrElse(
                    this::readBytesAndExecuteQuery,
                    () -> System.out.println("resource is empty")
            );
            
        }
    }

    @SneakyThrows
    private void readBytesAndExecuteQuery(InputStream inputStream){
        byte[] bytes = inputStream.readAllBytes();
        var sql = new String(bytes);
        for (String s : sql.split(";")) {
            connection.createStatement().execute(s);
        }
    }

    @SneakyThrows
    @Test
    void findByEmailAndPassword() {
        var result = userDao.findAll();

        Assertions.assertThat(result).containsExactlyInAnyOrderElementsOf(dataForFindAll.users);
    }
}