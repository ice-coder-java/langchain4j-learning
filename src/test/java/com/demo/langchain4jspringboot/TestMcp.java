package com.demo.langchain4jspringboot;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestMcp {
    @Test
    public void testMcp() {
        ChatModel model = QwenChatModel.builder()
                .apiKey("")
                .modelName("qwen3.6-max-preview")
                .build();

        /*
        {
      "command": "cmd",
      "args": [
        "/c",
        "npx",
        "-y",
        "@baidumap/mcp-server-baidu-map"
      ],
      "env": {
        "BAIDU_MAP_API_KEY": "xxx"
      }
    }
         */
        McpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of("cmd",
                        "/c",
                        "npx",
                        "-y",
                        "@baidumap/mcp-server-baidu-map"))
                .environment(Map.of("BAIDU_MAP_API_KEY", "xxx"))
                .logEvents(true) // 是否在日志中打印流量
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        McpToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .toolProvider(toolProvider)
                .build();

        System.out.println(assistant.chat("怎么从杭州到温州"));
    }

    public interface Assistant {
        String chat(String message);

        TokenStream chatStream(String message);
    }
}
