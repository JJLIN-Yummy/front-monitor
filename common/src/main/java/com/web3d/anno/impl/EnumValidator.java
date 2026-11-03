package com.web3d.anno.impl;

import com.web3d.anno.EnumValid;
import com.web3d.exception.BusinessException;
import com.web3d.model.ResultCodeEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;

/**
 * 自定义枚举校验的具体执行逻辑
 */
public class EnumValidator implements ConstraintValidator<EnumValid, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        // 获取注解上配置的枚举类（比如 EnvEnum.class）
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 1. 如果为空，交给 @NotBlank 去处理，这里直接返回 true 放行
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        // 2. 获取该枚举的所有实例
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null) {
            return false;
        }

        // 3. 遍历匹配
        for (Enum<?> enumConstant : enumConstants) {
            try {
                // 约定：你的所有枚举类里，都必须有一个 getName() 方法
                Method getNameMethod = enumConstant.getClass().getMethod("getName");
                String enumName = (String) getNameMethod.invoke(enumConstant);

                if (enumName.equals(value)) {
                    return true;
                }
            } catch (Exception e) {
                // 如果枚举里没写 getName() 方法，直接抛错提醒开发者
                throw new BusinessException(ResultCodeEnum.NOT_EXIST, value);
            }
        }

        // 4. 遍历完没匹配上，校验失败
        return false;
    }
}