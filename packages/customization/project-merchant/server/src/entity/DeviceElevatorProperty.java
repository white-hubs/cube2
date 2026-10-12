package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 电梯设备特有属性
 * </p>
 *
 * @author bq
 * @since 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("device_elevator_property")
public class DeviceElevatorProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 层数
     */
    private Integer floorNum;

    /**
     * 站数
     */
    private Integer stationNum;

    /**
     * 门数
     */
    private Integer doorNum;

    /**
     * 载重
     */
    @TableField("`load`")
    private Integer load;

    /**
     * 速度
     */
    private Float speed;

    /**
     * 年检日期
     */
    private LocalDateTime yearInspectDate;


}
