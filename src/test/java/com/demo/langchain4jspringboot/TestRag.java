package com.demo.langchain4jspringboot;

import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;

public class TestRag {

    @Test
    public void testEmbedding() {
        QwenEmbeddingModel embeddingModel = QwenEmbeddingModel.builder()
                .apiKey("sk-0648ff1e3274451b96eb2a2bd75dadca")
                .build();

        Response<Embedding> embed = embeddingModel.embed("你好，你是谁？");
        System.out.println(embed.content());
    }
    @Test
    public void testInmemory() {
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // 创建嵌入模型
        QwenEmbeddingModel embeddingModel = QwenEmbeddingModel.builder()
                .apiKey("sk-0648ff1e3274451b96eb2a2bd75dadca")
                .build();

        // 创建文本片段
        TextSegment textSegment1 = TextSegment.from("我叫汪朱冰");
        Embedding embed = embeddingModel.embed(textSegment1).content();
        embeddingStore.add(embed,textSegment1);

        TextSegment textSegment2 = TextSegment.from("你叫张毓晨");
        Embedding embed2 = embeddingModel.embed(textSegment2).content();
        embeddingStore.add(embed2,textSegment2);

        // 创建查询嵌入
        Embedding query = embeddingModel.embed("你是谁").content();
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(query)
//                .maxResults(1)
                .minScore(0.5)
                .build();

        EmbeddingSearchResult<TextSegment> search = embeddingStore.search(embeddingSearchRequest);
        search.matches().forEach(match -> {
            System.out.println(match.score());
            System.out.println(match.embedded().text());
        });
    }
}
