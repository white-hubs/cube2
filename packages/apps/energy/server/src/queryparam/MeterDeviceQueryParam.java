package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lrj
 * @Description 仪表管理
 * @date 2022-07-04 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeterDeviceQueryParam extends Paging {

    //是否报警
    private Boolean isAlarm;
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
     * 仪表编码
     */
    private String code;
    /**
     * 通用查询字段
     */
    private String keyword;
    //读数
    private BigDecimal readeValue;

    private String period;
    //区域id
    private String area;
    LocalDateTime begin = null;
    LocalDateTime end = null;

}