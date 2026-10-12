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
@TableName("wholesaler_questionnaire")
public class WholesalerQuestionnaire implements Serializable {

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
     * 编码
     */
    private String code;

    /**
     * 问卷名称
     */
    private String name;

    /**
     * 模板id
     */
    private String tempId;

    /**
     * 模板类型
     */
    private String tempType;

    /**
     * 模板类型
     */
    private String tempName;

    /**
     * 状态（是否有效）
     */
    private String status;

    /**
     * 描述
     */
    private String description;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * xxl的任务ID
     */
    private String xxlTaskIds;

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
