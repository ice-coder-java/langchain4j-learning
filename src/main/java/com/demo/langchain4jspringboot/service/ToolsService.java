package com.demo.langchain4jspringboot.service;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

@Service
public class ToolsService {

    @Tool("获取中国有多少个名字为name的人")
    public Integer nameCount(@P("name") String name) {
        System.out.println("name: " + name);
        return 10;
    }

    @Tool("根据车次和姓名信息退票")
    public String cancelTicket(@P("name") String name, @P("train_no") String trainNo) {
        System.out.println("name: " + name);
        System.out.println("train_no: " + trainNo);
        if (name.equals("张三") && trainNo.equals("1234")) {
            return "退票成功";
        } else {
            return "退票失败";
        }
    }

}
