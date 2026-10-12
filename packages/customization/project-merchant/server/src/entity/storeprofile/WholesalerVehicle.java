package com.guhecloud.cube.deliver.project.merchant.entity.storeprofile;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商户档案-所有车辆（批发商交通工具表）
 * </p>
 *
 * @author wangliang
 * @since 2021-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wholesaler_vehicle")
public class WholesalerVehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 项目ID
     */
    @NotBlank
    @NotNull
    private String projectId;

    /**
     * 车牌号（三轮车无车牌）
     */
    @NotBlank
    @NotNull
    private String vehiclePlate;

    /**
     * 类型
     */
    @NotBlank
    @NotNull
    private String vehicleType;

    /**
     * 颜色
     */
    private String color;

    /**
     * 所属人ID
     */
    @NotBlank
    @NotNull
    private String owner;

    /**
     * 所属人电话（巡逻的人要根据电话找人）
     */
    @NotBlank
    @NotNull
    private String mobile;

    /**
     * 档位
     */
    @NotBlank
    @NotNull
    private String stallId;

    /**
     * 档位名称
     */
    private String stallName;

    /**
     * 商户
     */
    private String companyId;

    /**
     * 商户名称
     */
    private String companyName;

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
     * 删除标识
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted=false;


}
