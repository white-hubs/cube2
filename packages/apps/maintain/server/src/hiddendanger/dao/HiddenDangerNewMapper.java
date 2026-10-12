package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.PageVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HazardStatisticsTopVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerReportVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerTypeStatistics;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerNew;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskDetailParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepartmentHiddenDangerStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.HiddenDangerDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.StaffHiddenDangerStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wql
 * @describe 隐患排查2.0
 * @date 2023/11/8 20:41
 */
@Mapper
public interface HiddenDangerNewMapper extends BaseMapper<HiddenDangerNew> {

    List<DeviceOperationStatisticsDTO> deviceHiddenNumStatistics(@Param("param") DeviceStatisticParam param);

    List<HiddenDangerTypeStatistics> hiddenDangerTypeStatistics(@Param("period") Period period);

    List<HiddenDangerReportVO> selectHiddenDangerForSafetyReport(@Param("period") Period period);

    Long selectHiddenDangerTotal(@Param("period") Period period);

    @InterceptorIgnore(tenantLine = "on")
    List<HiddenDangerNew> selectHiddenDangerNewList(@Param("period") Period period, @Param("tenant") Integer tenant);

    Integer isFaultValid(@Param("deviceId") String deviceId, @Param("begin") String begin, @Param("end") String end);

    Page<PageVO> getHiddenDangerCount(Page<?> page, @Param("num") Integer num);

    /**
     * @Author wql
     * @Description 巡检结果统计-异常隐患明细
     * @Date 9:35 2023/4/17
     **/
    Page<HiddenDangerDetailVO> getHiddenDangerDetail(Page<?> page, @Param("param") TaskDetailParam detailParam);

    /**
     * @Author wql
     * @Description 按条件统计隐患单数量
     * @Date 15:55 2023/4/17
     **/
    Integer countHiddenDangerNum(@Param("param") TaskDetailParam taskDetailParam);

    /**
     * 按部门批量统计隐患数量
     *
     * @param depIds 部门id集合
     * @param period 时间范围
     *
     * @return 统计结果
     */
    List<DepartmentHiddenDangerStatisticsVO> countDepartmentHiddenDangerStatistics(@Param("depIds") List<Long> depIds, @Param("period") Period period);

    /**
     * 按人员批量统计隐患数量
     *
     * @param staffIds 人员id集合
     * @param period   时间范围
     *
     * @return 统计结果
     */
    List<StaffHiddenDangerStatisticsVO> countStaffHiddenDangerStatistics(@Param("staffIds") List<Long> staffIds, @Param("period") Period period);

    List<HazardStatisticsTopVO> getHazardStatisticsByType(@Param("period") Period period);

    List<HazardStatisticsTopVO> getHazardStatistics(@Param("period") Period period);
}
