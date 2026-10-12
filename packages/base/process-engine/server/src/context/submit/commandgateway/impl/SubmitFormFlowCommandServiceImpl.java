package com.guhecloud.cube.base.flow.context.submit.commandgateway.impl;

import base.constant.VariableConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.client.dto.StartUserInfo;
import com.guhecloud.cube.base.flow.context.dao.FormBusinessNumberConfigMapper;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowTypeEnum;
import com.guhecloud.cube.base.flow.context.po.FormBusinessNumberConfigPO;
import com.guhecloud.cube.base.flow.context.po.FormFlowPO;
import com.guhecloud.cube.base.flow.context.querygateway.FormFlowBusinessQueryService;
import com.guhecloud.cube.base.flow.context.querygateway.FormFlowQueryService;
import com.guhecloud.cube.base.flow.context.submit.commandgateway.SubmitFormFlowCommandService;
import com.guhecloud.cube.base.flow.context.submit.commandgateway.command.SubmitAgainFormCommand;
import com.guhecloud.cube.base.flow.context.tool.FormNumberCreater;
import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.ProcessInstanceCommandService;
import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.command.StartProcessInstanceCommand;
import com.guhecloud.cube.base.flow.flowable.process.application.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ApplicationId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.BusinessId;
import com.guhecloud.cube.base.flow.form.commandgateway.FormInstanceCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormInstanceCommand;
import com.guhecloud.cube.base.flow.form.interfaces.vo.FormDefinitionVO;
import com.guhecloud.cube.base.flow.form.po.FormInstancePO;
import com.guhecloud.cube.base.flow.form.querygateway.FormDefinitionQueryService;
import com.guhecloud.cube.base.flow.form.querygateway.FormInstanceQueryService;
import com.guhecloud.cube.base.flow.form.querygateway.query.FormDefinitionQueryParam;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import instance.command.SubmitInitFormCommand;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.FormBizFieldMapVO;
import vo.FormFlowBaseVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/11/10
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = Exception.class)
public class SubmitFormFlowCommandServiceImpl implements SubmitFormFlowCommandService {

    @Autowired
    private ProcessInstanceCommandService processInstanceCommandService;
    private final FormInstanceCommandService formInstanceCommandService;
    @Resource(name = "flowFormNumberCreater")
    private FormNumberCreater formNumberCreater;
    private final FormInstanceQueryService formInstanceQueryService;
    private final FormFlowQueryService formFlowQueryService;
    private final ApplicationClient applicationClient;
    private final FormDefinitionQueryService formDefinitionQueryService;
    private final FormFlowBusinessQueryService formFlowBusinessQueryService;
    private final FormBusinessNumberConfigMapper businessNumberConfigMapper;

    @Override
    public String submitFirstForm(SubmitInitFormCommand submitInitFormCommand) {
        String businessType = submitInitFormCommand.getBusinessType();
        submitInitFormCommand.setBusinessTypeName(formFlowBusinessQueryService.getByBusinessTypeName(businessType));
        List<FormBusinessNumberConfigPO> numberConfigPOS = businessNumberConfigMapper.selectList(Wrappers
                                                                                                         .<FormBusinessNumberConfigPO>lambdaQuery()
                                                                                                         .eq(FormBusinessNumberConfigPO::getBusinessType, businessType));
        String formNo = formNumberCreater.create(CollectionUtils.isEmpty(numberConfigPOS) ? "" : numberConfigPOS.get(0).getFormNoPrefix());
        submitInitFormCommand.setFormNo(formNo);
        this.initStartUserInfo(submitInitFormCommand.getStartUserInfo());
        if (StringUtils.isBlank(submitInitFormCommand.getBackLogTitle())) {
            submitInitFormCommand.setBackLogTitle(SecurityContextHolder.getPrincipal().getName() +
                                                  "发起的" + submitInitFormCommand.getBusinessTypeName());
        }
        try {
            if (StringUtils.equalsAny(submitInitFormCommand.getType(), FormFlowTypeEnum.FLOW_FORM.name(), FormFlowTypeEnum.CUSTOM_FORM.name())) {
                Long formInstanceId = this.startFormInstance(submitInitFormCommand);
                return this.startProcessInstance(submitInitFormCommand, String.valueOf(formInstanceId));
            } else if (FormFlowTypeEnum.FORM.name().equals(submitInitFormCommand.getType())) {
                return this.startFormInstance(submitInitFormCommand).toString();
            } else if (FormFlowTypeEnum.FLOW.name().equals(submitInitFormCommand.getType())) {
                return this.startProcessInstance(submitInitFormCommand, "");
            }
        } catch (ServiceException e) {
            log.error("发起审批失败，提交的数据：" + submitInitFormCommand, e);
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            log.error("发起审批失败，提交的数据：" + submitInitFormCommand, e);
            throw new ServiceException("发起审批失败");
        }
        return "";
    }

    /**
     * 初始化发起人信息
     *
     * @param startUserInfo
     */
    private void initStartUserInfo(StartUserInfo startUserInfo) {
        if (startUserInfo == null) {
            return;
        }
        SecurityContextHolder.setPrincipal(Principal.builder()
                                                   .id(startUserInfo.getStartUserId())
                                                   .name(startUserInfo.getStartUserName())
                                                   .account("inner_server")
                                                   .type(Principal.Type.USER)
                                                   .admin(false)
                                                   .build());
    }

    @Override
    public void submitAgain(SubmitAgainFormCommand againFormCommand) {
        FormInstancePO formInstancePO = formInstanceQueryService.getById(againFormCommand.getFormInstanceId());
        Assert.isNotFound(ObjectUtils.isEmpty(formInstancePO), "发起失败，未查询原始流程信息");
        SubmitInitFormCommand command = new SubmitInitFormCommand();
        command.setData(againFormCommand.getData());
        command.setVariables(againFormCommand.getVariables());
        // 查询表单实例信息
        command.setBusinessType(formInstancePO.getBusinessType());
        command.setFormKey(formInstancePO.getFormKey());
        command.setFormDefinitionVersion(formInstancePO.getVersion());
        command.setBackLogTitle(againFormCommand.getBackLogTitle());
        // 查询表单定义ID
        List<FormDefinitionVO> definitionVOS = formDefinitionQueryService.get(new FormDefinitionQueryParam(formInstancePO.getFormKey(), formInstancePO.getVersion()));
        Assert.isNotFound(CollectionUtils.isEmpty(definitionVOS), "发起失败，未查询到表单信息");
        command.setFormDefinitionId(definitionVOS.get(0).getId());
        // 查询流程表单信息
        FormFlowPO formFlowPO = formFlowQueryService.getById(formInstancePO.getFormFlowId());
        Assert.isNotFound(ObjectUtils.isEmpty(formFlowPO), "发起失败，流程表单已被删除");
        Assert.isNotFound(!formFlowPO.getEnable(), "发起失败，流程表单已被禁用");
        command.setType(formFlowPO.getType());
        command.setApplication(formFlowPO.getApplication());
        try {
            ApplicationDTO application = applicationClient.getApplicationByCode(formFlowPO.getApplication());
            command.setApplicationName(application == null ? StringUtils.EMPTY_STRING : application.getName());
        } catch (Exception e) {
            log.error("再次发起审批查询应用名称失败", e);
        }
        command.setFlowKey(formFlowPO.getFlowKey());
        this.submitFirstForm(command);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitInnerServerForm(SubmitInitFormCommand command) {
        FormFlowBaseVO formFlowBaseVO = formFlowBusinessQueryService.getFormFlowBaseInfoByBusinessType(command.getBusinessType());
        command.setApplication(formFlowBaseVO.getApplication());
        command.setApplicationName(formFlowBaseVO.getApplicationName());
        command.setFlowKey(formFlowBaseVO.getFlowKey());
        command.setFormKey(formFlowBaseVO.getFormKey());
        command.setFormDefinitionId(formFlowBaseVO.getFormDefinitionId());
        command.setFormDefinitionVersion(formFlowBaseVO.getFormDefinitionVersion());
        command.setData(this.buildData(formFlowBaseVO, command.getData()));
        return this.submitFirstForm(command);
    }

    private String startProcessInstance(SubmitInitFormCommand submitInitFormCommand, String formInstanceId) {
        StartProcessInstanceCommand command = new StartProcessInstanceCommand();
        //公开表单
        if (submitInitFormCommand.isPublicForm()) {
            command.setStartAccount(submitInitFormCommand.getPhone());
            command.setSubmitName(submitInitFormCommand.getName() + ":" + submitInitFormCommand.getPhone());
            command.setPublicForm(true);
        } else {
            command.setStartAccount(SecurityContextHolder.getPrincipal().getId().toString());
        }
        command.setAppId(new ApplicationId(submitInitFormCommand.getApplicationName(), submitInitFormCommand.getApplication()));
        command.setBusinessId(new BusinessId("", submitInitFormCommand.getBusinessType(), submitInitFormCommand.getBusinessTypeName()));
        command.setProcessDefinitionKey(submitInitFormCommand.getFlowKey());
        command.setData(submitInitFormCommand.getData());
        command.setFormInstanceId(formInstanceId);
        command.setApproveUsers(submitInitFormCommand.getApproveUsers());
        command.setData(submitInitFormCommand.getData());
        command.setType(submitInitFormCommand.getType());
        command.setFormDefinitionId(submitInitFormCommand.getFormDefinitionId());
        // 设置默认值
        command.setInstanceName(submitInitFormCommand.getBackLogTitle());
        Map<String, Object> variables = new HashMap<>(submitInitFormCommand.getVariables());
        variables.put(VariableConstant.FORM_NO, submitInitFormCommand.getFormNo());
        command.setVariables(variables);
        ProcessInstanceDTO start = processInstanceCommandService.start(command);
        return start.getId();
    }

    private Long startFormInstance(SubmitInitFormCommand submitInitFormCommand) {
        FormInstanceCommand command = new FormInstanceCommand();
        command.setBusinessType(submitInitFormCommand.getBusinessType());
        command.setFormKey(submitInitFormCommand.getFormKey());
        command.setVersion(submitInitFormCommand.getFormDefinitionVersion());
        return formInstanceCommandService.start(command);
    }

    private String buildData(FormFlowBaseVO formFlowBaseVO, String data) {
        Map<String, Object> dataMap = new HashMap<>();

        //构建发起data
        List<FormBizFieldMapVO> formBizFieldMapList = formFlowBaseVO.getFormBizFieldMapList();
        //发起参数
        Map<String, Object> sourceDataMap = JsonUtils.deserialize(data, new TypeReference<Map<String, Object>>() {
        });
        for (FormBizFieldMapVO formBizFieldMapVO : formBizFieldMapList) {
            Object values = sourceDataMap.get(formBizFieldMapVO.getBusinessField());
            if (values instanceof List) {
                List<Object> valueLists = (List<Object>) values;
                List<Object> listMap = new ArrayList<>();
                for (Object object : valueLists) {
                    if (object instanceof Map) {
                        Map<String, Object> map = new HashMap<>();
                        Map<String, Object> valueMap = (Map<String, Object>) object;
                        for (FormBizFieldMapVO bizFieldMapVO : formBizFieldMapList) {
                            if (!valueMap.containsKey(bizFieldMapVO.getBusinessField())) {
                                continue;
                            }
                            map.put(bizFieldMapVO.getControlId(), valueMap.get(bizFieldMapVO.getBusinessField()));
                        }
                        listMap.add(map);
                    } else {
                        listMap.add(object);
                    }
                }
                dataMap.put(formBizFieldMapVO.getControlId(), listMap);
            } else {
                dataMap.put(formBizFieldMapVO.getControlId(), sourceDataMap.get(formBizFieldMapVO.getBusinessField()));
            }
        }
        return JsonUtils.serialize(dataMap);
    }
}
