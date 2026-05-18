package com.demo.langchain4jspringboot;

import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@MapperScan("com.demo.langchain4jspringboot.mapper")
public class Langchain4jSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(Langchain4jSpringbootApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(QwenEmbeddingModel qwenEmbeddingModel, EmbeddingStore embeddingStore) {
        return args -> {
            Document document = ClassPathDocumentLoader.loadDocument("rag/pai.txt", new TextDocumentParser());

            DocumentBySentenceSplitter splitter = new DocumentBySentenceSplitter(
                    100, // 每段长度
                    10 // 每段重叠长度
            );

            List<TextSegment> segments = splitter.split(document);

            List<Embedding> content = qwenEmbeddingModel.embedAll(segments).content();

            // 创建嵌入存储
            embeddingStore.addAll(content, segments);
        };
    }

}
