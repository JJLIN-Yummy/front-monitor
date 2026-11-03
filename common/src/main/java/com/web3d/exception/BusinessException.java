package com.web3d.exception;

import com.web3d.model.ResultCodeEnum;
import lombok.Getter;

/**
 * 自定义业务异常类
 * 继承 RuntimeException，因为 Spring 默认对 RuntimeException 进行事务回滚
 */
@Getter // 使用 Lombok 生成 getter 方法，省去手写
public class BusinessException extends RuntimeException {

    private Integer code;
    private String message;
    private ResultCodeEnum resultCodeEnum;

    public BusinessException(ResultCodeEnum resultCodeEnum, Object... args){
        this.resultCodeEnum = resultCodeEnum;
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.format(args);
    }
}