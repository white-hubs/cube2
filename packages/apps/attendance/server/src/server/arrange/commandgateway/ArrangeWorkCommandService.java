package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.command.SubmitArrangeAndDutyWorkCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.command.WorkDateCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupStaffCommand;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/16
 * @description
 */
public interface ArrangeWorkCommandService extends IService<ArrangeWorkPO> {

    /**
     * 手动排班
     *
     * @param submitArrangeAndDutyWorkCommand
     */
    void manualArrange(SubmitArrangeAndDutyWorkCommand submitArrangeAndDutyWorkCommand);

    /**
     * 保存一个员工一天的排班数据
     */
    boolean saveOrUpdateOneStaffOneDay(Long groupId, WorkDateCommand data, SubmitAttendanceGroupStaffCommand staff,
                                       List<String> newWorkClassesIds, Boolean isSaveLog
                                      );

    /**
     * 删除员工某天的排班数据
     *
     * @param date    时间
     * @param staffId 员工ID
     */
    void removeData(LocalDateTime date, Long staffId);

}
