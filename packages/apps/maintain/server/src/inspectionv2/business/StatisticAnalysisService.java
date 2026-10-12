package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

import cn.hutool.poi.excel.ExcelWriter;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DepartmentInspectionParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionNumParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionObjQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionResultParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.ResultRunInHoleParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskStandardAbnormalRateParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskDetailParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.CheckTheMismatchDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepInspectionDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepInspectionStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DeviceInspectionVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.HiddenDangerDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionResultDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionResultVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.StaffInspectionStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TaskStandardAbnormalRateVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TroubleListDetailVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @author wql
 * @describe 统计分析
 * @date 2023/4/14 14:46
 */
public interface StatisticAnalysisService {
    /**
     * @Author wql
     * @Description 巡检结果统计
     * @Date 14:52 2023/4/14
     **/
    Pagination<InspectionResultVO> statisticalResult(InspectionResultParam resultParam);

    /**
     * @Author wql
     * @Description 稽查不匹配项明细
     * @Date 17:41 2023/4/14
     **/
    Pagination<CheckTheMismatchDetailVO> checkDetail(ResultRunInHoleParam resultParam);

    /**
     * @Author wql
     * @Description 巡检结果分析-隐患记录
     * @Date 8:48 2023/4/17
     **/
    Pagination<HiddenDangerDetailVO> getHiddenDangerDetail(TaskDetailParam detailParam);

    /**
     * @Author wql
     * @Description 巡检结果分析-隐患记录导出
     * @Date 9:49 2023/4/17
     **/
    ExcelWriter getAllHiddenDangerDetail(TaskDetailParam detailParam);

    /**
     * @Author wql
     * @Description 报修记录明细
     * @Date 10:46 2023/4/17
     **/
    Pagination<TroubleListDetailVO> repairsDetail(ResultRunInHoleParam resultParam);

    /**
     * @Author wql
     * @Description 导出报修明细
     * @Date 11:09 2023/4/17
     **/
    ExcelWriter repairsDetailExport(ResultRunInHoleParam resultParam);

    /**
     * @Author wql
     * @Description 部门巡检统计
     * @Date 11:38 2023/4/17
     **/
    Pagination<DepInspectionStatisticsVO> departmentStatistics(DepartmentInspectionParam depParam);

    /**
     * @Author wql
     * @Description 任务明细
     * @Date 16:05 2023/4/17
     **/
    Pagination<InspectionResultDetailVO> taskDetail(TaskDetailParam detailParam);

    /**
     * @Author wql
     * @Description 任务明细导出
     * @Date 17:14 2023/4/17
     **/
    ExcelWriter taskDetailExport(TaskDetailParam detailParam);

    /**
     * @Author wql
     * @Description 人员巡检统计
     * @Date 18:52 2023/4/17
     **/
    Pagination<StaffInspectionStatisticsVO> staffStatistics(DepartmentInspectionParam depParam);

    /**
     * @Author wql
     * @Description 巡检记录明细
     * @Date 20:05 2023/4/17
     **/
    Pagination<DepInspectionDetailVO> inspectionDetail(InspectionNumParam inspectionNumParam);

    /**
     * @Author wql
     * @Description 巡检记录明细导出
     * @Date 20:25 2023/4/17
     **/
    ExcelWriter inspectionDetailExport(InspectionNumParam inspectionNumParam);

    /**
     * @Author wql
     * @Description 巡检对象统计
     * @Date 9:04 2023/4/18
     **/
    Pagination<InspectionObjStatisticsVO> objStatistics(InspectionObjQueryParam queryParam);

    /**
     * @Author wql
     * @Description 巡检对象分析-报修明细
     * @Date 21:06 2023/4/19
     **/
    Pagination<TroubleListDetailVO> objStatisticsRepairsDetail(TaskDetailParam queryParam);

    /**
     * @Author wql
     * @Description 巡检对象分析-报修明细导出
     * @Date 21:11 2023/4/19
     **/
    ExcelWriter objStatisticsRepairsDetailExport(TaskDetailParam queryParam);

    /**
     * @Author wql
     * @Description 迅捷结果统计导出
     * @Date 9:48 2023/4/25
     **/
    ExcelWriter resultStatisticsExport(InspectionResultParam resultParam);

    /**
     * 部门巡检统计导出
     *
     * @param depParam
     *
     * @return
     */
    ExcelWriter departmentStatisticsExport(DepartmentInspectionParam depParam);

    /**
     * @Author wql
     * @Description 人员巡检统计导出
     * @Date 10:16 2023/4/25
     **/
    ExcelWriter staffStatisticsExport(DepartmentInspectionParam depParam);

    /**
     * @Author wql
     * @Description 巡检对象统计导出
     * @Date 10:24 2023/4/25
     **/
    ExcelWriter objStatisticsExport(InspectionObjQueryParam queryParam);

    /**
     * @Author wql
     * @Description 稽查不匹配项导出
     * @Date 14:45 2023/4/25
     **/
    ExcelWriter checkDetailExport(ResultRunInHoleParam resultParam);

    /**
     * @Author wql
     * @Description 设备巡检信息统计
     * @Date 10:55 2023/8/9
     **/
    DeviceInspectionVO deviceInspectionInfo(Long deviceId);

    /**
     * 任务标准项异常率统计
     */
    Pagination<TaskStandardAbnormalRateVO> taskStandardAbnormalRate(TaskStandardAbnormalRateParam param);

    /**
     * 任务标准项异常率导出
     */
    List<TaskStandardAbnormalRateVO> taskStandardAbnormalRateExport(TaskStandardAbnormalRateParam param);
}
