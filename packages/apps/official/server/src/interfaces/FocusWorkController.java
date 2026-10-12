package com.guhecloud.cube.srv.gen.official.management.server.interfaces;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.FormClient;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.FocusWorkCommandService;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command.FocusWorkBatchDeleteCommand;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command.FocusWorkFailCommand;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.FocusWorkReplyDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.FocusWorkUpdateDTO;
import com.guhecloud.cube.srv.gen.official.management.server.enumeration.FocusWorkStatus;
import com.guhecloud.cube.srv.gen.official.management.server.enumeration.FocusWorkUpdateStatus;
import com.guhecloud.cube.srv.gen.official.management.server.enumeration.OfficialApprovalBizEnum;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.FocusWorkExtendFlowVO;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.FocusWorkExtendReplyVO;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.FocusWorkFailVO;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.FocusWorkReplyVO;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.FocusWorkVO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkPO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkReplyPO;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.FocusWorkQueryService;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.dto.FocusWorkDTO;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.query.FocusWorkParam;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vo.FormFieldVO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 督办工作统计
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FocusWorkController {
    private final FocusWorkQueryService focusWorkQueryService;
    private final FocusWorkCommandService focusWorkCommandService;
    private final ProcessInstanceClient processInstanceClient;
    private final FlowClient flowClient;
    private final FormClient formClient;

    /**
     * 督办工作分页查询
     *
     * @param param
     *
     * @return
     */
    @DataPermissionSwitch(dataModel = "official:focusWork")
    @GetMapping("/focusWork/page/extendFlow")
    public Pagination<FocusWorkExtendFlowVO> focusWorkPageExtendFlow(FocusWorkParam param) {
        Pagination<FocusWorkDTO> page = focusWorkQueryService.pageFocusWork(param);
        //扩展流程信息
        List<FocusWorkExtendFlowVO> collect = page.getRecords().stream().map(d -> {
            FocusWorkExtendFlowVO fw = ObjectUtils.copying(d, FocusWorkExtendFlowVO.class);
            return fw;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, collect);
    }

    /**
     * 督办工作excel导出
     *
     * @param param
     *
     * @return
     */
    @DataPermissionSwitch(dataModel = "official:focusWork")
    @GetMapping("/focusWork/excel/extendFlow")
    public void focusWorkExcelExtendFlow(FocusWorkParam param) {
        focusWorkQueryService.focusWorkExcelExtendFlow(param);
    }

    /**
     * 督办工作详情 带最新的反馈
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/focusWork/{id}/detail/reply")
    public FocusWorkExtendReplyVO focusWorkPageExtendReply(@PathVariable Long id) {
        FocusWorkPO focusWork = focusWorkQueryService.getById(id);
        FocusWorkReplyPO reply = focusWorkQueryService.getLastestFocusWorkReply(id);
        FocusWorkReplyVO replyVO = reply != null ? ObjectUtils.copying(reply, FocusWorkReplyVO.class) : new FocusWorkReplyVO();
        if (ObjectUtils.isNotEmpty(focusWork) && ObjectUtils.isNotEmpty(replyVO)) {
            replyVO.setPracticableDetails(focusWork.getPracticableDetails());
        }
        //查询失败或未完成原因
        FocusWorkFailVO focusWorkFailVO = focusWorkQueryService.getFocusWorkFailVO(id);
        return FocusWorkExtendReplyVO.builder()
                .focusWork(focusWork != null ? ObjectUtils.copying(focusWork, FocusWorkVO.class) : new FocusWorkVO())
                .focusWorkReply(replyVO)
                .focusWorkFail(focusWorkFailVO)
                .build();
    }

    /**
     * 查询督办事项失败原因
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/focusWork/{id}/fail")
    public FocusWorkFailVO getFocusWorkFail(@PathVariable Long id) {
        //查询失败或未完成原因
        FocusWorkFailVO focusWorkFailVO = focusWorkQueryService.getFocusWorkFailVO(id);
        return focusWorkFailVO;
    }

    /**
     * 通过督办事项ID，查询反馈记录
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/focusWork/{id}/reply")
    public List<FocusWorkReplyVO> focusWorkReplyPage(@PathVariable Long id) {
        List<FocusWorkReplyPO> list = focusWorkQueryService.getReplyByFocusWorkid(id);
        if (CollectionUtils.isNotEmpty(list)) {
            return ObjectUtils.copying(list, FocusWorkReplyVO.class);
        }
        return Collections.emptyList();
    }

    /**
     * 表单控件：通过督办事项ID查询事项描述
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/focusWork/weight/{id}")
    public FormFieldVO focusWorkDescForWeight(@PathVariable Long id) {
        FocusWorkPO fw = focusWorkQueryService.getById(id);
        return FormFieldVO.builder().id(fw.getId().toString()).name(fw.getWorkDesc()).build();
    }

    /**
     * 表单控件：通过事项ID查询事项完成时间
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/focusWork/weight/{id}/completeTime")
    public FormFieldVO focusWorkCompleteTimeForWeight(@PathVariable Long id) {
        FocusWorkPO fw = focusWorkQueryService.getById(id);
        String finalDate = fw.getUpdateCompleteTime() != null ? fw.getUpdateCompleteTimeStr() : fw.getCompleteTimeStr();
        return FormFieldVO.builder().id(fw.getId().toString())
                .name(finalDate).build();
    }

    /**
     * 表单控件：通过事项ID查询事项工作要求
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/focusWork/weight/{id}/requirement")
    public FormFieldVO focusWorkRequirementForWeight(@PathVariable Long id) {
        FocusWorkPO fw = focusWorkQueryService.getById(id);
        return FormFieldVO.builder().id(fw.getId().toString())
                .name(fw.getRequirement()).build();
    }

    /**
     * 表单控件：获取督办事项的状态列表
     *
     * @return
     */
    @GetMapping("/focusWork/weight/status/list")
    public List<FormFieldVO> focusWorkStatusForWeight() {
        return Arrays.stream(FocusWorkUpdateStatus.values()).map(s -> FormFieldVO.builder()
                        .id(s.name()).name(s.getLabel()).build())
                .collect(Collectors.toList());
    }

    /**
     * 表单控件：查询未完成的督办事项列表
     *
     * @return
     */
    @GetMapping("/focusWork/weight/list")
    public List<FormFieldVO> focusWorkListForWeight() {
        List<FocusWorkPO> list = this.focusWorkQueryService.list(Wrappers.<FocusWorkPO>lambdaQuery()
                                                                         .notIn(FocusWorkPO::getStatus, FocusWorkStatus.FINISH, FocusWorkStatus.STOP));
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().map(w -> FormFieldVO.builder().id(w.getId().toString())
                            .name(w.getWorkDesc()).build())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 获取督办事项中的第一责任人
     *
     * @return
     */
    @GetMapping("/focusWork/firstUsers")
    public List<FormFieldVO> getFirstUsersInFocusWork() {
        List<Map> users = this.focusWorkQueryService.getFirstUsersInFocusWork();
        return JsonUtils.deserialize(users, new TypeReference<List<FormFieldVO>>() {
        });
    }

    /**
     * 获取督办审批记录
     * 先调用processInstanceClient，查询出流程列表
     * 取出流程数据中的 表单定义ID，Json表单数据，进行解析
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/focusWork/flow/page")
    public Pagination<FocusWorkExtendFlowVO> focusWorkFlowPage(ProcessInstancePageQuery query) {
        //通过flow的client查询督办审批数据
        Pagination<ProcessInstanceDTO> flowPage = processInstanceClient.page(query);
        if (CollectionUtils.isNotEmpty(flowPage.getRecords())) {
            //督办事项，直接从json中解析出业务数据；因为审批结束前，在数据库中查不出督办业务数据
            //如果审批结束了，可以通过processInstanceId 查询出督办业务数据
            List<FocusWorkExtendFlowVO> collect = flowPage.getRecords().stream().map(f -> {
                FocusWorkExtendFlowVO buildVo = new FocusWorkExtendFlowVO();
                //获取表单定义ID
                Long formInstanceId = Long.valueOf(f.getVariables().get("formInstanceId").toString());
                //获取表单定义ID
                Long definitionId = formClient.getDefinitionId(formInstanceId);
                String jsonFormData = f.getVariables().get("data").toString();
                String bizJson = flowClient.formParse(new FormDataParam(definitionId, jsonFormData));
                JSONObject oldJsonData = JSONObject.parseObject(bizJson);
                if (OfficialApprovalBizEnum.OFFICIAL_FOCUS_WORK.name().equals(query.getBusinessType())) {
                    FocusWorkPO focusWork = this.focusWorkQueryService.getOne(Wrappers.<FocusWorkPO>lambdaQuery()
                                                                                      .eq(FocusWorkPO::getProcessInstanceId, f.getProcessInstanceId()));
                    if (focusWork != null) {
                        buildVo = ObjectUtils.copying(focusWork, FocusWorkExtendFlowVO.class);
                    } else {
                        FocusWorkPO focusWorkPO = JSONObject.parseObject(bizJson, FocusWorkPO.class);
                        focusWorkPO = focusWorkCommandService.extendFocusWork(this.formatFocusWorkInfo(focusWorkPO, f));
                        buildVo = ObjectUtils.copying(focusWorkPO, FocusWorkExtendFlowVO.class);
                        buildVo.setCompleteTimeStr(oldJsonData.getString("completeTime"));
                    }
                } else if (OfficialApprovalBizEnum.OFFICIAL_FOCUS_WORK_REPLY.name().equals(query.getBusinessType())) {
                    FocusWorkReplyDTO replyDTO = JSONObject.parseObject(bizJson, FocusWorkReplyDTO.class);
                    FocusWorkPO focusWorkPO = this.focusWorkQueryService.getById(replyDTO.getFocusWorkId());
                    buildVo = ObjectUtils.copying(focusWorkPO, FocusWorkExtendFlowVO.class);
                } else if (OfficialApprovalBizEnum.OFFICIAL_FOCUS_WORK_UPDATE.name().equals(query.getBusinessType())) {
                    FocusWorkUpdateDTO updateDTO = JSONObject.parseObject(bizJson, FocusWorkUpdateDTO.class);
                    FocusWorkPO focusWorkPO = this.focusWorkQueryService.getById(updateDTO.getFocusWorkId());
                    buildVo = ObjectUtils.copying(focusWorkPO, FocusWorkExtendFlowVO.class);
                }
                //扩展流程信息
                buildVo = buildVo == null ? new FocusWorkExtendFlowVO() : buildVo;
                buildVo.setProcessInstanceId(f.getProcessInstanceId());
                buildVo.setProcessInstanceCode(f.getVariables().get("formNo").toString());
                buildVo.setProcessInstanceStatus(f.getStatus());
                buildVo.setProcessInstanceNode(f.getActiveNodeName());
                buildVo.setApplyTime(f.getSubmitedTime());
                buildVo.setApplyDoneTime(f.getLastTaskCompletedTime());
                buildVo.setApplicantName(f.getStartUserName());
                return buildVo;
            }).collect(Collectors.toList());
            return PagingUtils.convert(flowPage, collect);
        }
        return Pagination.empty();
    }

    private FocusWorkPO formatFocusWorkInfo(FocusWorkPO focusWorkPO, ProcessInstanceDTO processInstanceDTO) {
        if (StringUtils.isNotBlank(focusWorkPO.getDeptId())) {
            List<String> deptIds = JsonUtils.deserialize(focusWorkPO.getDeptId(), new TypeReference<List<String>>() {
            });
            focusWorkPO.setDeptId(deptIds.stream().collect(Collectors.joining(",")));
        }
        if (StringUtils.isNotBlank(focusWorkPO.getFirstUserId())) {
            List<String> userIds = JsonUtils.deserialize(focusWorkPO.getFirstUserId(), new TypeReference<List<String>>() {
            });
            focusWorkPO.setFirstUserId(userIds.stream().collect(Collectors.joining(",")));
        }
        if (StringUtils.isNotBlank(focusWorkPO.getSecondUserId())) {
            List<String> userIds = JsonUtils.deserialize(focusWorkPO.getSecondUserId(), new TypeReference<List<String>>() {
            });
            focusWorkPO.setSecondUserId(userIds.stream().collect(Collectors.joining(",")));
        }
        if (StringUtils.isNotBlank(focusWorkPO.getFocusWorkStarterId())) {
            List<String> userIds = JsonUtils.deserialize(focusWorkPO.getFocusWorkStarterId(), new TypeReference<List<String>>() {
            });
            focusWorkPO.setFocusWorkStarterId(userIds.stream().collect(Collectors.joining(",")));
        }
        focusWorkPO.setApplicantId(processInstanceDTO.getStartUserId());
        return focusWorkPO;
    }

    /**
     * 添加督办事项最终的失败或未完成原因
     *
     * @param command
     */
    @PostMapping("/focusWork/fail")
    public void addFocusWorkFail(@RequestBody FocusWorkFailCommand command) {
        this.focusWorkCommandService.addFocusWorkFail(command);
    }

    /**
     * 督办工作批量删除
     *
     * @param command
     */
    @PostMapping("/focusWork/batch-deleted")
    public void focusWorkBatchDelete(@Valid @RequestBody FocusWorkBatchDeleteCommand command) {
        this.focusWorkCommandService.focusWorkBatchDelete(command);
    }
}
