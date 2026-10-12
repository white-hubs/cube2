package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "device_type")
public class DeviceType {

    private String id;

    private String pid;

    private String code;

    private String name;

    private Integer validityMonths;

    private Integer sort;

    private Boolean deleted;
}
