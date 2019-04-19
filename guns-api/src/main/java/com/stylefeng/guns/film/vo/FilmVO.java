package com.stylefeng.guns.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FilmVO implements Serializable {
    private String filmNum;
    private Integer nowPage;
    private Integer totalPages;



    private List<FilmInfo> filmInfos;
}
