package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-22  09:40
 * @Description:
 * @Version: 1.0
 */
public interface AttStaffClockRepository {
    /**
     * 生成打卡任务
     *
     * @param user
     * @param classes
     * @param classDate
     * @param schedulingId
     *
     * @return
     */
    List<AttClockInRecordsPO> insertAttClockInRecords(UserDTO user, AttClassesRecordPO classes, LocalDateTime classDate, Long schedulingId);
}
