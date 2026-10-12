package com.guhecloud.cube.srv.gen.attendance.management.server.rule.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceGroupStaffMessage {
    public static final String TOPIC = "ATTENDANCE_GROUP_STAFF";

    private Long groupId;

    private List<Long> addStaffIds;

    private List<Long> delStaffIds;
}
