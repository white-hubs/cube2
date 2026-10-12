package com.guhecloud.cube.srv.gen.attendance.management.server.overtime.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.dto.AttendanceStatisticsDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.query.AttendanceStatQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.dao.dto.OvertimeDurationGroupByTypeDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.dao.dto.WorkOvertimeStatForFeeDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.po.WorkOvertimeSituationPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.querygateway.query.OvertimeForFeeStatParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.querygateway.query.OvertimeSituationParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author appet
 * @date 2022/11/21
 */
@Mapper
public interface WorkOvertimeSituationMapper extends BaseMapper<WorkOvertimeSituationPO> {

    /**
     * 加班费时长统计
     *
     * @param page
     * @param param
     *
     * @return
     */
    Page<WorkOvertimeStatForFeeDTO> getWorkOvertimeStatForFee(Page page, @Param("param") OvertimeForFeeStatParam param);

    List<WorkOvertimeStatForFeeDTO> getWorkOvertimeStatForFee(@Param("param") OvertimeForFeeStatParam param);

    /**
     * 查询加班时长
     *
     * @param param
     *
     * @return
     */
    Float getOvertimeDurationStat(@Param("param") OvertimeSituationParam param);

    /**
     * 个人的加班时长统计
     *
     * @param param
     *
     * @return
     */
    List<OvertimeDurationGroupByTypeDTO> getOvertimeDurationStatGroupByType(@Param("param") AttendanceStatQueryParam param);

    /**
     * 加班数据汇总
     *
     * @param param 条件
     *
     * @return 加班统计结果
     */
    List<AttendanceStatisticsDTO> getWorkOvertimeSummaryPage(@Param("param") AttendanceStatQueryParam param);
}
