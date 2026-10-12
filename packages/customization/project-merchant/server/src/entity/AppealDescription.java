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
 * 诉求描述表
 * </p>
 *
 * @author lzy
 * @since 2021-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("appeal_description")
public class AppealDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 编号
     */
    private String code;

    /**
     * 描述名称
     */
    private String name;

    /**
     * 诉求类型
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
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
