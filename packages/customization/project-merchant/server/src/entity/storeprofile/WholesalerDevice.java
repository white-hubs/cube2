package com.guhecloud.cube.deliver.project.merchant.entity.storeprofile;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商户档案-所有设备（批发商的设备）
 * </p>
 *
 * @author wangliang
 * @since 2022-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wholesaler_device")
public class WholesalerDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 设备类型Id
     */
    private String typeId;

    /**
     * 设备数量
     */
    private Integer count;

    /**
     * 所在档位ID
     */
    private String stallId;

    /**
     * 档位名称
     */
    private String stallName;

    /**
     * 所属商户ID
     */
    private String companyId;

    /**
     * 商户名称
     */
    private String companyName;

    /**
     * 设备描述
     */
    private String description;

    /**
     * 是否危险物品
     */
    private Integer danger;

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
