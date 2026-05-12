package com.demo.langchain4jspringboot.config;

import com.demo.langchain4jspringboot.service.ToolsService;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    public interface Assistant {
        String chat(String message);

        TokenStream chatStream(String message);
    }

    @Bean
    public Assistant assistant(ChatModel qwenChatModel,
                               StreamingChatModel qwenStreamingChatModel) {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(qwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .chatMemory(chatMemory)
                .build();
        return assistant;
    }

    public interface AssistantUnique {
        String chat(@MemoryId int memoryId, @UserMessage String message);

        TokenStream chatStream(@MemoryId int memoryId, @UserMessage String message);
    }

    @Bean
    public AssistantUnique assistantUnique(ChatModel qwenChatModel,
                                           StreamingChatModel qwenStreamingChatModel) {

        ChatMemoryProvider chatMemoryProvider = memoryId ->
                MessageWindowChatMemory
                        .builder()
                        .maxMessages(10)
                        .id(memoryId)
                        .build();
        AssistantUnique assistantUnique = AiServices.builder(AssistantUnique.class)
                .chatModel(qwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
        return assistantUnique;
    }

    public interface AssistantUniqueDb {
        String chat(@MemoryId int memoryId, @UserMessage String message);

        TokenStream chatStream(@MemoryId int memoryId, @UserMessage String message);
    }

    @Bean
    public AssistantUniqueDb assistantUniqueDb(ChatModel qwenChatModel,
                                               StreamingChatModel qwenStreamingChatModel,
                                               PersistentChatMemoryStore persistentChatMemoryStore) {

        ChatMemoryProvider chatMemoryProvider = memoryId ->
                MessageWindowChatMemory
                        .builder()
                        .maxMessages(10)
                        .id(memoryId)
                        .chatMemoryStore(persistentChatMemoryStore)
                        .build();
        AssistantUniqueDb assistantUnique = AiServices.builder(AssistantUniqueDb.class)
                .chatModel(qwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
        return assistantUnique;
    }

    public interface AssistantTools {
        String chat(String message);

        TokenStream chatStream(String message);
    }

    @Bean
    public AssistantTools assistantTools(ChatModel qwenChatModel,
                                         StreamingChatModel qwenStreamingChatModel,
                                         ToolsService toolsService) {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        AssistantTools assistantTools = AiServices.builder(AssistantTools.class)
                .chatModel(qwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .chatMemory(chatMemory)
                .tools(toolsService)
                .build();
        return assistantTools;
    }

    public interface AssistantSys {
        String chat(String message);

        TokenStream chatStream(String message);

        @SystemMessage("你是一个乘务员，若用户说要退票，则你需要询问得到车次和姓名信息，今天的日期是{{date}}")
        TokenStream chatStream(@UserMessage String message, @V("date") String date);
    }

    @Bean
    public AssistantSys assistantSys(ChatModel qwenChatModel,
                                     StreamingChatModel qwenStreamingChatModel,
                                     ToolsService toolsService) {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        AssistantSys assistantSys = AiServices.builder(AssistantSys.class)
                .chatModel(qwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .chatMemory(chatMemory)
                .tools(toolsService)
                .build();
        return assistantSys;
    }
}
