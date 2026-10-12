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
 * 设备（智能视频分析盒）
 * </p>
 *
 * @author zwb
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("device_videobox")
public class DeviceVideobox implements Serializable {

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
     * 设备类型外键(数据字典)
     */
    private String typeId;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称 
     */
    private String name;

    /**
     * 生产时间
     */
    private LocalDateTime productionDate;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 规格型号
     */
    private String specification;

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
     * 设备实景
     */
    private String liveAction;

    /**
     * 采购合同
     */
    private String purchaseContract;

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
