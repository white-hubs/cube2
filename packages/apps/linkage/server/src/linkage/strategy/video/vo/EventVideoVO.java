package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.vo;

import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteModeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.VideoActionTypeEnum;
import lombok.Data;

/**
 * 事件联动摄像机设备数据模型
 *
 * @author lanchun
 * @date 2022/6/21 13:44
 */
@Data
public class EventVideoVO {

    private Long id;

    /**
     * 事件关联预案主键
     */
    private Long eventLinkageId;

    /**
     * 事件主键
     */
    private Long eventId;

    /**
     * 联动预案主键
     */
    private Long linkageId;

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 摄像机token
     */
    private String token;

    /**
     * 动作类型
     */
    private VideoActionTypeEnum type;

    /**
     * 动作时长
     */
    private Integer time;

    /**
     * 动作时长单位
     */
    private String timeUnit;

    /**
     * 间隔时间
     */
    private Integer intervalTime;

    /**
     * 持续次数
     */
    private Integer durationTime;

    /**
     * 执行方式
     */
    private ExecuteModeEnum executeMode;

}
