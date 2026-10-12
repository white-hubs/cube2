package com.guhecloud.cube.deliver.project.merchant.entity.asset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *  资产
 * </p>
 *
 * @author bq
 * @since 2022-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("asset")
public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
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
     * 一级区域ID
     */
    private String areaId1;

    /**
     * 二级区域ID
     */
    private String areaId2;

    /**
     * 三级区域ID
     */
    private String areaId3;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型ID
     */
    private String typeId;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 来源
     */
    private String source;

    /**
     * 规格型号
     */
    private String model;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 产品序列号
     */
    private String serialNumber;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 生产日期
     */
    private LocalDateTime productionDate;

    /**
     * 价格
     */
    private Double price;

    /**
     * 购买日期
     */
    private LocalDateTime buyDate;

    /**
     * 使用年限(单位：月)
     */
    private String useYear;

    /**
     * 到期时间（生产日期+使用年限）
     */
    private LocalDateTime expireDate;

    /**
     * 使用部门ID
     */
    private String useDeptId;

    /**
     * 使用部门名称
     */
    private String useDeptName;

    /**
     * 使用人ID
     */
    private String userId;

    /**
     * 使用人名称
     */
    private String username;

    /**
     * 使用状态
     */
    private String status;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 联系人员
     */
    private String contactPerson;

    /**
     * 联系方式
     */
    private String contactPhone;

    /**
     * 联系地址
     */
    private String contactAddress;

    /**
     * 维保到期时间
     */
    private LocalDateTime maintExpireDate;

    /**
     * 备注
     */
    private String remark;

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
     * 删除标志(0代表存在，1代表已删除)
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;


}
