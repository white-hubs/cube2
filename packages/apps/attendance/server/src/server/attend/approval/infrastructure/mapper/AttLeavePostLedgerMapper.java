package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttLeavePostLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result.AttLeavePostLedgerPageResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 离岗台账mapper
 */
@Mapper
public interface AttLeavePostLedgerMapper extends BaseMapper<AttLeavePostLedgerPO> {
    /**
     * 离岗台账分页列表
     *
     * @param wrapper 条件
     *
     * @return 分页列表
     */
    @Select(" select staff_id, staff_name,dept_id ,dept_name,sum(post_number) as post_number, count(post_from_id) as post_count,group_concat(post_from_id separator ',') as post_from_ids from att_leave_post_ledger " +
            " ${ew.customSqlSegment} "
    )
    Page<AttLeavePostLedgerPageResult> selectLedgerPage(Page<AttLeavePostLedgerPageResult> page,
                                                        @Param(Constants.WRAPPER) Wrapper<AttLeavePostLedgerPO> wrapper
                                                       );

    /**
     * 离岗台账列表
     *
     * @param wrapper 条件
     *
     * @return 列表
     */
    @Select(" select staff_id, staff_name,dept_id ,dept_name,sum(post_number) as post_number, count(post_from_id) as post_count,group_concat(post_from_id separator ',') as post_from_ids from att_leave_post_ledger " +
            " ${ew.customSqlSegment} "
    )
    List<AttLeavePostLedgerPageResult> selectLedgerList(@Param(Constants.WRAPPER) Wrapper<AttLeavePostLedgerPO> wrapper);

}
