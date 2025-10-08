package com.xlei.zerocode.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xlei.zerocode.exception.ErrorCode;
import com.xlei.zerocode.exception.ThrowUtils;
import com.xlei.zerocode.model.entity.ChatHistory;
import com.xlei.zerocode.mapper.ChatHistoryMapper;
import com.xlei.zerocode.model.entity.User;
import com.xlei.zerocode.model.enums.ChatHistoryMsgTypeEnum;
import com.xlei.zerocode.service.ChatHistoryService;
import org.springframework.stereotype.Service;

/**
 * 对话历史 服务层实现。
 *
 * @author xlei
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory>  implements ChatHistoryService{

    @Override
    public Boolean saveChatMessage(Long appId, String message, String messageType, Long userId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用id错误");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "消息不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(messageType), ErrorCode.PARAMS_ERROR, "消息类型不能为空");
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.NOT_LOGIN_ERROR, "用户id错误");

        ChatHistoryMsgTypeEnum msgTypeEnum = ChatHistoryMsgTypeEnum.getEnumByValue(messageType);
        ThrowUtils.throwIf(msgTypeEnum == null, ErrorCode.PARAMS_ERROR, String.format("不支持 {} 消息类型",messageType));

        ChatHistory chatHistory = ChatHistory.builder()
                .appId(appId)
                .message(message)
                .messageType(messageType)
                .userId(userId)
                .build();
        return this.save(chatHistory);
    }
}
