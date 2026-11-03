package com.web3d.service;


import com.web3d.entity.bo.UserBo;
import com.web3d.entity.dto.LoginDto;
import com.web3d.entity.dto.SignupDto;
import com.web3d.entity.vo.LoginVo;

public interface LoginService {
    //根据账号密码获取用户信息
    LoginVo getUserInfo(UserBo userBo);

    void updateToken(LoginVo loginVo);

    void insertUserInfo(UserBo userBo);

    //todo: 判断当前手机号是否已经注册


}
