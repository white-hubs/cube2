package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.beforestat.application.dto.AttRepairSimpleInfoDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.WorkOmitParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.WorkOmitResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-01  09:58
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface AttApproveLogMapper extends BaseMapper<AttApproveLogPO> {
    Integer getActualDuration(@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime
                             );

    Page<WorkOmitResult> getWorkOmitByPage(@Param("page") Page<WorkOmitResult> page, @Param("param") WorkOmitParam param);

    Page<WorkOmitResult> getWorkOmitByPageAndGroup(@Param("page") Page<WorkOmitResult> page, @Param("param") WorkOmitParam param);

    List<WorkOmitResult> getWorkOmitExcle(@Param("param") WorkOmitParam param);

    List<WorkOmitResult> getWorkOmitByPageAndGroupExcle(@Param("param") WorkOmitParam param);

    @Select(" select user_id , count(1) as num from att_approve_log " +
            " ${ew.customSqlSegment} " +
            " group by user_id "
    )
    List<WorkOmitResult> getRepairCardCountStatList(@Param(Constants.WRAPPER) Wrapper<AttApproveLogPO> wrapper);

    /**
     * 获取简单的补卡统计信息
     *
     * @return 集合
     */
    @Select(" SELECT a.user_id,b.require_clock_in_time as repair_time,b.clock_type FROM att_approve_log a " +
            " left join att_clock_in_records b on a.clock_in_id=b.id " +
            " ${ew.customSqlSegment} "
    )
    List<AttRepairSimpleInfoDTO> getRepairSimpleInfo(@Param(Constants.WRAPPER) Wrapper<AttApproveLogPO> wrapper);
}
