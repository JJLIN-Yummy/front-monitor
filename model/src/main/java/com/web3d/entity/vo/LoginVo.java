package com.web3d.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录成功响应结果实体类")
public class LoginVo {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "用户邮箱")
    private String userEmail;

    @Schema(description = "令牌")
    private String token;


}
