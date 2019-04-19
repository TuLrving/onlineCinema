package com.stylefeng.guns.rest.modular.film;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.stylefeng.guns.film.FilmAsyncServiceAPI;
import com.stylefeng.guns.film.FilmServiceAPI;
import com.stylefeng.guns.film.vo.*;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmRequestVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/film/")
public class FilmController {

    @Reference(interfaceClass = FilmServiceAPI.class)
    FilmServiceAPI filmServiceAPI;

    //开启异步调用
    @Reference(interfaceClass = FilmAsyncServiceAPI.class,async = true)
    FilmAsyncServiceAPI filmAsyncServiceAPI;

    private static final String IMAGE_PRE = "http://img.meetingshop.cn/";

    /**
     * 获取首页
     * @return
     */
    @GetMapping("getIndex")
    public ResponseVO<FilmIndexVO> getIndex(){
        FilmIndexVO filmIndexVO = new FilmIndexVO();

        //获取所有的banners
        filmIndexVO.setBanners(filmServiceAPI.getBanners());
        //获取所有的hotFilms
        filmIndexVO.setHotFilms(filmServiceAPI.getHotFilms(true,8,1,1,99,99,99));
        //获取所有的soonFilms
        filmIndexVO.setSoonFilms(filmServiceAPI.getSoonFilms(true,8,1,1,99,99,99));
        //获取所有的boxRanking
        filmIndexVO.setBoxRanking(filmServiceAPI.getBoxRanking());
        //获取所有的expectRanking
        filmIndexVO.setExpectRanking(filmServiceAPI.getExpectRanking());
        //获取所有的top100
        filmIndexVO.setTop100(filmServiceAPI.getTop());

        return ResponseVO.success(IMAGE_PRE,filmIndexVO);
    }


    /**
     * 2、影片条件列表查询接口
     * @param catId
     * @param sourceId
     * @param yearId
     * @return
     */
    @GetMapping("getConditionList")
    public ResponseVO getConditionList(@RequestParam(value = "catId",required = false,defaultValue = "99") String catId,
                                       @RequestParam(value = "catId",required = false,defaultValue = "99") String sourceId,
                                       @RequestParam(value = "catId",required = false,defaultValue = "99") String yearId){
        FilmConditionVO filmConditionVO = new FilmConditionVO();
        //==判断uuid与前端传过来的id相等，相等将相应的实体active标记为true

        //-------------------------获取catInfo-------------------//
        List<CatVO> cats = filmServiceAPI.getCats();
        List<CatVO> catsResult = new ArrayList<>();

        CatVO catVO = null;
        boolean isActive = false;
        for (CatVO cat : cats) {
            if(cat.getCatId().equals("99")){
                catVO = cat;
                continue;
            }
            if(cat.getCatId().equals(catId)){
                isActive = true;
                cat.setActive(true);
            }else {
                cat.setActive(false);
            }
            catsResult.add(cat);
        }
        //不相等将所有的active均标记为true
        if(!isActive){
            catVO.setActive(true);
            catsResult.add(catVO);
        }else {
            catVO.setActive(false);
            catsResult.add(catVO);
        }

        //---------------------------获取sourceInfo-----------------//
        isActive = false;
        List<SourceVO> sources = filmServiceAPI.getSources();
        List<SourceVO> sourcesResult = new ArrayList<>();
        SourceVO sourceVO = null;
        for (SourceVO source : sources) {
            if(source.getSourceId().equals("99")){
                sourceVO = source;
                continue;
            }
            if(source.getSourceId().equals(sourceId)){
                isActive = true;
                source.setActive(true);
            }else {
                source.setActive(false);
            }
            sourcesResult.add(source);
        }
        //不相等将所有的active均标记为true
        if(!isActive){
            sourceVO.setActive(true);
            sourcesResult.add(sourceVO);
        }else {
            sourceVO.setActive(false);
            sourcesResult.add(sourceVO);
        }

        //----------------------------获取yearInfo-----------------//
        isActive = false;
        List<YearVO> years = filmServiceAPI.getYears();
        List<YearVO> yearsResult = new ArrayList<>();
        YearVO yearVO = null;
        for (YearVO year : years) {
            if(year.getYearId().equals("99")){
                yearVO = year;
                continue;
            }
            if(year.getYearId().equals(yearId)){
                isActive = true;
                year.setActive(true);
            }else {
                year.setActive(false);
            }
            yearsResult.add(year);
        }
        //不相等将所有的active均标记为true
        if(!isActive){
            yearVO.setActive(true);
            yearsResult.add(yearVO);
        }else {
            yearVO.setActive(false);
            yearsResult.add(yearVO);
        }

        filmConditionVO.setCats(catsResult);
        filmConditionVO.setYears(yearsResult);
        filmConditionVO.setSources(sourcesResult);

        return ResponseVO.success(filmConditionVO);
    }

    /**
     * 影片详情查询接口
     * @param filmRequestVO
     * @return
     */
    @GetMapping("getFilms")
    public ResponseVO getFilms(FilmRequestVO filmRequestVO){
        //根据showType判断影片查询类型
        FilmVO filmVO = null;
        //根据不同查询类型调用不同方法
        //查询类型，1-正在热映，2-即将上映，3-经典影片
        switch (filmRequestVO.getShowType()){
            case 1:filmVO = filmServiceAPI.getHotFilms(false,filmRequestVO.getPageSize(),
                    filmRequestVO.getNowPage(),filmRequestVO.getSortId(),
                    filmRequestVO.getCatId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId());
                break;
            case 2:filmVO = filmServiceAPI.getSoonFilms(false,filmRequestVO.getPageSize(),
                    filmRequestVO.getNowPage(),filmRequestVO.getSortId(),
                    filmRequestVO.getCatId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId());
                break;
            case 3:filmVO = filmServiceAPI.getClassicFilms(false,filmRequestVO.getPageSize(),
                    filmRequestVO.getNowPage(),filmRequestVO.getSortId(),
                    filmRequestVO.getCatId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId());
                break;
            default:filmVO = filmServiceAPI.getHotFilms(false,filmRequestVO.getPageSize(),
                    filmRequestVO.getNowPage(),filmRequestVO.getSortId(),
                    filmRequestVO.getCatId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId());
                break;

        }

        //根据sortId排序
        //添加各种条件查询
        //判断当前是第几页

        return ResponseVO.success(IMAGE_PRE,filmVO.getFilmInfos(),filmVO.getNowPage(),filmVO.getTotalPages());
    }

    @GetMapping("films/{searchParam}")
    public ResponseVO films(@PathVariable("searchParam")String searchParam,
                            int searchType) throws Exception{
        //根据searchType，判断查询类型.不同的查询类型，传入的条件会略有不同
        FilmDetailVO filmDetail = filmServiceAPI.getFilmDetail(searchType, searchParam);

        if(filmDetail == null){
            return ResponseVO.serviceFail("没有可查询的影片");
        }else if(filmDetail.getFilmId() == null || filmDetail.getFilmId().trim().length() == 0){
            return ResponseVO.serviceFail("没有可查询的影片");
        }

        String  filmId = filmDetail.getFilmId();

//        long beginTime = System.currentTimeMillis();

        //所有演员信息
//        List<ActorsVO> actors = filmAsyncServiceAPI.getActorsVO(filmId);//同步时的做法
        filmAsyncServiceAPI.getActorsVO(filmId);
        Future<List<ActorsVO>> actorsVOFuture = RpcContext.getContext().getFuture();
        //导演信息
//        ActorsVO director = filmAsyncServiceAPI.getDirector(filmId);
        filmAsyncServiceAPI.getDirector(filmId);
        Future<ActorsVO> directorFuture = RpcContext.getContext().getFuture();
        //影片介绍信息
//        FilmDescVO filmDescVO = filmAsyncServiceAPI.getFilmDescVO(filmId);
        filmAsyncServiceAPI.getFilmDescVO(filmId);
        Future<FilmDescVO> filmDescFuture = RpcContext.getContext().getFuture();
        //影片图片信息
//        ImgVO imgVO = filmAsyncServiceAPI.getImgVO(filmId);
        filmAsyncServiceAPI.getImgVO(filmId);
        Future<ImgVO> imgFuture = RpcContext.getContext().getFuture();
        long endTime = System.currentTimeMillis();

//        System.out.println("异步时间："+ (endTime - beginTime));

        //组织actors
        ActorsRequestVO actorsRequestVO = new ActorsRequestVO();
        actorsRequestVO.setActors(actorsVOFuture.get());
        actorsRequestVO.setDirector(directorFuture.get());

        //组织InfoRequestVO
        InfoRequestVO infoRequestVO = new InfoRequestVO();
        infoRequestVO.setActors(actorsRequestVO);
        infoRequestVO.setBiography(filmDescFuture.get());
        infoRequestVO.setFilmId(filmId);
        infoRequestVO.setImgs(imgFuture.get());

        //组织filmDetail,此时所有信息组织到一起
        filmDetail.setInfo04(infoRequestVO);

        //查询影片的详细信息 -->Dubbo的异步获取
        return ResponseVO.success(IMAGE_PRE,filmDetail);
    }
}
