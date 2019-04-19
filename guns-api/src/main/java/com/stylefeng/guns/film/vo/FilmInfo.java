package com.stylefeng.guns.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FilmInfo implements Serializable {
    private String filmId;
    private Integer filmType;
    private String imgAddress;
    private String filmName;
    private String filmScore;

    private String expectNum;
    private String showTime;

    private int boxNum;

    private String socre;
}
