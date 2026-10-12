package com.guhecloud.cube.srv.gen.attendance.management.server.clock.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.dao.dto.ClassesClockStatDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.interfaces.vo.UserClockDayVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.po.ClassesClockPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.querygateway.query.ClockQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.dto.AttendanceStatisticsDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.query.AttendanceStatQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author appet
 * @date 2022/11/21
 */
@Mapper
public interface ClassesClockMapper extends BaseMapper<ClassesClockPO> {
    /**
     * 获取用户一天的打卡情况
     *
     * @param userId    用户ID
     * @param clockDate 打卡时间
     *
     * @return
     */
    List<UserClockDayVO> getUserClockByDay(@Param("userId") Long userId, @Param("clockDate") String clockDate);

    /**
     * 打卡统计
     *
     * @param param
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<ClassesClockStatDTO> getClassesClockStat(@Param("param") AttendanceStatQueryParam param);

    /**
     * 打卡分页
     *
     * @param page  分页参数
     * @param param 查询参数
     *
     * @return
     */
    //    @InterceptorIgnore(tenantLine = "true")
    Page<ClassesClockPO> getPage(Page page, @Param("param") ClockQueryParam param, @Param("tenant") Integer tenant);

    //    @InterceptorIgnore(tenantLine = "true")
    List<ClassesClockPO> getPage(@Param("param") ClockQueryParam param, @Param("tenant") Integer tenant);

    /**
     * 打卡数据汇总
     * 员工考勤统计v1.5
     *
     * @param param
     *
     * @return
     */
    List<AttendanceStatisticsDTO> getClockDataSummaryPage(@Param("param") AttendanceStatQueryParam param);

    /**
     * 打卡数据统计 视图
     *
     * @param param 统计条件
     *
     * @return 统计数据
     */
    List<AttendanceStatisticsDTO> selectAttendanceSummary(@Param("param") AttendanceStatQueryParam param);

    List<ClassesClockPO> getAll(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,
                                @Param("userId") Long userId
                               );

    List<ClassesClockPO> getAllUser(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
