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
 * @since 2022-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wholesaler_questionnaire_temp")
public class WholesalerQuesTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
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
     * 问卷模板名称
     */
    private String name;

    /**
     * 问卷模板类型
     */
    private String type;

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
