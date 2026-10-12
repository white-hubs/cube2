package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.message;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceClockInMessage {
    public static final String TOPIC = "ATTENDANCE_CLOCK_IN";
    /**
     * 打卡信息
     */
    private AttClockInRecordsPO clockInfo;
}
