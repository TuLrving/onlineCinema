package com.stylefeng.guns.rest.modular.user;


import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import com.stylefeng.guns.user.UserAPI;
import com.stylefeng.guns.user.vo.UserInfoModel;
import com.stylefeng.guns.user.vo.UserModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Reference
    private UserAPI userAPI;

    /**
     * 注册
     */
    @PostMapping("register")
    public ResponseVO register(UserModel userModel){
        //判断用户名是否为空
        if(userModel.getUsername() == null || userModel.getUsername().trim().length() == 0){
            return ResponseVO.serviceFail("用户名不能为空");
        }
        //判断密码是否为空
        if(userModel.getPassword() == null || userModel.getPassword().trim().length() == 0){
            return ResponseVO.serviceFail("密码不能为空");
        }
        //返回消息
        boolean isSuccess = userAPI.register(userModel);
        if(isSuccess){
            return ResponseVO.success("注册成功");
        }
        return ResponseVO.serviceFail("注册失败");
    }

    /**
     * 检查用户名是否可用
     */
    @PostMapping("check")
    public ResponseVO check(String username){
        //判断用户名是否为空
        if(username != null && username.trim().length() > 0){
            boolean notExist = userAPI.checkUsername(username);
            if (notExist){
                //账号不存在,即账号可用使用
                return ResponseVO.success("用户名可用");
            } else{
                return ResponseVO.serviceFail("用户名已存在");
            }
        }else {
            return ResponseVO.serviceFail("用户名不能为空");
        }
    }

    /**
     * 用户退出功能
     */
    @GetMapping ("logout")
    public ResponseVO logout(){
        /**
         * 前端删除jwt缓存
         */
        return ResponseVO.serviceFail("用户名不能为空");
    }

    /**
     * 用户查询信息
     */
    @GetMapping ("getUserInfo")
    public ResponseVO getUserInfo(){
        String userId = CurrentUser.getUserId();
        //判断用户是否已经登录
        if(userId != null && userId.trim().length() > 0){
            int id = Integer.valueOf(userId);
            //后台进行查询用户信息
            UserInfoModel userInfo = userAPI.getUserInfo(id);
            if(userInfo != null){
                return ResponseVO.success(userInfo);
            }else {
                return ResponseVO.serviceFail("用户信息查询失败");
            }
        }
        return ResponseVO.serviceFail("用户未登录");
    }

    /**
     * 更新用户信息
     */
    @PostMapping ("updateUserInfo")
    public ResponseVO updateUserInfo(UserInfoModel userInfoModel){
        String userId = CurrentUser.getUserId();
        //判断用户是否已经登录
        if(userId != null && userId.trim().length() > 0){
            int id = Integer.valueOf(userId);
            //判断当前线程保存的用户id是否与前端传来的id相同
            if(id != userInfoModel.getUuid()){
                return ResponseVO.serviceFail("请修改您的个人信息");
            }
            //后台进行更新用户信息并返回更新后的信息
            UserInfoModel userInfo = userAPI.updateInfo(userInfoModel);
            if(userInfo != null){
                return ResponseVO.success(userInfo);
            }else {
                return ResponseVO.serviceFail("用户信息查询失败");
            }
        }
        return ResponseVO.serviceFail("用户未登录");
    }
}
