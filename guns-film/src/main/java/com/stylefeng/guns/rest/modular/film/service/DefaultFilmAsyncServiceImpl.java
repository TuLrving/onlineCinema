package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.film.FilmAsyncServiceAPI;
import com.stylefeng.guns.film.FilmServiceAPI;
import com.stylefeng.guns.film.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class DefaultFilmAsyncServiceImpl implements FilmAsyncServiceAPI {


    @Autowired
    private MoocFilmInfoTMapper moocFilmInfoTMapper;

    @Autowired
    private MoocActorTMapper moocActorTMapper;


    /**
     * 通过filmId获取完整的影片信息,供内部使用
     * @param filmId
     * @return
     */
    private MoocFilmInfoT getFilmInfo(String filmId){
        MoocFilmInfoT moocFilmInfoT = new MoocFilmInfoT();
        moocFilmInfoT.setUuid(Integer.valueOf(filmId));

        moocFilmInfoT  = moocFilmInfoTMapper.selectOne(moocFilmInfoT);

        return moocFilmInfoT;

    }

    /**
     * 获取影片描述信息
     * @param filmId
     * @return
     */
    @Override
    public FilmDescVO getFilmDescVO(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);

        FilmDescVO filmDescVO = new FilmDescVO();
        filmDescVO.setBiography(moocFilmInfoT.getBiography());
        filmDescVO.setFilmId(moocFilmInfoT.getFilmId());

        return filmDescVO;
    }

    /**
     * 获取导演信息
     * @param filmId
     * @return
     */
    @Override
    public ActorsVO getDirector(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);

        Integer directorId = moocFilmInfoT.getDirectorId();
        MoocActorT moocActorT = moocActorTMapper.selectById(directorId);
        ActorsVO actorsVO = new ActorsVO();
        actorsVO.setDirectorName(moocActorT.getActorName());
        actorsVO.setImgAddress(moocActorT.getActorImg());

        return actorsVO;
    }

    /**
     * 获取演员信息
     * @param filmId
     * @return
     */
    @Override
    public List<ActorsVO> getActorsVO(String filmId) {
        List<ActorsVO> actors = moocActorTMapper.getActors(filmId);
        return actors;
    }

    /**
     * 获取影片图片信息
     * @param filmId
     * @return
     */
    @Override
    public ImgVO getImgVO(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);
        String filmImgs = moocFilmInfoT.getFilmImgs();
        String[] imgs = filmImgs.split(",");

        ImgVO imgVO = new ImgVO();
        imgVO.setMainImg(imgs[0]);
        imgVO.setImg01(imgs[1]);
        imgVO.setImg02(imgs[2]);
        imgVO.setImg03(imgs[3]);
        imgVO.setImg04(imgs[4]);

        return imgVO;
    }
}
