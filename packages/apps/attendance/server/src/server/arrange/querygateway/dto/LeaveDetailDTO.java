package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.dto;

import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity.LeaveFormDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveDetailDTO {
    private Long id;
    /**
     * 请假类型
     */
    private String leaveType;

    private LeaveFormDetail.LeaveTime leaveTime;
}
