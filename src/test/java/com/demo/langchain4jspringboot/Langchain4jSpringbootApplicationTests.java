package com.demo.langchain4jspringboot;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Langchain4jSpringbootApplicationTests {
    
    @Autowired
    private QwenChatModel qwenChatModel;

    @Test
    void testMemory(){
        UserMessage userMessage1 = UserMessage.from("你是谁");
        ChatResponse response1 = qwenChatModel.chat(userMessage1);
        AiMessage aiMessage1 = response1.aiMessage();
        System.out.println(aiMessage1.text());
        System.out.println("-----------------");

        UserMessage userMessage2 = UserMessage.from("我的上一个问题是什么");
        ChatResponse response2 = qwenChatModel.chat(userMessage1,aiMessage1,userMessage2);
        AiMessage aiMessage2 = response2.aiMessage();
        System.out.println(aiMessage2.text());
    }
}
