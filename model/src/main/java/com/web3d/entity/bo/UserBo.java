package com.web3d.entity.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "user的业务对象")
public class UserBo {
    private String id;
    private String userName;
    private String userPhone;
    private String userPassword;
    private String userEmail;
    private String token;
}
