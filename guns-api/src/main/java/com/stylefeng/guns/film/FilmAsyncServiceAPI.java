package com.stylefeng.guns.film;

import com.stylefeng.guns.film.vo.ActorsVO;
import com.stylefeng.guns.film.vo.FilmDescVO;
import com.stylefeng.guns.film.vo.ImgVO;

import java.util.List;

/**
 *
 * 用于异步调用
 */
public interface FilmAsyncServiceAPI {
    //获取影片描述信息
    FilmDescVO getFilmDescVO(String filmId);

    //获取导演信息
    ActorsVO getDirector(String filmId);

    //获取演员信息
    List<ActorsVO> getActorsVO(String filmId);

    //获取图片信息
    ImgVO getImgVO(String filmId);
}
