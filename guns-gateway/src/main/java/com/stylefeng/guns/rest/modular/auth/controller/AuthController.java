package com.stylefeng.guns.rest.modular.auth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthRequest;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthResponse;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import com.stylefeng.guns.user.UserAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 请求验证的
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:22
 */
@RestController
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Reference
    UserAPI userAPI;


    @RequestMapping(value = "${jwt.auth-path}")
    public ResponseVO<?> createAuthenticationToken(AuthRequest authRequest) {


        boolean validate = true;
        //去掉jwt的账号密码验证机制,使用我们自己的,登录成功会返回用户的uuid
        int userId = userAPI.login(authRequest.getUserName(), authRequest.getPassword());
        if(userId == 0){
            //登陆失败
            validate = false;
        }


        if (validate) {
            //生成token和randomkey
            final String randomKey = jwtTokenUtil.getRandomKey();
            final String token = jwtTokenUtil.generateToken(userId + "", randomKey);
            //登陆成功返回信息
            return ResponseVO.success(new AuthResponse(token,randomKey));
        } else {
            //登陆失败返回信息
            return ResponseVO.serviceFail("账户名或者密码错误");
        }
    }
}
