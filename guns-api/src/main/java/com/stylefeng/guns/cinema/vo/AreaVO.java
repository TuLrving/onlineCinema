package com.stylefeng.guns.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AreaVO implements Serializable {
    private Integer areaId;
    private String areaName;
    private boolean isActive;
}
