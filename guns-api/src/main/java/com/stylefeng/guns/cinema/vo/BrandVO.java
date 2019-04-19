package com.stylefeng.guns.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BrandVO implements Serializable {
    private Integer brandId;
    private String brandName;
    private boolean isActive;
}
