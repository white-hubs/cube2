package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 备品备件使用记录中使用的备件
 * </p>
 *
 * @author zwb
 * @since 2021-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("spare_use_spare")
public class SpareUseSpare implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 备件使用记录ID
     */
    private String spareUseId;

    /**
     * 备件ID
     */
    private String spareId;

    /**
     * 备件名称
     */
    private String spareName;

    /**
     * 备件类型ID
     */
    private String typeId;

    /**
     * 备件类型Code
     */
    private String typeCode;

    /**
     * 备件类型名称
     */
    private String typeName;

    /**
     * 备件厂家
     */
    private String factory;

    /**
     * 备件型号
     */
    private String model;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 使用数量
     */
    private Integer useNum;

    /**
     * 时间
     */
    private LocalDateTime time;


}
