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
 * 常见问题解答
 * </p>
 *
 * @author benhuabao
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("faq")
public class Faq implements Serializable {

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
     * 常见问题
     */
    private String question;

    /**
     * 类型
     */
    private String type;

    /**
     * 答案
     */
    private String answer;

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


    private LocalDateTime createDay;
    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;


}
