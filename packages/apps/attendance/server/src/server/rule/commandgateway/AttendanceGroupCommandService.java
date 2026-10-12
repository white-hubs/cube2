package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;

/**
 * @author bq
 * @date 2022/11/14
 * @description
 */
public interface AttendanceGroupCommandService extends IService<AttendanceGroupPO> {

    /**
     * 新增或修改考勤数据
     * 新增为草稿版
     * 修改只在草稿版
     *
     * @param command
     *
     * @return
     */
    Long saveOrUpdate(SubmitAttendanceGroupCommand command);

    void delete(Long id);

}
