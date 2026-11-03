package com.web3d.service.impl;

import com.web3d.entity.bo.SignupBo;
import com.web3d.entity.bo.UserBo;
import com.web3d.entity.dto.LoginDto;
import com.web3d.entity.dto.SignupDto;
import com.web3d.entity.system.SysUser;
import com.web3d.entity.vo.LoginVo;
import com.web3d.exception.BusinessException;
import com.web3d.mapper.SysUserMapper;
import com.web3d.model.ResultCodeEnum;
import com.web3d.service.LoginService;
import com.web3d.utils.JwtUtil;
import com.web3d.utils.PasswordUtil;
import com.web3d.utils.SnowflakeIdUtil;
import com.web3d.utils.ValidUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    @Transactional
    public LoginVo getUserInfo(UserBo userBo) {

        SysUser userInfo = sysUserMapper.getUserInfo(userBo);
        log.info("service:{}",userInfo);

        if(userInfo != null){

            boolean valid = ValidUtil.isValid(userInfo.getIsValid());
            if(!valid){
                throw new BusinessException(ResultCodeEnum.ACCOUNT_DISABLED);
            }

            //验证数据库密码和传入的密码是否合法
            boolean pwdMatches = PasswordUtil.matches(userBo.getUserPassword(), userInfo.getUserPassword());
            if(!pwdMatches){
                return null;
            }


            //返回controller层的对象
            LoginVo loginVo = new LoginVo();
            BeanUtils.copyProperties(userInfo,loginVo);
            //=============JwtToken==============
            String userName = userInfo.getUserName();//用户name
            //生成签名
            String token = JwtUtil.sign(userName,false);

            //将token设置到vo,传输给前端
            loginVo.setToken(token);
            return loginVo;
        }

        return null;


    }

    @Override
    @Transactional
    public void updateToken(LoginVo loginVo) {
        //将token插入数据库
        UserBo userBo = new UserBo();
        userBo.setId(loginVo.getId());
        userBo.setUserName(loginVo.getUserName());
        userBo.setToken(loginVo.getToken());
        log.info("陈墨瞳:{}",userBo);
        sysUserMapper.updateUser(userBo);
    }

    @Override
    @Transactional
    public void insertUserInfo(UserBo userBo) {
        SignupBo signupBo = new SignupBo();
        BeanUtils.copyProperties(userBo,signupBo);
        //雪花算法 id
        signupBo.setId(SnowflakeIdUtil.generateId());
        //加密密码
        signupBo.setUserPassword(PasswordUtil.encode(userBo.getUserPassword()));
        log.info("signBo", signupBo);
        int row = sysUserMapper.insertUser(signupBo);
        if(row <= 0) {
            throw new BusinessException(ResultCodeEnum.INSERT_FAIL);
        }
    }
}
