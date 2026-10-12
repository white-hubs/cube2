package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 食安抽检
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "meattrade_spot_check")
public class SpotCheck extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 报备主键
     */
    private Long porkReportId;

    /**
     * 报备编号
     */
    private String porkReportCode;

    /**
     * 抽检员主键
     */
    private Long inspectorId;

    /**
     * 实际抽检片数
     */
    private Integer actualCheckSlices;

    /**
     * 抽检比例
     */
    private Float requiredCheckRatio;

    /**
     * 规定抽检片数
     */
    private Integer requiredCheckSlices;

    /**
     * 瘦肉精
     */
    private SpotCheckEnum clenbuterol;

    /**
     * 含水量
     */
    private SpotCheckEnum watered;

    /**
     * 是否抽检
     */
    private Boolean status;

    /**
     * 抽检时间
     */
    private LocalDateTime spotCheckTime;
    /**
     * 备注
     */
    private String remark;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
