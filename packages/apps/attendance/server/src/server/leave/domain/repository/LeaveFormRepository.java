package com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.aggregate.LeaveForm;

import java.util.Optional;

/**
 * @author pengjian
 * @date 2022/11/18 19:19
 * @description 请假单仓储
 */
public interface LeaveFormRepository {

    Long save(LeaveForm leaveForm);

    Optional<LeaveForm> getById(Long id);
}
