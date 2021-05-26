package com.mprog.dao;

import com.mprog.entity.User;
import com.mprog.utill.TestDataSource;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    void whenFindAll_thenSuccess() {
        var result = userDao.findAll();

        Assertions.assertThat(result).containsExactlyInAnyOrderElementsOf(dataForFindAll.users);
    }

    @Test
    void whenFindByEmailAndPassword_thenSuccess(){
        String email = "test2@mail.ru";
        String password = "test2test2";
        var user = Optional.of(User.builder().id(2).email(email).password(password).build());
        System.out.println(user);

        var result = userDao.findByEmailAndPassword(email, password);
        Assertions.assertThat(result).isEqualTo(user);
    }

    @Test
    void givenEntity_whenSave_thenReturnEntitySaved(){
        var user = User.builder().email("test5@mail.ru").password("test5test5").build();

        var result = userDao.save(user);

        Assertions.assertThat(result.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(result.getPassword()).isEqualTo(user.getPassword());
    }
}