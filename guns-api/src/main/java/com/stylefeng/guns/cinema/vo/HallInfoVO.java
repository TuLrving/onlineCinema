package com.stylefeng.guns.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class HallInfoVO implements Serializable {
    private Integer hallFieldId;
    private String hallName;
    private String price;
    private String seatFile;
    private String soldSeats;
}
