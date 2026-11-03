package com.web3d.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "端类型枚举") // 🎯 给整个枚举加描述
public enum SdkTypeEnum {

    WEB("WEB", "PC/移动端 Web 浏览器"),
    MINI_PROGRAM("MINI_PROGRAM", "微信/支付宝/抖音小程序"),
    ANDROID("ANDROID", "Android 原生或跨平台 App"),
    IOS("IOS", "iOS 原生或跨平台 App"),
    ELECTRON("ELECTRON", "桌面客户端 (Electron)"),
    SERVER("SERVER", "Node.js 服务端"),
    UNKNOWN("UNKNOWN", "未知终端");

    private final String code;
    private final String desc;

    SdkTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 安全转换：根据 code 获取枚举对象，防止前端乱传报错
     */
    public static SdkTypeEnum getByCode(String code) {
        if (code == null) return UNKNOWN;
        for (SdkTypeEnum type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
