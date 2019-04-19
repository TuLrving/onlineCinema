package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.film.vo.FilmDetailVO;
import com.stylefeng.guns.rest.common.persistence.model.MoocFilmT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
 * @author LrvingTc
 * @since 2019-03-31
 */

public interface MoocFilmTMapper extends BaseMapper<MoocFilmT> {
    FilmDetailVO getFilmDetailByName(@Param("fileName") String fileName);

    FilmDetailVO getFilmDetailById(@Param("uuid") String uuid);
}
