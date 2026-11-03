package com.web3d.converter;

import com.web3d.entity.bo.UserBo;
import com.web3d.entity.dto.LoginDto;
import com.web3d.entity.dto.SignupDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConverter {
    UserBo toBo(LoginDto loginDto);
    UserBo toBo(SignupDto signupDto);
}
