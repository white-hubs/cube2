package com.guhecloud.cube.srv.gen.device.supervise.server.account.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.Deletable;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 设备收藏
 * </p>
 *
 * @author zwb
 * @since 2022-07-05
 */
@Data
@TableName("device_account_collect")
public class DeviceAccountCollectPO implements Deletable<Long> {
    /**
     * 设备实例ID
     */
    private Long id;

    /**
     * 租户编号
     */
    private Integer tenant;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 是否删除
     */
    private Boolean deleted;
    /**
     * 创建时间
     */
    private LocalDateTime time;

}
