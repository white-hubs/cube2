package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ZWB（zwbhaover@foxmail.com）
 * @version 1.0
 */

@Data
@TableName(value = "spare_type")
public class SpareType {
    /** ID */
    private String id;
    /** 父ID */
    private String pid;
    /** 项目ID */
    private String projectId;
    /** 编码 */
    private String code;
    /** 名称 */
    private String name;
    /** 描述 */
    private String description;
    /** 排序 */
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;
}
