package com.guhecloud.cube.base.flow.client;

import com.guhecloud.cube.base.flow.client.dto.HistoricalProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.dto.PageOfDataBoardDTO;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.dto.TaskLogVO;
import com.guhecloud.cube.base.flow.client.dto.WorkOrderStatisticsDTO;
import com.guhecloud.cube.base.flow.client.param.WordOrderStatisticsVO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.platform.base.model.Pagination;
import feign.Response;
import instance.command.ProcessStartedMessageAttrCommand;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = ServiceRoute.TAG)
public interface ProcessInstanceClient {

    /**
     * 按条件查询历史流程实例
     * 历史流程实例：包含了正在运行的流程实例和已完成的流程实例
     *
     * @param processInstanceIds    流程实例id数组   因为是get请求，所以此接口不支持传递大量的流程实例
     * @param processInstanceStatus 流程实例状态
     * @param backlogKey            过滤此用户的数据
     * @param submitted             提交的时间
     *
     * @return 过滤后的流程实例
     */
    @GetMapping("/flow/historical/instance/list")
    List<HistoricalProcessInstanceDTO> historicalList(@RequestParam("processInstanceIds") Set<Long> processInstanceIds,
                                                      @RequestParam("processInstanceStatus") ProcessInstanceStatus processInstanceStatus,
                                                      @RequestParam("backlogKey") BacklogKey backlogKey,
                                                      @RequestParam("submitted") Period submitted
                                                     );

    /**
     * 分页查询流程实例
     * 未来将会废弃，目前没有可用替代实现
     *
     * @param processInstancePageQuery
     *
     * @return
     */
    @GetMapping("/process/instance/historical/page")
    Pagination<ProcessInstanceDTO> page(@Valid @SpringQueryMap ProcessInstancePageQuery processInstancePageQuery);

    /**
     * @Author wql
     * @Description 所有流程实例列表
     * @Date 13:52 2023/11/6
     **/
    @GetMapping("/process/instance/historical/allPage")
    Pagination<ProcessInstanceDTO> allPage(@Valid @SpringQueryMap ProcessInstancePageQuery processInstancePageQuery);

    /**
     * @Author wql
     * @Description 获取流程实例表单字段数据
     * @Date 10:09 2023/9/26
     **/
    @GetMapping("/process/start/tableInfo")
    ProcessStartedMessageAttrCommand getProcessInstanceFormInfo(@RequestParam("processInstanceId") String processInstanceId);

    /**
     * @Author wql
     * @Description 数据看板流程实例
     * @Date 9:49 2023/11/8
     **/
    @GetMapping("/process/instance/historical/page/backlog")
    Pagination<ProcessInstanceDTO> pageOfDataBoard(@Valid @SpringQueryMap PageOfDataBoardDTO query);

    /**
     * 批量查询流程表单
     *
     * @param processInstancePageQuery
     *
     * @return
     */
    @PostMapping("/process/instance/historical/batch")
    Pagination<ProcessInstanceDTO> allPageBatch(@Valid @RequestBody ProcessInstancePageQuery processInstancePageQuery);

    /**
     * 批量导出流程审批记录
     */
    @PostMapping("/process/instance/historical/export/batch")
    Response processInstanceHistoricalExportBatch(@Valid @RequestBody ProcessInstancePageQuery processInstancePageQuery);

    /**
     * 历史流程节点
     */
    @PostMapping("/process/historical/node")
    Map<String, List<TaskLogVO>> getHistoricNode(@RequestBody List<String> processInstanceIdList);

    /**
     * 根据流程id查询流程实例
     */
    @GetMapping("/process/instance/historical/get/id")
    ProcessInstanceDTO processInstanceGet(@RequestParam("processInsId") String processInsId);

    /**
     * 获取全部工单总数
     */
    @GetMapping("/statistics/group/businessType/total")
    List<WorkOrderStatisticsDTO> getNumberGroupByBusinessTypeTotal(@SpringQueryMap WordOrderStatisticsVO query);
}
