package com.stylefeng.guns.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ActorsRequestVO implements Serializable {
    //导演
    private ActorsVO director;
    //所有演员
    private List<ActorsVO> actors;
}
