package com.guhecloud.cube.srv.gen.attendance.management.server.leave.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.valueobject.LeaveStatus;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("leave_form")
public class LeaveFormPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String leaveFormDetail;
    private String leaveRule;
    private LeaveStatus status;
    private LocalDateTime leaveStartTime;
    private LocalDateTime leaveEndTime;
}
