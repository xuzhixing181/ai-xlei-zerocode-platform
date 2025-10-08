package com.xlei.zerocode.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * @author https://github.com/xuzhixing181
 */
@Getter
public enum ChatHistoryMsgTypeEnum {

    USER("用户", "user"),
    AI("AI", "ai");

    private final String text;

    private final String value;

    ChatHistoryMsgTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static ChatHistoryMsgTypeEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (ChatHistoryMsgTypeEnum anEnum : ChatHistoryMsgTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
