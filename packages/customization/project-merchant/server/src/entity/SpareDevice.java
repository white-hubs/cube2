package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ZWB（zwbhaover@foxmail.com）
 * @version 1.0
 */

@Data
@TableName(value = "spare_device")
public class SpareDevice {

    /** ID */
    private String id ;
    /** 备件ID */
    private String spareId ;
    /** 设备ID */
    private String deviceId ;
}
