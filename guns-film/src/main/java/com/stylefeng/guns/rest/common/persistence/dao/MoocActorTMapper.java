package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.film.vo.ActorsVO;
import com.stylefeng.guns.rest.common.persistence.model.MoocActorT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 演员表 Mapper 接口
 * </p>
 *
 * @author LrvingTc
 * @since 2019-03-31
 */
public interface MoocActorTMapper extends BaseMapper<MoocActorT> {

    List<ActorsVO> getActors(@Param("filmId") String filmId);

}
