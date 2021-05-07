package com.mprog.serialization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.net.SocketAddress;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@ToString
public class Message implements Serializable {
    public static final long serialVersionUID = 42L;

//    private final LocalDateTime timestamp = LocalDateTime.now();
//    private final SocketAddress target = Settings.ADDRESS;
    private final String message;
}
