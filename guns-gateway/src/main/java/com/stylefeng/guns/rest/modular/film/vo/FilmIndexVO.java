package com.stylefeng.guns.rest.modular.film.vo;

import com.stylefeng.guns.film.vo.BannerVO;
import com.stylefeng.guns.film.vo.FilmInfo;
import com.stylefeng.guns.film.vo.FilmVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FilmIndexVO implements Serializable {
    //获取所有的banners
    private List<BannerVO> banners;
    //获取所有的hotFilms
    private FilmVO hotFilms;
    //获取所有的soonFilms
    private FilmVO soonFilms;
    //获取所有的boxRanking
    private List<FilmInfo> boxRanking;
    //获取所有的expectRanking
    private List<FilmInfo> expectRanking;
    //获取所有的top100
    private List<FilmInfo> top100;
}
