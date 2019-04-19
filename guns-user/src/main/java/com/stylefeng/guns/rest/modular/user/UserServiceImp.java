package com.stylefeng.guns.rest.modular.user;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MoocUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocUserT;
import com.stylefeng.guns.user.UserAPI;
import com.stylefeng.guns.user.vo.UserInfoModel;
import com.stylefeng.guns.user.vo.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;


@Component
@Service(interfaceClass = UserAPI.class)
@Transactional
public class UserServiceImp implements UserAPI {

    @Autowired
    private MoocUserTMapper moocUserTMapper ;

    /**
     * 注册
     * @param userModel
     * @return
     */
    @Override
    public boolean register(UserModel userModel) {
        MoocUserT moocUserT = new MoocUserT();
        //插入信息
        moocUserT.setUserName(userModel.getUsername());
        moocUserT.setEmail(userModel.getEmail());
        moocUserT.setUserPhone(userModel.getPhone());
        moocUserT.setAddress(userModel.getAddress());

        //使用md5的方式加密密码并插入
        String passwordMd5 = MD5Util.encrypt(userModel.getPassword());
        moocUserT.setUserPwd(passwordMd5);

        Integer insert = moocUserTMapper.insert(moocUserT);
        if(insert > 0){
            return true;
        }
        return false;
    }

    @Override
    public int login(String username, String password) {
        //根据登录信息从数据库中查找
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(username);
        MoocUserT result = moocUserTMapper.selectOne(moocUserT);
        //判断加密后的密码是否相同,相同则返回uuid
        if(result != null && result.getUuid() > 0){
            String encrypt = MD5Util.encrypt(password);
            if(result.getUserPwd().equals(encrypt)){
                return result.getUuid();
            }
        }
        //密码错误返回0
        return 0;
    }


    /**
     * 查找用户名是否已经存在
     * @param username
     * @return
     */
    @Override
    public boolean checkUsername(String username) {
        EntityWrapper<MoocUserT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name",username);
        Integer count = moocUserTMapper.selectCount(entityWrapper);
        if(count != null && count > 0){
            //表示账号已经存在
            return false;
        }
        //表示账号不存在
        return true;
    }

    private UserInfoModel do2InfoModel(MoocUserT moocUserT){
        UserInfoModel userInfoModel = new UserInfoModel();

        userInfoModel.setUsername(moocUserT.getUserName());
        userInfoModel.setUpdateTime(moocUserT.getUpdateTime().getTime());
        userInfoModel.setSex(moocUserT.getUserSex());
        userInfoModel.setPhone(moocUserT.getUserPhone());
        userInfoModel.setNickname(moocUserT.getNickName());
        userInfoModel.setLifeState(moocUserT.getLifeState()+"");
        userInfoModel.setHeadAddress(moocUserT.getHeadUrl());
        userInfoModel.setEmail(moocUserT.getEmail());
        userInfoModel.setBeginTime(moocUserT.getBeginTime().getTime());
        userInfoModel.setBirthday(moocUserT.getBirthday());
        userInfoModel.setBiography(moocUserT.getBiography());
        userInfoModel.setAddress(moocUserT.getAddress());

        return userInfoModel;
    }

    /**
     * 根据uuid查找相应的用户信息
     * @param uuid
     * @return
     */
    @Override
    public UserInfoModel getUserInfo(int uuid) {
        //根据uuid在数据库中查询到相应的信息
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUuid(uuid);
        MoocUserT user = moocUserTMapper.selectOne(moocUserT);
        //将信息转化为UserInfoModel
        UserInfoModel userInfoModel = do2InfoModel(user);
        //返回UserInfoModel
        return userInfoModel;
    }


    /**
     * 更新用户信息
     * @param userInfoModel
     * @return
     */
    @Override
    public UserInfoModel updateInfo(UserInfoModel userInfoModel) {
        //将userInfoModel转化为moocUserT
        MoocUserT moocUserT = new MoocUserT();

        moocUserT.setUuid(userInfoModel.getUuid());
        moocUserT.setAddress(userInfoModel.getAddress());
        moocUserT.setUserSex(userInfoModel.getSex());
        moocUserT.setUpdateTime(new Date(System.currentTimeMillis()));
        moocUserT.setNickName(userInfoModel.getNickname());
        moocUserT.setLifeState(Integer.valueOf(userInfoModel.getLifeState()));
        moocUserT.setHeadUrl(userInfoModel.getHeadAddress());
        moocUserT.setBirthday(userInfoModel.getBirthday());
        moocUserT.setBiography(userInfoModel.getBiography());
        moocUserT.setBeginTime(new Date(userInfoModel.getBeginTime()));
        moocUserT.setEmail(userInfoModel.getEmail());
        moocUserT.setUserPhone(userInfoModel.getPhone());

        System.out.println(moocUserT);
        //将moocUserT存储到数据库中
        Integer integer = moocUserTMapper.updateById(moocUserT);

        System.out.println(integer);

        if(integer > 0){
            //将修改后的信息查询出来
            MoocUserT userT = moocUserTMapper.selectOne(moocUserT);
            UserInfoModel newUserInfo = do2InfoModel(moocUserT);
            //返回给前端
            return newUserInfo;
        }
        //未修改成功,返回原来的信息
        return userInfoModel;
    }
}
