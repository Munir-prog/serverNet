package com.mprog.mapper;

import com.mprog.dto.MessageDto;
import com.mprog.entity.Message;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class MessageMapper implements Mapper<MessageDto, Message>{

    public static final MessageMapper INSTANCE = new MessageMapper();

    @Override
    public Message mapFrom(MessageDto object) {
        return Message.builder()
                .message(object.getMessage())
                .build();
    }

    @Override
    public MessageDto mapTo(Message object) {
        return MessageDto.builder()
                .message(object.getMessage())
                .build();
    }

    public static MessageMapper getInstance() {
        return INSTANCE;
    }
}
