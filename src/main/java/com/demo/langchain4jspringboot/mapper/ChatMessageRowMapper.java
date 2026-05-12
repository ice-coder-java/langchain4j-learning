package com.demo.langchain4jspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.langchain4jspringboot.entity.ChatMessageRowEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMessageRowMapper extends BaseMapper<ChatMessageRowEntity> {
}
