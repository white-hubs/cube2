package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.AcreageDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "area")
public class AreaDeliver extends AcreageDTO {

    private String id;

    private String pid;

    private String projectId;

    private String bimId;

    private String code;

    private String name;

    private Double acreage;

    private String level;

    private String type;

    private Integer sort;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted=false;

}
