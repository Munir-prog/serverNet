package com.mprog.service;

import com.mprog.dao.MessageDao;
import com.mprog.dto.MessageDto;
import com.mprog.mapper.MessageMapper;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class MessageService {
    private static final MessageService INSTANCE = new MessageService();
    private final MessageMapper messageMapper = MessageMapper.getInstance();
    private final MessageDao messageDao = MessageDao.getInstance();

    public List<MessageDto> getHistory() {
        return messageDao.findAll().stream()
                .map(messageMapper::mapTo)
                .toList();
    }

    public void create(MessageDto messageDto) {
        var messageEntity = messageMapper.mapFrom(messageDto);
        messageDao.save(messageEntity);

//        return messageEntity.getId();
    }

    public static MessageService getInstance() {
        return INSTANCE;
    }
}
