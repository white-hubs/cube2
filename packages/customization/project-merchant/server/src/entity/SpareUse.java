package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 备品备件使用记录
 * </p>
 *
 * @author zwb
 * @since 2021-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("spare_use")
public class SpareUse implements Serializable {

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
     * 维保公司
     */
    private String miantFirm;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备类型ID
     */
    private String deviceTypeId;

    /**
     * 设备类型code
     */
    private String deviceTypeCode;

    /**
     * 设备类型名称
     */
    private String deviceTypeName;

    /**
     * 一级区域
     */
    private String areaId1;

    /**
     * 二级区域
     */
    private String areaId2;

    /**
     * 三级区域
     */
    private String areaId3;

    /**
     * 安装位置
     */
    private String installAddress;

    /**
     * 用途
     */
    private String purpose;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务ID
     */
    private String businessKey;

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


}
