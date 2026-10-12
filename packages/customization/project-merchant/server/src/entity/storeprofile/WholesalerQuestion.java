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
@TableName("wholesaler_question")
public class WholesalerQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 问卷模板ID
     */
    private String questionnaireTempId;

    /**
     * 问题类型
     */
    private String type;

    /**
     * 题号
     */
    private Integer number;

    /**
     * 题干
     */
    private String name;

    /**
     * 选项
     */
    private String options;

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
