package com.demo.langchain4jspringboot.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.langchain4jspringboot.entity.ChatMessageRowEntity;
import com.demo.langchain4jspringboot.entity.ChatSessionEntity;
import com.demo.langchain4jspringboot.mapper.ChatMessageRowMapper;
import com.demo.langchain4jspringboot.mapper.ChatSessionMapper;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class PersistentChatMemoryStore implements ChatMemoryStore {

    private final ChatSessionMapper chatSessionMapper;
    private final ChatMessageRowMapper chatMessageRowMapper;

    public PersistentChatMemoryStore(ChatSessionMapper chatSessionMapper,
                                     ChatMessageRowMapper chatMessageRowMapper) {
        this.chatSessionMapper = chatSessionMapper;
        this.chatMessageRowMapper = chatMessageRowMapper;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String id = String.valueOf(memoryId);
        if (chatSessionMapper.selectById(id) == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ChatMessageRowEntity> q = new LambdaQueryWrapper<>();
        q.eq(ChatMessageRowEntity::getMemoryId, id).orderByAsc(ChatMessageRowEntity::getMsgIndex);
        List<ChatMessageRowEntity> rows = chatMessageRowMapper.selectList(q);
        if (rows.isEmpty()) {
            return Collections.emptyList();
        }
        List<ChatMessage> out = new ArrayList<>(rows.size());
        for (ChatMessageRowEntity row : rows) {
            out.add(ChatMessageDeserializer.messageFromJson(row.getMessageJson()));
        }
        return out;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String id = String.valueOf(memoryId);
        List<ChatMessage> list = messages == null ? List.of() : messages;

        chatMessageRowMapper.delete(
                new LambdaQueryWrapper<ChatMessageRowEntity>().eq(ChatMessageRowEntity::getMemoryId, id));

        if (list.isEmpty()) {
            chatSessionMapper.deleteById(id);
            return;
        }

        if (chatSessionMapper.selectById(id) == null) {
            ChatSessionEntity session = new ChatSessionEntity();
            session.setMemoryId(id);
            chatSessionMapper.insert(session);
        }

        int index = 0;
        for (ChatMessage message : list) {
            ChatMessageRowEntity row = new ChatMessageRowEntity();
            row.setMemoryId(id);
            row.setMsgIndex(index++);
            row.setMessageJson(ChatMessageSerializer.messageToJson(message));
            chatMessageRowMapper.insert(row);
        }
    }

    @Override
    public void deleteMessages(Object memoryId) {
        String id = String.valueOf(memoryId);
        chatSessionMapper.deleteById(id);
    }
}
