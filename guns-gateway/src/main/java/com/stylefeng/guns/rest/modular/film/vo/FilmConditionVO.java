package com.stylefeng.guns.rest.modular.film.vo;

import com.stylefeng.guns.film.vo.CatVO;
import com.stylefeng.guns.film.vo.SourceVO;
import com.stylefeng.guns.film.vo.YearVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FilmConditionVO implements Serializable {
    private List<CatVO> cats;
    private List<SourceVO> sources;
    private List<YearVO> years;
}
