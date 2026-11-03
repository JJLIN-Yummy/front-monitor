package com.web3d.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "用户注册的请求参数")
public class SignupDto {

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String userPhone;

    @Schema(description = "密码")
    private String userPassword;

    @Schema(description = "邮箱")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}$", message = "邮箱格式不正确")
    private String userEmail;
}
