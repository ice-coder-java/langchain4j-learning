package com.demo.langchain4jspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("chat_message")
public class ChatMessageRowEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String memoryId;

    private Integer msgIndex;

    private String messageJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(String memoryId) {
        this.memoryId = memoryId;
    }

    public Integer getMsgIndex() {
        return msgIndex;
    }

    public void setMsgIndex(Integer msgIndex) {
        this.msgIndex = msgIndex;
    }

    public String getMessageJson() {
        return messageJson;
    }

    public void setMessageJson(String messageJson) {
        this.messageJson = messageJson;
    }
}
