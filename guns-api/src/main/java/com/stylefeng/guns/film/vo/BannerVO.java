package com.stylefeng.guns.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BannerVO implements Serializable {
    private String bannerId;
    private String bannerAddress;
    private String bannerUrl;

    //使用lombok自动生成get，set，tostring方法
}
