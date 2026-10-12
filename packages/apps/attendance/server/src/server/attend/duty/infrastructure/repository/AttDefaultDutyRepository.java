package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.domain.repository.AttDutyRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.mapper.AttDutyMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttPoAttributeSet;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttDefaultDutyRepository implements AttDutyRepository {

    private final AttDutyMapper attDutyMapper;

    /**
     * 删除人员日期的值班
     *
     * @param yearMonth      月份
     * @param staffId        人员id
     * @param removeDateList 日期集合
     */
    @Override
    public void removeStaffDateList(String yearMonth, Long staffId, List<LocalDateTime> removeDateList) {
        attDutyMapper.remove(DateUtils.toTimestamp(LocalDateTime.now()), Wrappers.<AttDutyPO>lambdaQuery()
                                     .eq(AttDutyPO::getStaffId, staffId)
                                     .eq(AttDutyPO::getDutyMonth, yearMonth)
                                     .in(AttDutyPO::getDutyDate, removeDateList)
                            );
    }

    /**
     * 批量插入
     *
     * @param insertList 插入的集合
     */
    @Override
    public void insertBatchPO(List<AttDutyPO> insertList) {
        if (insertList == null || insertList.isEmpty()) {
            return;
        }
        for (AttDutyPO po : insertList) {
            AttPoAttributeSet.initAddBasePoAttribute(po);
            attDutyMapper.insert(po);
        }
    }
}
