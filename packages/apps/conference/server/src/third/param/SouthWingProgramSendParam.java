package com.guhecloud.cube.srv.gen.conferencemanage.server.third.param;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/15 9:55
 * @Description:
 */
@Data
public class SouthWingProgramSendParam {

    /**
     * 节目id
     */
    private String programId;

    /**
     * 节目名称
     */
    private String name;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 设备id
     */
    private String deviceId;

}
