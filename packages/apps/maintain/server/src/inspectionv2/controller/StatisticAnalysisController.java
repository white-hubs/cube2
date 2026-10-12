package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.StatisticAnalysisService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.convert.TaskStandardAbnormalRateConvert;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.TaskStandardAbnormalRateExportDTO;
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
import com.guhecloud.platform.base.util.AssertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wql
 * @describe 统计分析
 * @date 2023/4/13 20:13
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatisticAnalysisController {

    private final StatisticAnalysisService statisticAnalysisService;

    /**
     * @Author wql
     * @Description 巡检结果统计
     * @Date 14:56 2023/4/14
     **/
    @GetMapping("/statistic-analysis/resultStatistics")
    public Pagination<InspectionResultVO> statisticalResult(InspectionResultParam resultParam) {
        return statisticAnalysisService.statisticalResult(resultParam);
    }

    /**
     * @Author wql
     * @Description 巡检结果统计导出
     * @Date 9:47 2023/4/25
     **/
    @GetMapping("/statistic-analysis/resultStatisticsExport")
    public void resultStatisticsExport(InspectionResultParam resultParam,
                                       HttpServletResponse response
                                      ) throws UnsupportedEncodingException {
        ExcelWriter writer = statisticAnalysisService.resultStatisticsExport(resultParam);
        setResponseInfo(response, "巡检结果统计");
        try (var outputStream = response.getOutputStream()) {
            writer.flush(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * @Author wql
     * @Description 部门巡检统计
     * @Date 11:37 2023/4/17
     **/
    @GetMapping("/statistic-analysis/departmentStatistics")
    public Pagination<DepInspectionStatisticsVO> departmentStatistics(DepartmentInspectionParam depParam) {
        return statisticAnalysisService.departmentStatistics(depParam);
    }

    /**
     * @Author wql
     * @Description 部门巡检统计导出
     * @Date 10:05 2023/4/25
     **/
    @GetMapping("/statistic-analysis/departmentStatisticsExport")
    public void departmentStatisticsExport(DepartmentInspectionParam depParam,
                                           HttpServletResponse response
                                          ) throws UnsupportedEncodingException {
        ExcelWriter writer = statisticAnalysisService.departmentStatisticsExport(depParam);
        setResponseInfo(response, "部门巡检统计");
        try (var outputStream = response.getOutputStream()) {
            writer.flush(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * @Author wql
     * @Description 人员巡检统计  objStatistics
     * @Date 18:51 2023/4/17
     **/
    @GetMapping("/statistic-analysis/staffStatistics")
    public Pagination<StaffInspectionStatisticsVO> staffStatistics(DepartmentInspectionParam depParam) {
        return statisticAnalysisService.staffStatistics(depParam);
    }

    /**
     * @Author wql
     * @Description 人员巡检统计导出
     * @Date 10:13 2023/4/25
     **/
    @GetMapping("/statistic-analysis/staffStatisticsExport")
    public void staffStatisticsExport(DepartmentInspectionParam depParam,
                                      HttpServletResponse response
                                     ) throws UnsupportedEncodingException {
        ExcelWriter writer = statisticAnalysisService.staffStatisticsExport(depParam);
        setResponseInfo(response, "人员巡检统计");
        try (var outputStream = response.getOutputStream()) {
            writer.flush(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * @Author wql
     * @Description 巡检对象统计
     * @Date 9:03 2023/4/18
     **/
    @GetMapping("/statistic-analysis/objStatistics")
    public Pagination<InspectionObjStatisticsVO> objStatistics(InspectionObjQueryParam queryParam) {
        return statisticAnalysisService.objStatistics(queryParam);
    }

    /**
     * @Author wql
     * @Description 巡检对象统计导出
     * @Date 10:23 2023/4/25
     **/
    @GetMapping("/statistic-analysis/objStatisticsExport")
    public void objStatisticsExport(InspectionObjQueryParam queryParam, HttpServletResponse response) throws UnsupportedEncodingException {
        ExcelWriter writer = statisticAnalysisService.objStatisticsExport(queryParam);
        setResponseInfo(response, "巡检对象统计");
        try (var outputStream = response.getOutputStream()) {
            writer.flush(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * @Author wql
     * @Description 巡检对象分析-报修明细
     * @Date 21:06 2023/4/19
     **/
    @GetMapping("/statistic-analysis/objStatisticsRepairsDetail")
    public Pagination<TroubleListDetailVO> objStatisticsRepairsDetail(TaskDetailParam queryParam) {
        return statisticAnalysisService.objStatisticsRepairsDetail(queryParam);
    }

    /**
     * @Author wql
     * @Description 巡检对象分析-报修明细导出
     * @Date 21:09 2023/4/19
     **/
    @GetMapping("/statistic-analysis/objStatisticsRepairsDetailExport")
    public void objStatisticsRepairsDetailExport(TaskDetailParam queryParam,
                                                 HttpServletResponse response
                                                ) throws UnsupportedEncodingException {
        ExcelWriter writer = statisticAnalysisService.objStatisticsRepairsDetailExport(queryParam);
        setResponseInfo(response, "报修明细导出");
        try (var outputStream = response.getOutputStream()) {
            writer.flush(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * @Author wql
     * @Description 巡检结果分析-稽查不匹配项明细列表
     * @Date 18:11 2023/4/14
     **/
    @GetMapping("/statistic-analysis/checkDetail")
    public Pagination<CheckTheMismatchDetailVO> checkDetail(ResultRunInHoleParam resultParam) {
        return statisticAnalysisService.checkDetail(resultParam);
    }

    /**
     * @Author wql
     * @Description 巡检结果分析-稽查不匹配项明细导出
     * @Date 18:11 2023/4/14
     **/
    @GetMapping("/statistic-analysis/checkDetailExport")
    public void checkDetailExport(ResultRunInHoleParam resultParam, HttpServletResponse response) throws UnsupportedEncodingException {
        ExcelWriter writer = statisticAnalysisService.checkDetailExport(resultParam);
        setResponseInfo(response, "巡检不匹配项明细");
        try (var outputStream = response.getOutputStream()) {
            writer.flush(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * @Author wql
     * @Description 隐患记录明细
     * @Date 8:49 2023/4/17
     **/
    @GetMapping("/statistic-analysis/hiddenDangerDetail")
    public Pagination<HiddenDangerDetailVO> getHiddenDangerDetail(TaskDetailParam detailParam) {
        return statisticAnalysisService.getHiddenDangerDetail(detailParam);
    }

    /**
     * @Author wql
     * @Description 隐患明细导出
     * @Date 10:38 2023/4/17
     **/
    @GetMapping("/statistic-analysis/exportHiddenDangerDetail")
    public void exportHiddenDangerDetail(TaskDetailParam detailParam, HttpServletResponse response) throws UnsupportedEncodingException {
        ExcelWriter writer = statisticAnalysisService.getAllHiddenDangerDetail(detailParam);
        setResponseInfo(response, "隐患记录");
        try (var outputStream = response.getOutputStream()) {
            writer.flush(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * @Author wql
     * @Description 巡检结果分析-报修记录明细列表
     * @Date 18:11 2023/4/14
     **/
    @GetMapping("/statistic-analysis/repairsDetail")
    public Pagination<TroubleListDetailVO> repairsDetail(ResultRunInHoleParam resultParam) {
        return statisticAnalysisService.repairsDetail(resultParam);
    }

    /**
     * @Author wql
     * @Description 巡检结果分析-报修记录明细列表导出
     * @Date 11:00 2023/4/17
     **/
    @GetMapping("/statistic-analysis/repairsDetailExport")
    public void repairsDetailExport(ResultRunInHoleParam resultParam, HttpServletResponse response) throws UnsupportedEncodingException {
        ExcelWriter writer = statisticAnalysisService.repairsDetailExport(resultParam);
        setResponseInfo(response, "报修记录");
        try (var outputStream = response.getOutputStream()) {
            writer.flush(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * @Author wql
     * @Description 任务明细下钻
     * @Date 16:04 2023/4/17
     **/
    @GetMapping("/statistic-analysis/taskDetail")
    public Pagination<InspectionResultDetailVO> taskDetail(TaskDetailParam detailParam) {
        return statisticAnalysisService.taskDetail(detailParam);
    }

    /**
     * @Author wql
     * @Description 任务明细导出
     * @Date 17:13 2023/4/17
     **/
    @GetMapping("/statistic-analysis/taskDetailExport")
    public void taskDetailExport(TaskDetailParam detailParam, HttpServletResponse response) throws UnsupportedEncodingException {
        ExcelWriter writer = statisticAnalysisService.taskDetailExport(detailParam);
        setResponseInfo(response, "任务记录明细");
        try (var outputStream = response.getOutputStream()) {
            writer.flush(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * @Author wql
     * @Description 巡检记录明细
     * @Date 20:05 2023/4/17
     **/
    @GetMapping("/statistic-analysis/inspectionDetail")
    public Pagination<DepInspectionDetailVO> inspectionDetail(InspectionNumParam inspectionNumParam) {
        return statisticAnalysisService.inspectionDetail(inspectionNumParam);
    }

    /**
     * @Author wql
     * @Description 巡检记录明细导出
     * @Date 20:24 2023/4/17
     **/
    @GetMapping("/statistic-analysis/inspectionDetailExport")
    public void inspectionDetailExport(InspectionNumParam inspectionNumParam,
                                       HttpServletResponse response
                                      ) throws UnsupportedEncodingException {
        ExcelWriter writer = statisticAnalysisService.inspectionDetailExport(inspectionNumParam);
        setResponseInfo(response, "巡检记录明细");
        try (var outputStream = response.getOutputStream()) {
            writer.flush(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * @Author wql
     * @Description 设备巡检统计信息
     * @Date 10:52 2023/8/9
     **/
    @GetMapping("/statistic-analysis/deviceInspection")
    public DeviceInspectionVO deviceInspectionInfo(Long deviceId) {
        AssertUtils.assertNotNull(deviceId, "设备id不能为空");
        return statisticAnalysisService.deviceInspectionInfo(deviceId);
    }

    /**
     * 任务标准项异常率统计
     */
    @GetMapping("/statistic-analysis/taskStandardAbnormalRate")
    public Pagination<TaskStandardAbnormalRateVO> taskStandardAbnormalRate(TaskStandardAbnormalRateParam param) {
        return statisticAnalysisService.taskStandardAbnormalRate(param);
    }

    /**
     * 任务标准项异常率统计导出
     */
    @GetMapping("/statistic-analysis/taskStandardAbnormalRateExport")
    public void taskStandardAbnormalRateExport(TaskStandardAbnormalRateParam param,
                                               HttpServletResponse response
                                              ) throws UnsupportedEncodingException {
        List<TaskStandardAbnormalRateExportDTO> exportList = TaskStandardAbnormalRateConvert.convertExport(
                statisticAnalysisService.taskStandardAbnormalRateExport(param)
        );
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("任务标准项异常率统计", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ExcelTypeEnum.XLSX.getValue());
        try (var outputStream = response.getOutputStream()) {
            EasyExcel.write(outputStream, TaskStandardAbnormalRateExportDTO.class)
                    .sheet("任务标准项异常率统计")
                    .doWrite(exportList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author wql
     * @Description 明细导出设置响应信息
     * @Date 11:35 2023/4/17
     **/
    private void setResponseInfo(HttpServletResponse response, String name) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        String fileName = URLEncoder.encode(name + DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss"), "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
    }

}
