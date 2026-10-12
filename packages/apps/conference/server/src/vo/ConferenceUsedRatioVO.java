package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/19 10:19
 * @Description: 驾驶舱会议使用率
 */

@Data
@Builder
public class ConferenceUsedRatioVO {

    /**
     * 会议室使用率
     */
    private Float usedRatio;

    /**
     * 日环比
     */
    private Float dayOnDay;

    /**
     * 周环比
     */

    private Float weekOnWeek;
}
