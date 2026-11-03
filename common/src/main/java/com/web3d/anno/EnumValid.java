package com.web3d.anno;

import com.web3d.anno.impl.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 自定义枚举校验注解
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class) // 🎯 指定真正的校验逻辑类
public @interface EnumValid {

    // 默认错误提示语
    String message() default "参数不在允许的枚举范围内";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // 🎯 核心：指定要校验的目标枚举类
    Class<? extends Enum<?>> enumClass();
}