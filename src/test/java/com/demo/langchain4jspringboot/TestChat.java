package com.demo.langchain4jspringboot;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.junit.jupiter.api.Test;

public class TestChat {

    @Test
    public void testChat() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();
        String chat = model.chat("你好，你是谁？");
        System.out.println(chat);
    }

    @Test
    public void testdeepseek() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey("sk-8298b5a11ee84b5ca2202908371a8415")
                .modelName("deepseek-v4-flash")
                .build();
        String chat = model.chat("你好，你是谁？");
        System.out.println(chat);
    }

    @Test
    public void testqwen() {
        ChatModel model = QwenChatModel.builder()
//                .baseUrl("https://dashscope.aliyuncs.com/api/v1")
                .apiKey("sk-0648ff1e3274451b96eb2a2bd75dadca")
                .modelName("qwen3.6-max-preview")
                .build();
        String chat = model.chat("你好，你是谁？");
        System.out.println(chat);
    }

    @Test
    public void testollama() {
        ChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("deepseek-r1:8b")
                .build();
        String chat = model.chat("你好，你是谁？");
        System.out.println(chat);
    }
}
