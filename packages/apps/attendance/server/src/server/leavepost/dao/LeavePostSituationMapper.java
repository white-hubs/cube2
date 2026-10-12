package com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.dto.AttendanceStatisticsDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.query.AttendanceStatQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.dao.dto.LeavePostDurationStatDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.po.LeavePostSituationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author appet
 * @date 2022/11/21
 */
@Mapper
public interface LeavePostSituationMapper extends BaseMapper<LeavePostSituationPO> {

    /**
     * 个人离岗时长统计
     *
     * @param param
     *
     * @return
     */
    List<LeavePostDurationStatDTO> getLeavePostDurationStat(@Param("param") AttendanceStatQueryParam param);

    @Select("select * from `leave_post_situation` p " +
            "where TIMESTAMPDIFF(MINUTE, p.`end_time`, #{currentTime}) <= #{diff} " +
            "and TIMESTAMPDIFF(MINUTE, p.`end_time`, #{currentTime}) >= 0 "
    )
    List<LeavePostSituationPO> getLeavePostByDiff(@Param("currentTime") LocalDateTime currentTime, @Param("diff") int diff);

    /**
     * 离岗数据汇总(分页)
     * 考勤管理v1.5
     *
     * @param param 条件
     *
     * @return 离岗数据汇总
     */
    List<AttendanceStatisticsDTO> getWorkOvertimeSummaryPage(@Param("param") AttendanceStatQueryParam param);
}
