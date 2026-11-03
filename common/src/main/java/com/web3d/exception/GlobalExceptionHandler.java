package com.web3d.exception;
import com.web3d.model.Result;
import com.web3d.model.ResultCodeEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Log4j2
@RestControllerAdvice//开启全局的异常捕获
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result error(Exception e){
        log.error("全局异常捕获：", e);
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(value = BusinessException.class)
    public Result businessError(BusinessException e){
        log.error("全局业务错误捕获：", e);
        return Result.build(null, e.getResultCodeEnum(),e.getMessage());
    }

    /**
     * 🎯 核心拦截：处理 DTO 上 @NotBlank、@Size 等注解校验失败抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidException(MethodArgumentNotValidException e) {
        // 1. 拿到校验失败的结果集
        BindingResult bindingResult = e.getBindingResult();
        // 获取所有校验失败的字段列表
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        // 2. 获取第一个校验失败的字段（通常给前端弹一个提示就够了）
        FieldError fieldError = bindingResult.getFieldError();
        FieldError firstError = fieldErrors.get(0);
        String fieldName = firstError.getField();


        String errorMessage = "参数校验不通过";
        if (fieldError != null) {
            // 这里的 message 就是你截图里写的 "env不能为空" 或 "长度不超过3"
            errorMessage = fieldError.getDefaultMessage();
        }

        // 3. 打印日志（使用 warn 级别，因为这是前端传参问题，不是系统崩溃）
        log.warn("参数校验失败: {}", errorMessage);

        // 4. 🎯 返回你自己的统一封装格式
        // 假设你的 ResultCodeEnum 里有一个 PARAM_VALID_ERROR(40001, "参数校验不通过")
        return Result.build(null,ResultCodeEnum.PARAM_VALID_ERROR, ResultCodeEnum.PARAM_VALID_ERROR.format(fieldName, errorMessage));
    }




}
