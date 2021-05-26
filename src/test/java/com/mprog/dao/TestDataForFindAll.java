package com.mprog.dao;

import com.mprog.entity.User;

import java.util.List;

public class TestDataForFindAll {
    /*
    ('test1@mail.ru', 'test1test1'),
       ('test2@mail.ru', 'test2test2'),
       ('test3@mail.ru', 'test3test3'),
       ('test4@mail.ru', 'test4test4');
     */
    User user1 = User.builder()
            .id(1)
            .email("test1@mail.ru")
            .password("test1test1")
            .build();
    User user2 = User.builder()
            .id(2)
            .email("test2@mail.ru")
            .password("test2test2")
            .build();

    List<User> users = List.of(user1, user2);

}
