package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lrj
 * @Description 仪表管理
 * @date 2022-07-04 15:12:36
 */
@Data
public class MeterDeviceVOV1 {

    //
    private Long id;
    //设备区域id
    private String deviceAreaId;
    //设备区域
    private String deviceArea;
    //上级设备
    private String parentName;
    //上级设备id
    private String parentId;
    //上期用量
    private BigDecimal lastReadeValue;
    //本期用量
    private BigDecimal nowReadeValue;
    //本次抄表时间
    private LocalDateTime nowReadeTime;
    //上期抄表时间
    private LocalDateTime lastReadeTime;
    //设备类型id
    private String deviceTypeId;
    //设备名称
    private String deviceName;
    //设备类型
    private String deviceType;
    //设备id
    private String deviceId;
    /**
     * 仪表类型
     */
    private String instrumentType;
    /**
     * 仪表类型中文名
     */
    private String instrumentName;
    /**
     * 设备编码
     */
    private String code;
    //读数
    private BigDecimal readeValue;

    private List<MeterDeviceVOV1> children;

    //趋势单表趋势
    private List<DataTimeFlagVo> dataTimeFlagVos;
    //本期值
    private BigDecimal nowNum = new BigDecimal("0.00");
    //上期期值
    private BigDecimal lastNum = new BigDecimal("0.00");

    //趋势单表历史
    private List<DataVo> deviceHistorys = new ArrayList<>();

}