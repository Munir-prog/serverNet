package com.mprog.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private Integer id;
    private String message;
}
