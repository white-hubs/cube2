package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAwayBusinessLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result.AttAwayBusinessLedgerPageResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 出差台账
 */
@Mapper
public interface AttAwayBusinessLedgerMapper extends BaseMapper<AttAwayBusinessLedgerPO> {

    /**
     * 出差台账列表（时长）
     *
     * @param wrapper 条件
     *
     * @return 列表
     */
    @Select(" select staff_id, staff_name,dept_id ,dept_name,sum(away_number) as away_number,count(away_from_id) as away_count,group_concat(away_from_id separator ',') as away_from_ids from att_away_business_ledger " +
            " ${ew.customSqlSegment} "
    )
    List<AttAwayBusinessLedgerPageResult> selectLedgerList(@Param(Constants.WRAPPER) Wrapper<AttAwayBusinessLedgerPO> wrapper);
}
