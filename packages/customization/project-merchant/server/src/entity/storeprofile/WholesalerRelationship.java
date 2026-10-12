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
 * 
 * </p>
 *
 * @author wangliang
 * @since 2022-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wholesaler_relationship")
public class WholesalerRelationship implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 任务类型，可能是问卷类型，可能是协议类型
     */
    private String type;

    /**
     * 档位ID
     */
    private String stallId;

    /**
     * 任务ID，可能是问卷ID可能是协议ID
     */
    private Long businessId;

    /**
     * 签约状态
     */
    private Integer status;

    /**
     * 签约人
     */
    private String signedWho;

    /**
     * 签约时间
     */
    private LocalDateTime sigendTime;

    /**
     * 是否档主
     */
    private Integer owner;
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

}
