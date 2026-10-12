package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "flow")
public class Flow {

    private String id;

    private String projectId;

    private String deptId;

    private String category;

    private String name;

    private String code;

    @TableField("`key`")
    private String key;

    private Boolean enabled;

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
