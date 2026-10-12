package com.guhecloud.cube.biz.adapter.park.server.application.dahua.model;

import lombok.Data;

/**
 * 大华返回的车场西信息对象
 */
@Data
public class DaHuaParkingLot {
    private Long id;
    /**
     * 绑定车场的车辆数
     */
    private int carNum;
    /**
     * 入口数
     */
    private int entranceNum;
    /**
     * 出口数
     */
    private int exitusNum;
    /**
     * 余位数
     */
    private int idleLot;
    /**
     * 总车位数
     */
    private int totalLot;
    /**
     * 使用了的车位数
     */
    private int usedLot;
    /**
     * 绑定的LED数
     */
    private int ledNum;
    /**
     * 场区名称
     */
    private String parkingLot;
    /**
     * 场区编号
     */
    private String parkingLotCode;
    /**
     * 父场区名称
     */
    private String parentName;
    /**
     * 场区所属组织编码
     */
    private String orgCode;

}
