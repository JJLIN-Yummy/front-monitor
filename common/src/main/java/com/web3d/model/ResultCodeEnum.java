package com.web3d.model;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"操作成功"),
    LOGIN_ERROR(401,"用户名或者密码错误"),
    VALIDATECODE_ERROR(202 , "验证码错误") ,
    LOGIN_AUTH(208 , "用户未登录"),
    USER_NAME_IS_EXISTS(209 , "用户名已经存在"),
    USER_INVALID(210,"用户已封号"),
    SYSTEM_ERROR(500 , "网络有问题请稍后重试"),
    NODE_ERROR( 217, "该节点下有子节点，不可以删除"),
    DATA_ERROR(204, "数据异常"),
    ACCOUNT_STOP( 216, "账号已停用"),

    STOCK_LESS( 219, "库存不足"),

    NO_TOKEN(401,"token不能为空"),
    INVALID_TOKEN(401,"无效token"),
    EXPIRE_TOKEN(401,"token已过期"),
    ALGORITHMMISMATCH_TOKEN(401,"token算法不一致"),
    NOT_EXIST(404,"{} 不存在"),
    SIGNUP_CONFLICT(409, "该手机号已注册"),

    SERVER_ERROR(500,"服务端错误"),

    INSERT_FAIL(50001, "新增数据失败"),
    DUPLICATE_KEY(50002, "该记录已存在"),
    PARAM_VALID_ERROR(40001, "{} 校验不通过: {}"),
    ACCOUNT_DISABLED(40002, "账号已被禁用，请联系管理员");

    private final Integer code;
    private final String message; // 默认的静态消息
    private final String template; // 🎯 带占位符的模板

    ResultCodeEnum(Integer code, String template) {
        this.code = code;
        this.message = template;
        this.template = template;
    }

    // 备用构造：如果不需要占位符，直接传完整消息
    ResultCodeEnum(Integer code, String template, String message) {
        this.code = code;
        this.message = message;
        this.template = template;
    }


    /**
     * 🎯 核心方法：动态格式化消息
     * @param args 替换占位符的参数
     */
    public String format(Object... args) {
        if (args == null || args.length == 0) {
            return this.message;
        }
        // 利用 Spring 提供的 StrUtil 或 Hutool 的 StrUtil
        // 这里手写一个极其简单的替换逻辑（为了防止你引入额外依赖）
        String result = this.template;
        for (Object arg : args) {
            result = result.replaceFirst("\\{\\}", String.valueOf(arg));
        }
        return result;
    }
}
