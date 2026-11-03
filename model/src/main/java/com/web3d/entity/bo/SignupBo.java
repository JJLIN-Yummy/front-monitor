package com.web3d.entity.bo;

import com.web3d.entity.dto.SignupDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignupBo extends SignupDto {
    private Long id;
}
