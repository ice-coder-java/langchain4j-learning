package com.demo.langchain4jspringboot;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestELP {

    @Test
    public void testEmbedding() {
        Document document = ClassPathDocumentLoader.loadDocument("rag/pai.txt", new TextDocumentParser());

        DocumentBySentenceSplitter splitter = new DocumentBySentenceSplitter(
                100, // 每段长度
                10 // 每段重叠长度
        );

        List<TextSegment> segments = splitter.split(document);

        // 创建嵌入模型
        QwenEmbeddingModel embeddingModel = QwenEmbeddingModel.builder()
                .apiKey("sk-0648ff1e3274451b96eb2a2bd75dadca")
                .build();

        List<Embedding> content = embeddingModel.embedAll(segments).content();

        // 创建嵌入存储
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(content, segments);

        /*// 创建查询嵌入
        Embedding query = embeddingModel.embed("什么是医路通").content();
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(query)
                .build();
        EmbeddingSearchResult<TextSegment> search = embeddingStore.search(embeddingSearchRequest);
        search.matches().forEach(match -> {
            System.out.println(match.score());
            System.out.println(match.embedded().text());
        });*/

        ChatModel model = QwenChatModel.builder()
                .apiKey("sk-0648ff1e3274451b96eb2a2bd75dadca")
                .modelName("qwen3.6-max-preview")
                .build();

        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(5)
                .minScore(0.5)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .contentRetriever(contentRetriever)
                .build();
        System.out.println(assistant.chat("什么是医路通"));
    }

    public interface Assistant {
        String chat(String message);

        TokenStream chatStream(String message);
    }

}
