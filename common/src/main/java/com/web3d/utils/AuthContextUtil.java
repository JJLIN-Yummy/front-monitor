package com.web3d.utils;


import com.web3d.entity.system.SysUser;

public class AuthContextUtil {
    //创建ThreadLocal对象
    private static final ThreadLocal<SysUser> threadLocal = new ThreadLocal<>();

    //ThreadLocal添加数据
    public static void set(SysUser sysUser){
        threadLocal.set(sysUser);
    }

    //ThreadLocal获取数据
    public static SysUser get(){
        return threadLocal.get();
    }

    //ThreadLocal删除数据
    public static void remove(){
        threadLocal.remove();
    }
}
