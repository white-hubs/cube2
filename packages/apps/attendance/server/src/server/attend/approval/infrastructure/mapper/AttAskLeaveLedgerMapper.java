package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result.AttAskLeaveLedgerPageDateResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 请假台账mapper
 */
@Mapper
public interface AttAskLeaveLedgerMapper extends BaseMapper<AttAskLeaveLedgerPO> {

    @Select(" select staff_id,holiday_id as holiday_rule_id,sum(leave_number) as leave_number,group_concat(leave_from_id separator ',') as leave_from_ids from att_ask_leave_ledger " +
            " ${ew.customSqlSegment} " +
            " group by staff_id,holiday_id "
    )
    List<AttAskLeaveLedgerPageDateResult> selectLedgerList(@Param(Constants.WRAPPER) Wrapper<AttAskLeaveLedgerPO> wrapper);
}
