package com.web3d.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Result<T> {
    //返回码
    @Schema(description = "状态码", example = "200")
    private Integer code;

    //返回消息
    @Schema(description = "提示信息", example = "操作成功")
    private String message;

    //返回数据
    @Schema(description = "返回数据")
    private T data;

    //私有化构造器
    private Result() {

    }


    //返回数据
    public static <T> Result<T> build(T body, Integer code, String message){
        Result<T> result = new Result<>();
        result.setData(body);
        result.setCode(code);
        result.setMessage(message);


        return result;
    }

    //通过枚举构造Result对象
    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum, String message){
        if(message == null || "".equals(message)){
            return Result.build(body, resultCodeEnum);
        }
        return Result.build(body, resultCodeEnum.getCode(), message);
    }

    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum){
        return Result.build(body, resultCodeEnum.getCode(), resultCodeEnum.getMessage());
    }
}
