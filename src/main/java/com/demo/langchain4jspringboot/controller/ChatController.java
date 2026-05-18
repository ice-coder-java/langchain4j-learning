package com.demo.langchain4jspringboot.controller;

import com.demo.langchain4jspringboot.config.AiConfig;
import com.demo.langchain4jspringboot.service.ToolsService;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenChatRequestParameters;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.service.TokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Autowired
    private QwenChatModel qwenChatModel;

    @Autowired
    private QwenStreamingChatModel qwenStreamingChatModel;

    @RequestMapping("/chat")
    public String chat(@RequestParam(defaultValue = "你是谁") String question) {
        return qwenChatModel.chat(question);
    }

    @RequestMapping(value = "/streamingChat", produces = "text/stream;charset=UTF-8")
    public Flux<String> streamingChat(@RequestParam(defaultValue = "你是谁") String question) {
        return Flux.create(sink -> {
            qwenStreamingChatModel.chat(question, new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    sink.next(partialResponse);
                }

                @Override
                public void onCompleteResponse(ChatResponse chatResponse) {
                    sink.complete();
                }

                @Override
                public void onError(Throwable throwable) {
                    sink.error(throwable);
                }
            });
        });
    }

    @Autowired
    private AiConfig.Assistant assistant;

    @RequestMapping("/memory_chat")
    public String memoryChat(@RequestParam(defaultValue = "你是谁") String question) {
        return assistant.chat(question);
    }

    @RequestMapping(value = "/memory_streaming_chat", produces = "text/stream;charset=UTF-8")
    public Flux<String> memoryStreamingChat(@RequestParam(defaultValue = "你是谁") String question) {
        TokenStream stream = assistant.chatStream(question);
        return Flux.create(sink -> {
            stream.onPartialResponse(sink::next)
                    .onCompleteResponse(chatResponse -> sink.complete())
                    .onError(sink::error)
                    .start();
        });
    }

    @Autowired
    private AiConfig.AssistantUnique assistantUnique;

    @RequestMapping("/memory_unique_chat")
    public String memoryUniqueChat(@RequestParam(defaultValue = "你是谁") String question, Integer userId) {
        return assistantUnique.chat(userId, question);
    }

    @RequestMapping(value = "/memory_unique_streaming_chat", produces = "text/stream;charset=UTF-8")
    public Flux<String> memoryUniqueStreamingChat(@RequestParam(defaultValue = "你是谁") String question, Integer userId) {
        TokenStream stream = assistantUnique.chatStream(userId, question);
        return Flux.create(sink -> {
            stream.onPartialResponse(sink::next)
                    .onCompleteResponse(chatResponse -> sink.complete())
                    .onError(sink::error)
                    .start();
        });
    }

    @Autowired
    private AiConfig.AssistantUniqueDb assistantUniqueDb;

    /** 使用 MySQL 持久化记忆的会话（memoryId 相同则共享历史） */
    @RequestMapping("/memory_db_chat")
    public String memoryDbChat(@RequestParam(defaultValue = "你是谁") String question,
                               @RequestParam Integer memoryId) {
        return assistantUniqueDb.chat(memoryId, question);
    }

    @RequestMapping(value = "/memory_db_streaming_chat", produces = "text/stream;charset=UTF-8")
    public Flux<String> memoryDbStreamingChat(@RequestParam(defaultValue = "你是谁") String question,
                                              @RequestParam Integer memoryId) {
        TokenStream stream = assistantUniqueDb.chatStream(memoryId, question);
        return Flux.create(sink -> {
            stream.onPartialResponse(sink::next)
                    .onCompleteResponse(chatResponse -> sink.complete())
                    .onError(sink::error)
                    .start();
        });
    }

    @Autowired
    private AiConfig.AssistantTools assistantTools;

    @RequestMapping("/memory_tools_chat")
    public String memoryToolsChat(@RequestParam(defaultValue = "你是谁") String question) {
        return assistantTools.chat(question);
    }

    @RequestMapping(value = "/memory_tools_streaming_chat", produces = "text/stream;charset=UTF-8")
    public Flux<String> memoryToolsStreamingChat(@RequestParam(defaultValue = "你是谁") String question) {
        TokenStream stream = assistantTools.chatStream(question);
        return Flux.create(sink -> {
            stream.onPartialResponse(sink::next)
                    .onCompleteResponse(chatResponse -> sink.complete())
                    .onError(sink::error)
                    .start();
        });
    }

    @Autowired
    private AiConfig.AssistantSys assistantSys;

    @RequestMapping(value = "/memory_sys_streaming_chat", produces = "text/stream;charset=UTF-8")
    public Flux<String> memorySysStreamingChat(@RequestParam(defaultValue = "你是谁") String question) {
        TokenStream stream = assistantSys.chatStream(question, LocalDate.now().toString());
        return Flux.create(sink -> {
            stream.onPartialResponse(sink::next)
                    .onCompleteResponse(chatResponse -> sink.complete())
                    .onError(sink::error)
                    .start();
        });
    }

    @Autowired
    private AiConfig.AssistantEmbedding assistantEmbedding;

    @RequestMapping(value = "/memory_embedding_streaming_chat", produces = "text/stream;charset=UTF-8")
    public Flux<String> memoryEmbeddingStreamingChat(@RequestParam(defaultValue = "你是谁") String question) {
        TokenStream stream = assistantEmbedding.chatStream(question);
        return Flux.create(sink -> {
            stream.onPartialResponse(sink::next)
                    .onCompleteResponse(chatResponse -> sink.complete())
                    .onError(sink::error)
                    .start();
        });
    }

}
