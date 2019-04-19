package com.stylefeng.guns.user;

import com.stylefeng.guns.user.vo.UserInfoModel;
import com.stylefeng.guns.user.vo.UserModel;

public interface UserAPI {
    int login(String username,String password); //登录

    boolean register(UserModel userModel); //注册

    boolean checkUsername(String username);//检查用户名是否已存在

    UserInfoModel getUserInfo(int uuid);//用户查找用户信息

    UserInfoModel updateInfo(UserInfoModel userInfoModel);//修改用户信息
}
