package com.guhecloud.cube.biz.mdata.server.stat.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2022/4/19
 */
@Data
public class AreaStallStatVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 档位编号
     */
    private String code;

    /**
     * 档位名称
     */
    private String name;

    /**
     * 档位类型
     */
    private String type;

    /**
     * 承租方式id
     */
    private Long leaseModeId;

    /**
     * 承租方式名称
     */
    private String leaseModeName;

    /**
     * 建筑面积
     */
    private String builtUpArea;

    /**
     * 实用面积
     */
    private String practicalArea;

    /**
     * 是否出租
     */
    private Boolean leased;

    /**
     * 租赁结束日期
     */
    private LocalDateTime leaseEndTime;

}
