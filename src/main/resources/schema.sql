-- 会话主表：一条记录对应一个 memoryId
CREATE TABLE IF NOT EXISTS `chat_session` (
  `memory_id` VARCHAR(64) NOT NULL COMMENT '会话标识，对应 @MemoryId',
  PRIMARY KEY (`memory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='LangChain4j 会话';

-- 消息从表：一对多，按 msg_index 排序还原顺序
CREATE TABLE IF NOT EXISTS `chat_message` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memory_id`    VARCHAR(64)  NOT NULL COMMENT '所属会话',
  `msg_index`    INT          NOT NULL COMMENT '会话内顺序，从 0 递增',
  `message_json` TEXT         NOT NULL COMMENT '单条 ChatMessage 的 JSON',
  PRIMARY KEY (`id`),
  KEY `idx_message_memory` (`memory_id`),
  CONSTRAINT `fk_message_session` FOREIGN KEY (`memory_id`) REFERENCES `chat_session` (`memory_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='LangChain4j 会话消息';
