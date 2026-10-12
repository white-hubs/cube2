package com.guhecloud.cube.deliver.project.merchant.entity.storeprofile;

import com.baomidou.mybatisplus.annotation.*;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
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
 * @since 2022-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wholesaler_contract")
public class WholesalerContract implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 主键ID
     */
    private String projectId;

    /**
     * 合同编号
     */
    private String code;

    /**
     * 签约方式（0新签约，1续签）
     */
    private Integer signingWay;

    /**
     * 合同类型ID
     */
    private String contractTypeId;

    /**
     * 合同状态（0草稿 1乙方签约 2签章（生效））
     */
    private Integer status;

    /**
     * 租赁日期始
     */
    private LocalDateTime beginTime;

    /**
     * 租赁日期终
     */
    private LocalDateTime endTime;

    /**
     * 备注
     */
    private String remark;

    private String signatoryId;

    private String signatoryName;

    private LocalDateTime signatoryTime;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String paperCode;

    private String oldId;

    private String oldCode;

}
