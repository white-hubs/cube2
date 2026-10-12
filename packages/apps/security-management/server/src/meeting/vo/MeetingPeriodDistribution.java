package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingPeriodDistribution {
    /**
     * 小时区间 01:00,02:00
     */
    private String date;

    /**
     * 每个小时内的时间会议记录数
     */
    private Long recordCount;
}
