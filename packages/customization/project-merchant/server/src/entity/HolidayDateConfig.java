package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

//@Data
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "holiday_date_config")
public class HolidayDateConfig {

    private String id;

    private String holidayConfigId;
    
    private String projectId;

    private LocalDateTime holidayTime;

    private String holidayType;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;
}
