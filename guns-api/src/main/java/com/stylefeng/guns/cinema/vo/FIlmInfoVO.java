package com.stylefeng.guns.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FIlmInfoVO implements Serializable {
    private Integer filmId;
    private String filmName;
    private String filmLength;
    private String filmType;
    private String filmCats;
    private String actors;
    private String imgAddress;
    private List<FilmFieldVO> filmFields;
}
