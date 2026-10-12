package com.guhecloud.cube.srv.gen.attendance.management.server.common;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAskLeaveFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAwayBusinessFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttCancelLeaveFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttLeavePostFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.po.ClassesChangeSituationPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.po.WorkOvertimeSituationPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.repairclock.po.RepairClockSituationPO;
import lombok.Getter;

/**
 * 补卡类型枚举
 *
 * @author appet
 * @date 2022/11/28
 */
@Getter
public enum ApprovalBizEnum {
    REPAIR_CLOCK("补卡申请", RepairClockSituationPO.class),
    OVERTIME("加班申请", WorkOvertimeSituationPO.class),
    CHANGE_CLASSES("换班申请", ClassesChangeSituationPO.class),
    LEAVE("请假申请", AttAskLeaveFromDTO.class),
    CANCEL_LEAVE("销假申请", AttCancelLeaveFromDTO.class),
    LEAVE_THE_POST("离岗申请", AttLeavePostFromDTO.class),
    AWAY_BUSINESS("出差申请", AttAwayBusinessFromDTO.class),
    ;

    private final String desc;
    private final Class<?> clazz;

    ApprovalBizEnum(String desc, Class<?> clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }

}
