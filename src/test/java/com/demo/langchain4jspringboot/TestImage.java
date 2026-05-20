package com.demo.langchain4jspringboot;

import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;

public class TestImage {

    @Test
    public void testwanx() {
        WanxImageModel model = WanxImageModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/api/v1")
                .apiKey("")
                .modelName("wan2.5-t2i-preview")
                .build();
        Response<Image> generate = model.generate("中国杭州");
        System.out.println(generate.content().url());

    }
}
