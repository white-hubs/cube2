package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingApplyAppointmentTrendVO {
    /**
     * 日期 MM-DD
     */
    private String date;

    /**
     * 会议预约记录数
     */
    private Long recordCount;
}
