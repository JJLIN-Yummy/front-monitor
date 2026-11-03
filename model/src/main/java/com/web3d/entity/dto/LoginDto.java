package com.web3d.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "用户登录的请求参数")
public class LoginDto {
    @Schema(description = "用户名")
    private String userName;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号")
    private String userPhone;

    @NotBlank(message = "密码不能为空")
    @Size(max = 50, message = "密码长度不能超过50位")
    @Schema(description = "密码")
    private String userPassword;


}
