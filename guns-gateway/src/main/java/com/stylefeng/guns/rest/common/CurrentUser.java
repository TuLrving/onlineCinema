package com.stylefeng.guns.rest.common;

public class CurrentUser {
    //用于保存用户id
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    //将用户id保存到本地线程中
    public static void saveUserId(String userId){
        THREAD_LOCAL.set(userId);
    }

    //将用户id取出
    public static String getUserId(){
        return THREAD_LOCAL.get();
    }
}
