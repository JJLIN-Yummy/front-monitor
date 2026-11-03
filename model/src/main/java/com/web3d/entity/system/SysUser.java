package com.web3d.entity.system;

import com.web3d.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "系统用户实体类")
public class SysUser extends BaseEntity {

    @Schema(description = "密码")
    private String userPassword;

    @Schema(description = "手机号")
    private String userPhone;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "用户邮箱")
    private String userEmail;

    @Schema(description = "token")
    private String token;

    @Schema(description = "是否有效")
    private Integer isValid;

}
