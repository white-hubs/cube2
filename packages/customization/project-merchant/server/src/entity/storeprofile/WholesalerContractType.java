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
 * @since 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wholesaler_contract_type")
public class WholesalerContractType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;
    /**
     * 编码
     */
    private String code;

    private String projectId;

    /**
     * 是否启用（1启用，0没有启用）
     */
    private Integer enabled;

    /**
     * 合同类型名称
     */
    private String name;

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
