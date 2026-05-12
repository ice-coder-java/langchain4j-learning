package com.demo.langchain4jspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("chat_session")
public class ChatSessionEntity {

    @TableId("memory_id")
    private String memoryId;

    public String getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(String memoryId) {
        this.memoryId = memoryId;
    }
}
