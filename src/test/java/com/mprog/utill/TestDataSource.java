package com.mprog.utill;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDataSource {


    @SneakyThrows
    public Connection getConnection() {
        return DriverManager.getConnection("jdbc:h2:mem:test");
    }
}
