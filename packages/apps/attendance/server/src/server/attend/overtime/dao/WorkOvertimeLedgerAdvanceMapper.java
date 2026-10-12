package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.entity.WorkOvertimeLedgerAdvance;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeLedgerPageResult;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-12  10:48
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface WorkOvertimeLedgerAdvanceMapper extends BaseMapper<WorkOvertimeLedgerAdvance> {
    List<WorkOvertimeLedgerPageResult> getWorkOvertimeLedgerByUserIds(@Param("userIds") Set<Long> userIds, @Param("period") Period period);
}
