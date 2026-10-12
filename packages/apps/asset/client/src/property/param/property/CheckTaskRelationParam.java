package com.guhecloud.cube.property.management.client.property.param.property;

import lombok.Data;

import java.util.List;

@Data
public class CheckTaskRelationParam {
    /**
     * 盘点任务id
     */
    private Long taskId;
    /**
     * 资产id
     */
    private Long propertyId;
    /**
     * 盘点结果
     */
    private String checkStatus;
    /**
     * 使用人
     */
    private Long receiveUserId;
    /**
     * 使用部门
     */
    private Long receiveDepId;
    private String receiveDepName;
    /**
     * 区域id
     */
    private List<Long> areaIds;
    private String areaName;

    /**
     * 存放地点
     */
    private String storageLocation;
    /**
     * 资产图片
     */
    private List<Long> imgs;
    /**
     * 异常类型
     */
    private Long abnormalType;
    /**
     * 异常说明
     */
    private String abnormalRemarks;
    /**
     * 异常图片
     */
    private List<String> abnormalFiles;

}
