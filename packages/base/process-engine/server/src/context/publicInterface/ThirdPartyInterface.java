package com.guhecloud.cube.base.flow.context.publicInterface;

import com.guhecloud.cube.base.flow.context.enumeration.SubmitCallTypeEnum;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailExtendVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailVO;
import com.guhecloud.cube.base.flow.context.querygateway.FormFlowBusinessQueryService;
import com.guhecloud.cube.base.flow.context.submit.commandgateway.SubmitFormFlowCommandService;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.ProcessInstanceQueryService;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query.ProcessInstancePageQuery;
import com.guhecloud.cube.base.flow.flowable.process.application.service.ProcessInstanceCacheService;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.ActivatedTaskCommandService;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log.TaskLog;
import com.guhecloud.cube.base.flow.flowable.task.application.querygateway.TaskInstanceQueryService;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.TaskInstanceDetailDTO;
import com.guhecloud.cube.base.flow.form.po.FormControlBusinessPO;
import com.guhecloud.cube.base.flow.form.querygateway.query.FormControlBusinessQueryService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import instance.command.CompleteTaskCommand;
import instance.command.SubmitInitFormCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vo.FormBizFieldMapVO;

import java.util.List;

/**
 * 第三方对接
 *
 * @describe 第三方对接
 * @date 2024/1/2 9:42
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdPartyInterface {
    private final FormFlowBusinessQueryService formFlowBusinessQueryService;
    private final FormControlBusinessQueryService formControlBusinessQueryService;
    private final SubmitFormFlowCommandService submitFormFlowCommandService;
    private final TaskInstanceQueryService taskInstanceQueryService;
    private final ProcessInstanceCacheService processInstanceCacheService;
    private final ActivatedTaskCommandService activatedTaskCommandService;
    private final ProcessInstanceQueryService processInstanceQueryService;

    /**
     * 根据业务type查询关联关系
     *
     * @param businessType 业务类型
     *
     * @return 表单字段关联关系
     */
    @GetMapping("/public/form-flow-business/type")
    public FormFlowDetailExtendVO getByBusinessTypePublic(String businessType) {
        FormFlowDetailVO formFlowDetailVO = formFlowBusinessQueryService.getByBusinessType(businessType);
        //查询字段映射
        List<FormControlBusinessPO> formControlBusinessPOS = formControlBusinessQueryService.lambdaQuery()
                .eq(FormControlBusinessPO::getFormDefinitionId, formFlowDetailVO.getFormDefinitionId())
                .list();
        List<FormBizFieldMapVO> formBizFieldMapVOS = ObjectUtils.copying(formControlBusinessPOS, FormBizFieldMapVO.class);
        FormFlowDetailExtendVO formFlowDetailExtendVO = ObjectUtils.copying(formFlowDetailVO, FormFlowDetailExtendVO.class);
        formFlowDetailExtendVO.setFormBizFieldMapList(formBizFieldMapVOS);
        formFlowDetailExtendVO.setCallType(SubmitCallTypeEnum.EXTERNAL.name());
        return formFlowDetailExtendVO;
    }

    /**
     * 提交流程
     *
     * @param formFlowCommand 业务提交初始表单数据
     *
     * @return 流程实例id
     */
    @PostMapping("/public/form-flows/first/submit")
    public String submitFirstPublic(@Valid @RequestBody SubmitInitFormCommand formFlowCommand) {
        formFlowCommand.setPublicForm(true);
        return submitFormFlowCommandService.submitFirstForm(formFlowCommand);
    }

    /**
     * 查询流程实例信息
     *
     * @param processInstancePageQuery 流程实例查询条件
     *
     * @return 流程实例信息
     */
    @GetMapping("/public/process/instance/historical/allPage")
    public Pagination<ProcessInstanceDTO> allPagePublic(@Valid ProcessInstancePageQuery processInstancePageQuery) {
        return taskInstanceQueryService.allPage(processInstancePageQuery);
    }

    /**
     * 获取流程节点任务信息
     *
     * @param processInstanceId 流程实例id
     * @param userId            可传入id
     *
     * @return 流程信息
     */
    @GetMapping("/public/process/task/form/id/{id}")
    public TaskInstanceDetailDTO getTaskFormByIdPublic(@PathVariable("id") String processInstanceId,
                                                       @RequestParam(value = "userId", required = false) String userId
                                                      ) {
        Long actUserId = 0L;
        if (StringUtils.isNotBlank(userId)) {
            actUserId = Long.parseLong(userId);
        } else {
            try {
                actUserId = SecurityContextHolder.getPrincipal().getId();
            } catch (Exception e) {
                log.error("外部用户调用");
            }
        }
        return processInstanceCacheService.getTaskFormById(processInstanceId, actUserId);
    }

    /**
     * 节点审批通过
     *
     * @param completeTaskCommand 节点审批信息
     */
    @PostMapping("/public/process/task/complete")
    public void completeTask(@RequestBody @Valid CompleteTaskCommand completeTaskCommand) {
        if (StringUtils.isBlank(completeTaskCommand.getAccount())) {
            completeTaskCommand.setAccount(SecurityContextHolder.getPrincipal().getId().toString());
        }
        activatedTaskCommandService.complete(completeTaskCommand);
    }

    /**
     * @param processInstanceId
     * @param flowKey
     *
     * @return
     */
    @GetMapping("/public/process/historical/path/line")
    public List<TaskLog> getHistoricLinePath(@RequestParam(required = false, name = "processInstanceId") String processInstanceId,
                                             @RequestParam(name = "flowKey", required = false) String flowKey
                                            ) {
        return processInstanceQueryService.getHistoricLinePath(processInstanceId, flowKey);
    }

}
