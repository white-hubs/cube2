package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "project")
public class Project {

    private String id;

    private String code;

    private String name;

    private String abbr;

    private String pinyin;

    private String longitude;

    private String latitude;

    private Boolean enabled;

    private String remark;

    private String provinceCode;

    private String cityCode;

    private String address;

    private String url;
    
    private String api;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime launchDate;

    private Boolean launched;
    
    private String introduction;

    private String figures;

    private int sort;

    private int acreage;

    private String copyright;

    private String model;

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
    private Integer tenant;

}
