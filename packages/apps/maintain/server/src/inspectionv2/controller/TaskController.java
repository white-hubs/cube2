package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.TaskService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.convert.TaskDetailRateExportConvert;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.convert.TaskPageExportConvert;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTaskDataModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.TaskDetailRateExportDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.TaskPageExportDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTaskParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionCommonParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskSplitParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.DetailRateResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskDetailResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskObjCountResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.PerformInspectionVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

/**
 * 巡检任务控制层
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskController {
    private static final int EXPORT_BATCH_SIZE = 2000;

    private final TaskService taskService;

    /**
     * 分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect-task/pageV2")
    @DataPermissionSwitch(dataModel = "inspect:inspectTask")
    public Pagination<TaskDetailResult> getPage(TaskPageParam param) {
        return taskService.getPage(param);
    }

    /**
     * 分页列表导出
     *
     * @param param 查询条件
     *
     * @throws IOException 写出失败
     */
    @PostMapping("/inspect-task/pageV2/export")
    @DataPermissionSwitch(dataModel = "inspect:inspectTask")
    public void exportPage(@RequestBody TaskPageParam param, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("巡检任务列表", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ExcelTypeEnum.XLSX.getValue());
        WriteSheet sheet = EasyExcel.writerSheet("巡检任务列表")
                .head(TaskPageExportDTO.class)
                .build();

        boolean wroteData = false;
        int pageNumber = 1;
        try (var outputStream = response.getOutputStream()) {
            ExcelWriter writer = EasyExcel.write(outputStream).build();
            try {
                while (true) {
                    param.setPageNumber(pageNumber);
                    param.setPageSize(EXPORT_BATCH_SIZE);
                    List<TaskDetailResult> records = taskService.getExportPage(param);
                    if (CollectionUtils.isEmpty(records)) {
                        break;
                    }
                    writer.write(TaskPageExportConvert.convert(records), sheet);
                    wroteData = true;
                    if (records.size() < EXPORT_BATCH_SIZE) {
                        break;
                    }
                    pageNumber++;
                }
                if (!wroteData) {
                    writer.write(Collections.emptyList(), sheet);
                }
            } finally {
                writer.finish();
            }
        }
    }

    /**
     * 详情
     *
     * @param id        任务id
     * @param notTarget 不查询巡检对象(为true时才不查询)
     *
     * @return 任务详情
     */
    @GetMapping("/inspect-task/detailV2")
    public TaskDetailResult getDetail(@RequestParam("id") Long id, @RequestParam(value = "notTarget", required = false) Boolean notTarget) {
        return taskService.getDetail(id, notTarget);
    }

    /**
     * @Author wql
     * @Description app端巡检任务详情
     * @Date 9:17 2023/4/21
     **/
    @GetMapping("/inspect-task/appTaskDetail/{id}")
    public TaskDetailResult appTaskDetail(@PathVariable("id") Long id) {
        return taskService.appTaskDetail(id);
    }

    /**
     * @Author wql
     * @Description 获取巡检对象的巡检内容
     * @Date 11:28 2023/4/21
     **/
    @GetMapping("/inspect-task/{taskId}/performInspection")
    public PerformInspectionVO performInspection(@PathVariable("taskId") Long taskId, Long objId) {
        return taskService.performInspection(taskId, objId);
    }

    /**
     * 巡检计划-详情-对象的巡检标准列表
     * 根据巡检任务和对象id获取巡检标准列表
     * taskId: 任务id Long 必填
     * targetId：对象id Long 必填
     *
     * @return
     */
    @GetMapping("/inspect-task/detail-standardV2")
    public List<StandardResult> getDeailStandardList(TaskPageParam param) {
        return taskService.getDeailStandardList(param.getTaskId(), param.getTargetId());
    }

    /**
     * 巡检任务-详情-执行进度
     *
     * @param param 入参
     *              id：任务id，isSplit:是否是拆解任务
     *
     * @return
     */
    @GetMapping("/inspect-task/detail-rateV2")
    public DetailRateResult getDetailRateResult(TaskPageParam param) {
        return taskService.getDetailRateResult(param);
    }

    /**
     * 巡检任务-详情-巡检对象列表导出
     *
     * @param param 入参，与 detail-rateV2 保持一致
     *
     * @throws IOException 写出失败
     */
    @PostMapping("/inspect-task/detail-rateV2/export")
    public void exportDetailRate(@RequestBody TaskPageParam param, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("巡检任务详情", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ExcelTypeEnum.XLSX.getValue());
        try (var outputStream = response.getOutputStream()) {
            EasyExcel.write(outputStream, TaskDetailRateExportDTO.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("巡检对象列表")
                    .doWrite(TaskDetailRateExportConvert.convert(taskService.getDetailRateExportList(param)));
        }
    }

    /**
     * 巡检任务对象统计
     *
     * @param taskId 任务id
     *
     * @return 统计结果
     */
    @GetMapping("/inspect-task/objCount")
    public TaskObjCountResult getTaskObjCount(Long taskId) {
        return taskService.getTaskObjCount(taskId);
    }

    /**
     * 巡检对象分页（支持对象类型/对象名称/状态筛选）
     *
     * @param param 条件（id 或 taskId、targetCategory、keyword、status、page/size）
     *
     * @return 巡检对象分页
     */
    @GetMapping("/inspect-task/inspection-obj")
    public Pagination<InspectionObjVO> getInspectionObjInfo(TaskPageParam param) {
        return taskService.getInspectionObjInfo(param);
    }

    /**
     * 任务拆解
     *
     * @param taskSplitParam
     */
    @PostMapping("/inspect-task/splitV2")
    public void splitTask(@RequestBody TaskSplitParam taskSplitParam) {
        taskService.splitTask(taskSplitParam);
    }

    /**
     * 测试发送消息
     */
    @PostMapping("/inspect-task/sendMsp")
    public void testSendMsg(Long taskId) {
        InspectTaskPO taskPO = taskService.getById(taskId);
        taskService.checkTaskGenerateSendMsg(taskPO);
    }

    /**
     * @Author wql
     * @Description 安全管理数据采集接口
     * @Date 17:42 2023/9/8
     **/
    @PreAuthorize("hasScope('inspect_manage')")
    @GetMapping("/inspect-task/getInspectDataModel")
    public List<InspectTaskDataModel> getSafetyManagementDataModel(Period period) {
        return taskService.getSafetyManagementDataModel(period);
    }

    /**
     * 新增巡检任务
     *
     * @param taskParam
     */
    @PostMapping("/inspect-task/save")
    public void addInspectTask(@RequestBody InspectTaskParam taskParam) {
        taskService.saveInspectTask(taskParam);
    }

    /**
     * 获取任务总数
     *
     * @param param 时间
     *
     * @return 总数
     */
    @GetMapping("/inspect-task/taskTotal")
    public Long getTaskTotal(InspectionCommonParam param) {
        return taskService.getTaskTotal(param);
    }

    /**
     * 获取任务总数
     *
     * @param param 时间
     *
     * @return 总数
     */
    @PostMapping("/inspect-task/taskTotal/post")
    public Long postTaskTotal(@RequestBody InspectionCommonParam param) {
        return taskService.getTaskTotal(param);
    }

    /**
     * 巡捡处理率指标
     */
    @GetMapping("/inspect-task/complete/rate/metric")
    public HandlerRateMetricDTO getInspectTaskCompleteRateMetric(@RequestParam("period") Period period) {
        return taskService.getInspectTaskCompleteRateMetric(period);
    }

    /**
     * 巡检处理效率指标
     */
    @GetMapping("/inspect-task/complete/efficiency/metric")
    public HandlerEfficiencyMetricDTO getInspectTaskCompleteEfficiencyMetric(@RequestParam("period") Period period) {
        return taskService.getInspectTaskCompleteEfficiencyMetric(period);
    }

    /**
     * 巡检处理数、处理总时长
     */
    @GetMapping("/inspect-task/complete/time/num/metric")
    public TaskHandlerTimeVO getInspectTaskTimeCompleteTimeNumMetric(@RequestParam("period") Period period) {
        return taskService.getInspectTaskTimeCompleteTimeNumMetric(period);
    }
}
