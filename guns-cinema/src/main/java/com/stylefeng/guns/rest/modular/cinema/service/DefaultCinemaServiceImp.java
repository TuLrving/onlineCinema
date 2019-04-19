package com.stylefeng.guns.rest.modular.cinema.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.cinema.CinemaServiceAPI;
import com.stylefeng.guns.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.MoocAreaDictT;
import com.stylefeng.guns.rest.common.persistence.model.MoocBrandDictT;
import com.stylefeng.guns.rest.common.persistence.model.MoocCinemaT;
import com.stylefeng.guns.rest.common.persistence.model.MoocHallDictT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class DefaultCinemaServiceImp implements CinemaServiceAPI {

    @Autowired
    private MoocAreaDictTMapper moocAreaDictTMapper;

    @Autowired
    private MoocBrandDictTMapper moocBrandDictTMapper;

    @Autowired
    private MoocCinemaTMapper moocCinemaTMapper;

    @Autowired
    private MoocFieldTMapper moocFieldTMapper;

    @Autowired
    private MoocHallDictTMapper moocHallDictTMapper;

    @Autowired
    private MoocHallFilmInfoTMapper moocHallFilmInfoTMapper;

    /**
     * 根据CinemaRequestVO，查询影院列表
     *
     * @param cinemaRequestVO
     * @return
     */
    @Override
    public Page<CinemaVO> getCinemas(CinemaRequestVO cinemaRequestVO) {
        //业务实体
        List<CinemaVO> cinemas = new ArrayList<>();
        //根据条件==> brandId,districtId,hallType判断，为99表示无限制，为其他表示有限制
        EntityWrapper<MoocCinemaT> entityWrapper = new EntityWrapper<>();
        if(cinemaRequestVO.getBrandId() != 99){
            entityWrapper.eq("brand_id",cinemaRequestVO.getBrandId());
        }
        if(cinemaRequestVO.getDistrictId() != 99){
            entityWrapper.eq("area_id",cinemaRequestVO.getDistrictId());
        }
        if(cinemaRequestVO.getHallType() != 99){
            entityWrapper.like("hall_ids","%#" + cinemaRequestVO.getHallType() + "#%");
        }
        Page<MoocCinemaT> page = new Page<>(cinemaRequestVO.getNowPage(),cinemaRequestVO.getPageSize());
        List<MoocCinemaT> moocCinemaTS = moocCinemaTMapper.selectPage(page, entityWrapper);
        //组织业务实体
        for (MoocCinemaT moocCinemaT : moocCinemaTS) {
            CinemaVO cinemaVO = new CinemaVO();

            cinemaVO.setAddress(moocCinemaT.getCinemaAddress());
            cinemaVO.setCinemaName(moocCinemaT.getCinemaName());
            cinemaVO.setMinimumPrice(moocCinemaT.getMinimumPrice()+"");
            cinemaVO.setUuid(moocCinemaT.getUuid()+"");

            cinemas.add(cinemaVO);
        }

        //组织返回值
        Page<CinemaVO> result = new Page<>();

        long counts = moocCinemaTMapper.selectCount(entityWrapper);
        result.setRecords(cinemas);
        result.setTotal(counts);
        result.setCurrent(cinemaRequestVO.getNowPage());
        result.setSize(cinemaRequestVO.getPageSize());

        return result;
    }

    /**
     * 根据条件获取行政区域列表
     *
     * @param areaId
     * @return
     */
    @Override
    public List<AreaVO> getAreas(int areaId) {
        //业务实体
        List<AreaVO> areas = new ArrayList<>();
        //判断areaId是否存在
        MoocAreaDictT moocAreaDictT = moocAreaDictTMapper.selectById(areaId);
        boolean flag = false;
        if(areaId == 99 || moocAreaDictT == null || moocAreaDictT.getUuid() == null){
            flag = true;
        }
        //根据判断结果进行处理
        List<MoocAreaDictT> moocAreaDictTS = moocAreaDictTMapper.selectList(null);
        for (MoocAreaDictT areaDictT : moocAreaDictTS) {
            AreaVO areaVO = new AreaVO();
            areaVO.setAreaName(areaDictT.getShowName());
            areaVO.setAreaId(areaDictT.getUuid());
            if(flag){
                //说明要选择的为99
                if(areaDictT.getUuid() == 99)
                    areaVO.setActive(true);
            }else{
                //否则只有符合要求时才设置为true
                if(areaDictT.getUuid() == areaId)
                    areaVO.setActive(true);
            }
            areas.add(areaVO);
        }
        return areas;
    }

    /**
     * 根据条件获取品牌列表，除了99以外，其他的数字均为isActive
     *
     * @param brandId
     * @return
     */
    @Override
    public List<BrandVO> getBrands(int brandId) {
        //业务实体
        List<BrandVO> brands = new ArrayList<>();
        //判断brandId是否存在
        MoocBrandDictT moocBrandDictT = moocBrandDictTMapper.selectById(brandId);
        boolean flag = false;
        if(brandId == 99 || moocBrandDictT == null || moocBrandDictT.getUuid() == null){
            flag = true;
        }
        //根据判断结果进行处理
        List<MoocBrandDictT> moocBrandDictTs = moocBrandDictTMapper.selectList(null);
        for (MoocBrandDictT brandDictT : moocBrandDictTs) {
           BrandVO brandVO = new BrandVO();
           brandVO.setBrandId(brandDictT.getUuid());
           brandVO.setBrandName(brandDictT.getShowName());
           if(flag){
               if(brandDictT.getUuid() == 99){
                   brandVO.setActive(true);
               }
           }else {
               if(brandDictT.getUuid() == brandId){
                   brandVO.setActive(true);
               }
           }
           brands.add(brandVO);
        }
        return brands;
    }

    /**
     * 获取影厅类型
     *
     * @param hallType
     * @return
     */
    @Override
    public List<HalltypeVO> getHallTypes(int hallType) {
        //业务实体
        List<HalltypeVO> halltypes = new ArrayList<>();
        //判断hallType是否存在
        MoocHallDictT moocHallDictT = moocHallDictTMapper.selectById(hallType);
        boolean flag = false;
        if(hallType == 99 || moocHallDictT == null ||moocHallDictT.getUuid() == null){
            flag = true;
        }
        List<MoocHallDictT> moocHallDictTS = moocHallDictTMapper.selectList(null);
        for (MoocHallDictT hallDictT : moocHallDictTS) {
            HalltypeVO halltypeVO = new HalltypeVO();
            halltypeVO.setHalltypeId(moocHallDictT.getUuid());
            halltypeVO.setHalltypeName(moocHallDictT.getShowName());

            if(flag){
                if(hallDictT.getUuid() == 99){
                    halltypeVO.setActive(true);
                }
            }else {
                if(hallDictT.getUuid() == hallType){
                    halltypeVO.setActive(true
                    );
                }
            }
            halltypes.add(halltypeVO);
        }
        return halltypes;
    }

    /**
     * 根据影院编号，获取影院信息
     *
     * @param cinemaId
     * @return
     */
    @Override
    public CinemaInfoVO getCinemaInfoById(int cinemaId) {
        //根据影院编号获取信息
        MoocCinemaT moocCinemaT = moocCinemaTMapper.selectById(cinemaId);
        //组织业务实体
        CinemaInfoVO cinemaInfoVO = new CinemaInfoVO();
        cinemaInfoVO.setImgUrl(moocCinemaT.getImgAddress());
        cinemaInfoVO.setCinemaPhone(moocCinemaT.getCinemaPhone());
        cinemaInfoVO.setCinemaName(moocCinemaT.getCinemaName());
        cinemaInfoVO.setCinemaId(moocCinemaT.getUuid()+"");
        cinemaInfoVO.setCinemaAdress(moocCinemaT.getCinemaAddress());
        return cinemaInfoVO;
    }

    /**
     * 根据影院编号获取所有电影的信息和对应的放映场次信息
     *
     * @param cinemaId
     * @return
     */
    @Override
    public FIlmInfoVO getCinemaInfoByCinemaId(int cinemaId) {
        return null;
    }

    /**
     * 根据放映场次id获取放映信息
     *
     * @param fieldId
     * @return
     */
    @Override
    public FilmFieldVO getFilmFieldInfo(int fieldId) {
        return null;
    }

    /**
     * 根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
     *
     * @param fieldId
     * @return
     */
    @Override
    public FIlmInfoVO getFilmInfoByFieldId(int fieldId) {
        return null;
    }
}
