package com.guhecloud.cube.base.flow.client;

import com.guhecloud.cube.base.flow.client.dto.TaskVariableDTO;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import instance.command.BackTaskCommand;
import instance.command.CompleteTaskCommand;
import instance.command.SubmitInitFormCommand;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import vo.FormFlowBaseVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = ServiceRoute.TAG)
public interface FlowClient {

    @PostMapping("/form-definition/parse")
    String formParse(@Valid @RequestBody FormDataParam param);

    /**
     * 获取指定前缀工单号
     *
     * @param prefix
     *
     * @return
     */
    @GetMapping("/form/no")
    String getFormNo(@RequestParam("prefix") String prefix);

    /**
     * @Author wql
     * @Description 发起流程
     * @Date 9:56 2023/11/29
     **/
    @PostMapping("/form-flows/first/submit")
    String submitFirst(@Valid @RequestBody SubmitInitFormCommand formFlowCommand);

    /**
     * 内部服务流程发起
     *
     * @param formFlowCommand
     *
     * @return
     */
    @PostMapping("/form-flows/inner/submit")
    String submitInnerServer(@Valid @RequestBody SubmitInitFormCommand formFlowCommand);

    /**
     * 根据业务类型获取表单信息
     *
     * @param businessType
     *
     * @return
     */
    @GetMapping("/form-flow-business/base/type")
    FormFlowBaseVO getFormFlowBaseInfoByBusinessType(@RequestParam(value = "businessType") String businessType);

    /**
     * 同意
     *
     * @param completeTaskCommand
     */
    @PostMapping("/process/task/complete")
    void completeTask(@RequestBody @Valid CompleteTaskCommand completeTaskCommand);

    /**
     * 退回
     *
     * @param backCommand
     */
    @PostMapping("/process/task/back")
    void back(@RequestBody @Valid BackTaskCommand backCommand);

    /**
     * 根据流程实例和审批人获取活跃任务
     */
    @GetMapping("/process/run/task/process-ins-id")
    List<TaskVariableDTO> gerRunTaskByProcessInsIdAndAssignee(@RequestParam("processInstanceId") String processInstanceId,
                                                              @RequestParam(value = "assignee", required = false) String assignee
                                                             );

    /**
     * 根据当前工单获取来源工单id
     */
    @GetMapping("/process/source-id/get")
    String getSourceProcessIdByProcessId(@RequestParam("processInstanceId") String processInstanceId);

    /**
     * 根据当前工单获取来源工单id
     */
    @PostMapping("/process/process-id/by/source")
    Map<String, String> getProcessIdBySourceId(@RequestBody List<String> processInstanceIdList);

    /**
     * 批量删除审批流程
     */
    @PostMapping("/form/instance/form-flows/delete")
    Boolean deleteFormFlow(@RequestBody Set<String> processInsIds);
}
