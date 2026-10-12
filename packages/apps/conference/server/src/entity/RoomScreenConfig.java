package com.guhecloud.cube.srv.gen.conferencemanage.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会议屏配置
 *
 * @TableName room_screen_config
 */
@EqualsAndHashCode(callSuper = true)
@TableName("room_screen_config")
@Data
public class RoomScreenConfig extends BasicEnhancedTenantEntity<Long> {
    /**
     *
     */
    private Long id;

    /**
     * 会议室id
     */
    private Long conferenceId;

    /**
     * 设备
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 展示状态
     */
    private String displayStatus;

    /**
     * 节目接口地址
     */
    private String programUrl;

    /**
     * 鉴权信息
     */
    private String authInfo;

    /**
     * 节目刷新时间 秒
     */

    private Integer cronExpression;

    /**
     * 节目id
     */
    private String programId;

    /**
     * 厂商编码
     */
    private String manufacturerCode;
}