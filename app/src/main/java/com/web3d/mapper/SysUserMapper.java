package com.web3d.mapper;


import com.web3d.entity.bo.SignupBo;
import com.web3d.entity.bo.UserBo;
import com.web3d.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper {

    SysUser getUserInfo(UserBo userBo);

    int insertUser(SignupBo signupBo);

    void updateUser(UserBo userBo);

}
