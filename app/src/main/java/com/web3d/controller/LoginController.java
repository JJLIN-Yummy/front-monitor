package com.web3d.controller;

import com.web3d.converter.UserConverter;
import com.web3d.entity.bo.UserBo;
import com.web3d.entity.dto.LoginDto;
import com.web3d.entity.dto.SignupDto;
import com.web3d.entity.vo.LoginVo;
import com.web3d.exception.BusinessException;
import com.web3d.model.Result;
import com.web3d.model.ResultCodeEnum;
import com.web3d.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Tag(name = "登录接口")
@RestController
@RequestMapping(value = "/api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserConverter userConverter;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVo> login(@Valid @RequestBody LoginDto loginDto){
        UserBo userBo = userConverter.toBo(loginDto);
        LoginVo userInfo = loginService.getUserInfo(userBo);
        if(userInfo == null){
            //报错
            throw new BusinessException(ResultCodeEnum.LOGIN_ERROR);
        }
        log.info(userInfo);
        return Result.build(userInfo, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/signup")
    public Result<Void> signup(@Valid @RequestBody SignupDto signupDto){
        UserBo userBo = userConverter.toBo(signupDto);
        LoginVo userInfo = loginService.getUserInfo(userBo);
        if(userInfo != null){
            //报错
            throw new BusinessException(ResultCodeEnum.SIGNUP_CONFLICT);
        }

        //插入数据
        loginService.insertUserInfo(userBo);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

}
