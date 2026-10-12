package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingAttendedTrendVO {

    /**
     * 本周 MM-DD，本月 *周，本年 *月
     */
    private String date;

    /**
     * 会见记录数
     */
    private Long recordCount;
}
