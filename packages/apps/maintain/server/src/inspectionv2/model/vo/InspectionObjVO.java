package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe app端巡检任务详情-巡检对象跟踪
 * @date 2023/4/21 9:19
 */
@Data
public class InspectionObjVO {
    /**
     * 对象id
     */
    private Long targetId;
    /**
     * 对象编码
     */
    private String targetCode;
    /**
     * 二维码 code
     */
    private String qrCode;
    /**
     * 对象名称
     */
    private String targetName;
    /**
     * 对象分类
     */
    private String targetCategory;
    /**
     * 巡检状态
     */
    private String state;
    /**
     * 巡检时间
     */
    private LocalDateTime time;
    /**
     * 巡检人
     */
    private String staff;
    /**
     * 巡检结果
     */
    private String result;
    /**
     * 巡检id
     */
    private Long inspectionId;
    /**
     * 经纬度
     */
    private String latAndLon;
    /**
     * 经纬度地址
     */
    private String location;
    /**
     * 巡检对象具体地址
     */
    private String targetAddress;

    /**
     * 区域ID
     */
    private String areaId;

    /**
     * 区域名
     */
    private String areaName;

}
