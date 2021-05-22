package com.mprog.mapper;

import com.mprog.dto.UserDto;
import com.mprog.entity.User;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserMapper implements Mapper<UserDto, User>{

    public static final UserMapper INSTANCE = new UserMapper();

    @Override
    public User mapFrom(UserDto object) {
        return User.builder()
                .email(object.getEmail())
                .password(object.getPassword())
                .build();
    }

    @Override
    public UserDto mapTo(User object) {
        return UserDto.builder()
                .email(object.getEmail())
                .password(object.getPassword())
                .build();
    }

    public static UserMapper getInstance() {
        return INSTANCE;
    }
}
