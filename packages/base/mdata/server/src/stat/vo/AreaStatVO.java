package com.guhecloud.cube.biz.mdata.server.stat.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lzy
 * @date 2022/4/19
 */
@Data
public class AreaStatVO {

    /**
     * 区域名称
     */
    private String name;

    /**
     * 占地面积
     */
    private BigDecimal builtUpArea;

    /**
     * 楼栋数
     */
    private Integer building;

    /**
     * 档位数
     */
    private Integer stallCounts;

    /**
     * 出租
     */
    private Integer leaseCounts;

    /**
     * 未出租
     */
    private Integer releaseCounts;

    /**
     * 商户数
     */
    private Integer supplierCounts;

    /**
     * 设备数
     */
    private Integer devicesCounts;

    /**
     * 在线
     */
    private Integer onlineCounts;

    /**
     * 离线
     */
    private Integer offlineCounts;

}
