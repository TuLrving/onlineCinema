package com.stylefeng.guns.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class HalltypeVO implements Serializable {
    private Integer halltypeId;
    private String halltypeName;
    private boolean isActive;
}
