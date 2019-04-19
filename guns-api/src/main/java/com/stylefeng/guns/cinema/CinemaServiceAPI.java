package com.stylefeng.guns.cinema;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.cinema.vo.*;

import java.util.List;

public interface CinemaServiceAPI {

    /**
     * 根据CinemaRequestVO，查询影院列表
     * @param cinemaRequestVO
     * @return
     */
    Page<CinemaVO> getCinemas(CinemaRequestVO cinemaRequestVO);

    /**
     * 根据条件获取行政区域列表
     * @param areaId
     * @return
     */
    List<AreaVO> getAreas(int areaId);

    /**
     * 根据条件获取品牌列表，除了99以外，其他的数字均为isActive
     * @param brandId
     * @return
     */
    List<BrandVO> getBrands(int brandId);

    /**
     * 获取影厅类型
     * @param hallType
     * @return
     */
    List<HalltypeVO> getHallTypes(int hallType);

    /**
     * 根据影院编号，获取影院信息
     * @param cinemaId
     * @return
     */
    CinemaInfoVO getCinemaInfoById(int cinemaId);

    /**
     * 根据影院编号获取所有电影的信息和对应的放映场次信息
     * @param cinemaId
     * @return
     */
    FIlmInfoVO getCinemaInfoByCinemaId(int cinemaId);

    /**
     * 根据放映场次id获取放映信息
     * @param fieldId
     * @return
     */
    FilmFieldVO getFilmFieldInfo(int fieldId);

    /**
     * 根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
     * @param fieldId
     * @return
     */
    FIlmInfoVO getFilmInfoByFieldId(int fieldId);
}
