package com.demo.langchain4jspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.langchain4jspringboot.entity.ChatSessionEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSessionEntity> {
}
