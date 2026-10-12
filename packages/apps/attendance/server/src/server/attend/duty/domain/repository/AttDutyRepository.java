package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.domain.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyPO;

import java.time.LocalDateTime;
import java.util.List;

public interface AttDutyRepository {
    /**
     * 删除人员日期的值班
     *
     * @param yearMonth      月份
     * @param staffId        人员id
     * @param removeDateList 日期集合
     */
    void removeStaffDateList(String yearMonth, Long staffId, List<LocalDateTime> removeDateList);

    /**
     * 批量插入
     *
     * @param insertList 插入的集合
     */
    void insertBatchPO(List<AttDutyPO> insertList);
}
