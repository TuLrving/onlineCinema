package com.stylefeng.guns.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActorsVO implements Serializable {
    private String imgAddress;
    private String directorName;
    private String roleName;
}
