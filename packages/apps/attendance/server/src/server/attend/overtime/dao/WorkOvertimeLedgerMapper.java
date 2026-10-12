package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.entity.WorkOvertimeLedger;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeLedgerQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeLedgerPageResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-18  16:22
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface WorkOvertimeLedgerMapper extends BaseMapper<WorkOvertimeLedger> {
    Page<WorkOvertimeLedgerPageResult> getLedgerByPage(@Param("page") Page<WorkOvertimeLedgerPageResult> page,
                                                       @Param("param") WorkOvertimeLedgerQueryParam param
                                                      );

    List<WorkOvertimeLedgerPageResult> getLedgerByAll(@Param("param") WorkOvertimeLedgerQueryParam param);

    @Select(" SELECT user_id, " +
            " sum( work_duration ) AS work_duration, sum( work_pay_duration ) AS work_pay_duration, sum( rest_duration ) AS rest_duration, sum( rest_pat_duration ) AS rest_pat_duration, sum( fest_duration ) AS fest_duration, sum( fest_oat_duration ) AS fest_oat_duration FROM work_overtime_ledger " +
            " ${ew.customSqlSegment} "
    )
    List<WorkOvertimeLedgerPageResult> selectLedgerList(@Param(Constants.WRAPPER) Wrapper<WorkOvertimeLedger> wrapper);
}
