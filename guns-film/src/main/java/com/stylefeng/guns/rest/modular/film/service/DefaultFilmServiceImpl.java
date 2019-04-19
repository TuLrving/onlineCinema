package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.util.DateUtil;
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
public class DefaultFilmServiceImpl implements FilmServiceAPI {


    @Autowired
    private MoocBannerTMapper moocBannerTMapper;

    @Autowired
    private MoocFilmTMapper moocFilmTMapper;

    @Autowired
    private MoocCatDictTMapper moocCatDictTMapper;

    @Autowired
    private MoocSourceDictTMapper moocSourceDictTMapper;

    @Autowired
    private MoocYearDictTMapper moocYearDictTMapper;

    @Autowired
    private MoocFilmInfoTMapper moocFilmInfoTMapper;

    @Autowired
    private MoocActorTMapper moocActorTMapper;
    /**
     * 获取所有的banners
     * @return
     */
    @Override
    public List<BannerVO> getBanners() {
        List<MoocBannerT> moocBannerTS = moocBannerTMapper.selectList(null);
        List<BannerVO> result = new ArrayList<>();
        for (MoocBannerT moocBannerT : moocBannerTS) {
            BannerVO bannerVO = new BannerVO();
            bannerVO.setBannerId(moocBannerT.getUuid()+"");
            bannerVO.setBannerUrl(moocBannerT.getBannerUrl());
            bannerVO.setBannerAddress(moocBannerT.getBannerAddress());

            result.add(bannerVO);
        }

        return result;
    }

    /**
     * //将moocFilmTs转存到filmInfos中
     * @param moocFilmTS
     * @return
     */
    private List<FilmInfo> moocFilmTS2FilmInfo(List<MoocFilmT> moocFilmTS){
        List<FilmInfo> filmInfos = new ArrayList<>();
        for (MoocFilmT moocFilmT : moocFilmTS) {
            FilmInfo filmInfo = new FilmInfo();

            filmInfo.setSocre(moocFilmT.getFilmScore());
            filmInfo.setShowTime(DateUtil.getDay(moocFilmT.getFilmTime()));
            filmInfo.setImgAddress(moocFilmT.getImgAddress());
            filmInfo.setFilmType(moocFilmT.getFilmType());
            filmInfo.setFilmScore(moocFilmT.getFilmScore());
            filmInfo.setFilmName(moocFilmT.getFilmName());
            filmInfo.setFilmId(moocFilmT.getUuid()+"");
            filmInfo.setExpectNum(moocFilmT.getFilmPresalenum()+"");
            filmInfo.setBoxNum(moocFilmT.getFilmBoxOffice());

            filmInfos.add(filmInfo);
        }
        return filmInfos;
    }

    /**
     * 抽取获取三种电影中重复的代码块
     * @param isLimit
     * @param num
     * @param nowPage
     * @param sortId
     * @param catId
     * @param sourceId
     * @param yearId
     * @param entityWrapper
     * @param page
     * @return
     */
    private FilmVO getFilms(boolean isLimit, int num,int nowPage,int sortId,int catId,int sourceId,int yearId,EntityWrapper entityWrapper,Page page){
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();

        //根据isLimit判断是否是首页所需要的影片
        if(isLimit){
            //是首页需要的的影片
            List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
            //将moocFilmTs转存到filmInfos中
            filmInfos = moocFilmTS2FilmInfo(moocFilmTS);

            filmVO.setFilmInfos(filmInfos);
            filmVO.setFilmNum(moocFilmTS.size()+"");
        }else {
            //不是首页所需要的热映的影片

            //根据id限定查询条件
            if(sourceId != 99){
                entityWrapper.eq("film_source",sourceId);
            }
            if(yearId != 99){
                entityWrapper.eq("film_date",yearId);
            }
            if(catId != 99){
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats",catStr);
            }

            List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
            //将moocFilmTs转存到filmInfos中
            filmInfos = moocFilmTS2FilmInfo(moocFilmTS);

            int totalCounts = moocFilmTMapper.selectCount(entityWrapper);
            int totalPages = totalCounts / num + 1;

            filmVO.setNowPage(nowPage);
            filmVO.setTotalPages(totalPages);
            filmVO.setFilmInfos(filmInfos);
            filmVO.setFilmNum(moocFilmTS.size()+"");
        }
        return filmVO;
    }

    /**
     * 获取所有的热映影片，根据限制数量返回
     * @param isLimit
     * @param num
     * @return
     */
    @Override
    public FilmVO getHotFilms(boolean isLimit, int num,int nowPage,int sortId,int catId,int sourceId,int yearId) {

        //限制条件,限制为热映影片
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","1");

        //排序方式，1-按热门搜索，2-按时间搜索，3-按评价搜索
        Page<MoocFilmT> page = null;
        switch (sortId){
            case 1: page = new Page<>(nowPage,num,"film_box_office");
                break;
            case 2: page = new Page<>(nowPage,num,"film_date");
                break;
            case 3: page = new Page<>(nowPage,num,"film_score");
                break;
            default:
                page = new Page<>(nowPage,num,"film_box_office");
                break;
        }

        return getFilms(isLimit,num,nowPage,sortId,catId,sourceId,yearId,entityWrapper,page);
    }

    /**
     * 获取即将上映的电影，根据受欢迎程度排序
     * @param isLimit
     * @param num
     * @return
     */
    @Override
    public FilmVO getSoonFilms(boolean isLimit, int num,int nowPage,int sortId,int catId,int sourceId,int yearId) {
        //限制条件,限制为热映影片，1代表正在热映的影片，2代表即将上映的影片
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","2");

        //排序方式，1-按热门搜索，2-按时间搜索，3-按评价搜索
        Page<MoocFilmT> page = null;
        switch (sortId){
            case 1: page = new Page<>(nowPage,num,"film_preSaleNum");
                break;
            case 2: page = new Page<>(nowPage,num,"film_date");
                break;
            case 3: page = new Page<>(nowPage,num,"film_preSaleNum");
                break;
            default:
                page = new Page<>(nowPage,num,"film_preSaleNum");
                break;
        }

        return getFilms(isLimit,num,nowPage,sortId,catId,sourceId,yearId,entityWrapper,page);
    }

    @Override
    public FilmVO getClassicFilms(boolean isLimit,int num,int nowPage, int sortId, int catId, int sourceId, int yearId) {
        //限制条件,限制为热映影片，1代表正在热映的影片，2代表即将上映的影片,3代表经典电影
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","3");

        //排序方式，1-按热门搜索，2-按时间搜索，3-按评价搜索
        Page<MoocFilmT> page = null;
        switch (sortId){
            case 1: page = new Page<>(nowPage,num,"film_box_office");
                break;
            case 2: page = new Page<>(nowPage,num,"film_date");
                break;
            case 3: page = new Page<>(nowPage,num,"film_score");
                break;
            default:
                page = new Page<>(nowPage,num,"film_box_office");
                break;
        }

        return getFilms(false,num,nowPage,sortId,catId,sourceId,yearId,entityWrapper,page);
    }

    /**
     * 获取正在热映的票房排行榜
     * @return
     */
    @Override
    public List<FilmInfo> getBoxRanking() {
        // -> 条件，热映的票房排行前十
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","1");

        Page<MoocFilmT> page = new Page<>(1,10,"film_box_office");

        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);

        List<FilmInfo> filmInfos = moocFilmTS2FilmInfo(moocFilmTS);

        return filmInfos;
    }

    /**
     * 获取人气排行榜(预售)
     * @return
     */
    @Override
    public List<FilmInfo> getExpectRanking() {
        // -> 条件，预售排行前十
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","2");

        Page<MoocFilmT> page = new Page<>(1,10,"film_preSaleNum");

        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);

        List<FilmInfo> filmInfos = moocFilmTS2FilmInfo(moocFilmTS);

        return filmInfos;
    }

    /**
     * //获取Top100
     * @return
     */
    @Override
    public List<FilmInfo> getTop() {

        // -> 条件，正在热映的影片评分排行前十

        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","1");

        Page<MoocFilmT> page = new Page<>(1,10,"film_score");

        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);

        List<FilmInfo> filmInfos = moocFilmTS2FilmInfo(moocFilmTS);

        return filmInfos;
    }

    /**
     * 获取所有的cat
     * @return
     */
    @Override
    public List<CatVO> getCats() {
        List<CatVO> cats = new ArrayList<>();

        List<MoocCatDictT> moocCatDictTS = moocCatDictTMapper.selectList(null);
        for (MoocCatDictT moocCatDictT : moocCatDictTS) {
            CatVO cat = new CatVO();

            cat.setCatId(moocCatDictT.getUuid() + "");
            cat.setCatName(moocCatDictT.getShowName());

            cats.add(cat);
        }

        return cats;
    }

    /**
     * 获取来源分类的电影
     * @return
     */
    @Override
    public List<SourceVO> getSources() {
        List<SourceVO> sources = new ArrayList<>();
        List<MoocSourceDictT> moocSourceDictTS = moocSourceDictTMapper.selectList(null);
        for (MoocSourceDictT moocSourceDictT : moocSourceDictTS) {
            SourceVO sourceVO = new SourceVO();
            sourceVO.setSourceId(moocSourceDictT.getUuid() + "");
            sourceVO.setSourceName(moocSourceDictT.getShowName());

            sources.add(sourceVO);
        }
        return sources;
    }

    /**
     * 获取时间分类的电影
     * @return
     */
    @Override
    public List<YearVO> getYears() {
        List<YearVO> years = new ArrayList<>();
        List<MoocYearDictT> moocYearDictTS = moocYearDictTMapper.selectList(null);
        for (MoocYearDictT moocYearDictT : moocYearDictTS) {
            YearVO yearVO = new YearVO();
            yearVO.setYearId(moocYearDictT.getUuid() + "");
            yearVO.setYearName(moocYearDictT.getShowName());

            years.add(yearVO);
        }
        return years;
    }

    /**
     * 获取影片详细信息
     * @param searchType
     * @param searchParam
     * @return
     */
    @Override
    public FilmDetailVO getFilmDetail(int searchType, String searchParam) {
        FilmDetailVO detailVO = null;
        //searchType : 0表示按照编号查找，1表示按照名称查找
        if(searchType == 0){
            detailVO = moocFilmTMapper.getFilmDetailById(searchParam);
        }else {
            detailVO = moocFilmTMapper.getFilmDetailByName(searchParam);
        }
        return detailVO;
    }


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
