package com.xlei.zerocode.service;

import com.mybatisflex.core.service.IService;
import com.xlei.zerocode.model.entity.ChatHistory;
import com.xlei.zerocode.model.entity.User;

/**
 * 对话历史 服务层。
 *
 * @author xlei
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 保存对话历史消息
     * @param chatId: 应用id
     * @param message: 对话消息
     * @param messageType: 消息类型 (user或 AI)
     * @param userId: 用户id
     * @return
     */
    Boolean saveChatMessage(Long chatId, String message, String messageType, Long userId);

}
