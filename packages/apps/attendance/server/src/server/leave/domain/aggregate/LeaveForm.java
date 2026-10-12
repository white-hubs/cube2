package com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.aggregate;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity.LeaveFormDetail;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.valueobject.LeaveStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/11/18 17:26
 * @description 请假单
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveForm {
    private Long id;

    /**
     * 请假单编号
     */
    @Setter
    private String code;

    /**
     * 请假单明细
     */
    private LeaveFormDetail detail;

    /**
     * 请假单提交时间，默认为当前时间
     */
    @Setter
    private LocalDateTime submitTime;

    /**
     * 请假规则
     */
    @Setter
    private LeaveRule leaveRule;

    /**
     * 请假单状态
     */
    private LeaveStatus status;

    public LeaveForm(LeaveFormDetail detail, LeaveRule leaveRule) {
        this.id = IdWorker.getId();
        this.code = "";
        this.detail = detail;
        this.leaveRule = leaveRule;
        this.status = LeaveStatus.APPLYING;
    }

    public void changeStatus(LeaveStatus status) {
        this.status = status;
    }
}
