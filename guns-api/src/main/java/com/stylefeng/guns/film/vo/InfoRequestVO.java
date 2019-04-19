package com.stylefeng.guns.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InfoRequestVO implements Serializable {

    //组织actors信息
    private ActorsRequestVO actors;
    //组织影片介绍信息
    private FilmDescVO biography;
    //组织影片照片地址信息
    private ImgVO imgs;
    //filmId
    private String filmId;
}
