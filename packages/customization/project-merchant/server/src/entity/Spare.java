package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "spare")
public class Spare {

    /**
     * ID
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型ID
     */
    private String typeId;

    /**
     * 类型Code
     */
    private String typeCode;

    /**
     * 厂家
     */
    private String factory;

    /**
     * 型号
     */
    private String model;

    /**
     * 库存;数量
     */
    private Integer num;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 价格
     */
    private Double price;

    /**
     * 数量过低预警值;库存最小
     */
    private Integer estimateLow;

    /**
     * 数量过高预警值;库存最大
     */
    private Integer estimateHigh;

    /**
     * 描述
     */
    private String description;

    /**
     * 地址
     */
    private String address;

    /**
     * 添加时间
     */
    private LocalDateTime time;

    /**
     * 是否有详情
     */
    private Boolean hasDetails;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 状态
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;
}
