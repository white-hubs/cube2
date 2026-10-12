package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备离线记录表
 * </p>
 *
 * @author bq
 * @since 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("device_online_record")
public class DeviceOnlineRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 上线/下线
     */
    private int online;

    /**
     * 上下线时间
     */
    private LocalDateTime date;

}
