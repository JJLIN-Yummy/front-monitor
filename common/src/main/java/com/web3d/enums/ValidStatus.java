package com.web3d.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "是否有效枚举")
public enum ValidStatus {

    INVALID(0, "无效"),
    VALID(1, "有效");

    private final Integer code;
    private final String desc;

    ValidStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonValue
    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    // 根据code反向匹配
    public static ValidStatus getByCode(Integer code) {
        for (ValidStatus value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
