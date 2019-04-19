package com.stylefeng.guns.film;

import com.stylefeng.guns.film.vo.*;

import java.util.List;

public interface FilmServiceAPI {
    //获取banners
    List<BannerVO> getBanners();
    //获取热映影片
    FilmVO getHotFilms(boolean isLimit, int num, int nowPage, int sortId, int catId, int sourceId, int yearId);
    //获取即将上映影片[根据受欢迎程度排序]
    FilmVO getSoonFilms(boolean isLimit,int num,int nowPage,int sortId,int catId,int sourceId,int yearId);
    //获取经典影片
    FilmVO getClassicFilms(boolean isLimit,int num,int nowPage,int sortId,int catId,int sourceId,int yearId);
    //获取票房排行榜
    List<FilmInfo> getBoxRanking();
    //获取人气排行榜
    List<FilmInfo> getExpectRanking();
    //获取Top100
    List<FilmInfo> getTop();

    //===== >> 获取影片条件接口
    // 分类条件
    List<CatVO> getCats();
    // 片源条件
    List<SourceVO> getSources();
    //获取年代条件
    List<YearVO> getYears();



    //获取影片详情信息
    FilmDetailVO getFilmDetail(int searchType,String searchParam);

    //获取影片描述信息
    FilmDescVO getFilmDescVO(String filmId);

    //获取导演信息
    ActorsVO getDirector(String filmId);
    //获取演员信息
    List<ActorsVO> getActorsVO(String filmId);

    //获取图片信息
    ImgVO getImgVO(String filmId);
}
