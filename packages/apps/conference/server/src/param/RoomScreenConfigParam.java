package com.guhecloud.cube.srv.gen.conferencemanage.server.param;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.DisplayStatusEnum;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/10 15:50
 * @Description: 会议屏配置参数
 */

@Data
public class RoomScreenConfigParam {
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
    private DisplayStatusEnum displayStatus;

    /**
     * 发送节目任务id
     */
    private Long jobId;

    /**
     * cron表达式
     */
    private Long cronExpression;

    /**
     * 节目接口地址
     */
    private String programUrl;

    /**
     * 鉴权信息
     */
    private String authInfo;

}
