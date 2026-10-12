package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.AcreageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("space")
public class Space extends AcreageDTO {

    private String id;

    private String projectId;

    private String areaId1;

    private String areaId2;

    private String areaId3;

    private String bimId;

    private String deptId;

    private String deptName;

    private String companyId;

    private String companyName;

    private String code;

    private String name;

    private String type;

    private String attribute;

    private String tradeModel;

    private Double acreage;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;
    private String sceneCode;
}
