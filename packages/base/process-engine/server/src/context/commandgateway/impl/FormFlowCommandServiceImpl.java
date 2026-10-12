package com.guhecloud.cube.base.flow.context.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowCommandService;
import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowGroupService;
import com.guhecloud.cube.base.flow.context.commandgateway.FormPermissionService;
import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowCommand;
import com.guhecloud.cube.base.flow.context.dao.FormFlowMapper;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowStatusEnum;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowTypeEnum;
import com.guhecloud.cube.base.flow.context.po.FormFlowPO;
import com.guhecloud.cube.base.flow.context.querygateway.FormFlowBusinessQueryService;
import com.guhecloud.cube.base.flow.flowable.definition.application.commandgateway.ProcessDefinitionCommandService;
import com.guhecloud.cube.base.flow.flowable.definition.application.dto.ProcessDefinitionDTO;
import com.guhecloud.cube.base.flow.flowable.definition.interfaces.dto.FlowEngineDTO;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.ProcessInstanceQueryService;
import com.guhecloud.cube.base.flow.form.commandgateway.FormDefinitionCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.PublishFormDefinitionCommand;
import com.guhecloud.cube.base.flow.form.dao.FormInstanceMapper;
import com.guhecloud.cube.base.flow.form.enumeration.FormInstanceStatusEnum;
import com.guhecloud.cube.base.flow.form.po.FormDefinitionPO;
import com.guhecloud.cube.base.flow.form.po.FormInstancePO;
import com.guhecloud.cube.base.flow.webhooks.domain.WebhookConfig;
import com.guhecloud.cube.base.flow.webhooks.service.WebhookConfigService;
import com.guhecloud.cube.base.flow.webhooks.vo.WebhookConfigVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/09/08
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormFlowCommandServiceImpl extends ServiceImpl<FormFlowMapper, FormFlowPO> implements FormFlowCommandService {
    private final FormInstanceMapper formInstanceMapper;
    private final FormDefinitionCommandService definitionCommandService;
    private final FormFlowBusinessQueryService formFlowBusinessQueryService;
    private final ProcessInstanceQueryService processInstanceQueryService;
    private final ProcessDefinitionCommandService processDefinitionCommandService;
    private final WebhookConfigService webhookConfigService;
    private final FileClient fileClient;
    private final FormPermissionService formPermissionService;
    private final FormFlowGroupService formFlowGroupService;

    @Override
    public Long saveOrUpdate(FormFlowCommand formFlowCommand) {
        formFlowGroupService.assertLeafGroupForFormFlow(formFlowCommand.getFormGroupId());
        Long id = formFlowCommand.getId();
        // 发布表单定义
        String formKey = StringUtils.EMPTY_STRING;
        if (Objects.nonNull(formFlowCommand.getId())) {
            FormFlowPO formFlowPO = this.getById(id);
            if (Objects.nonNull(formFlowPO)) {
                formKey = formFlowPO.getFormKey();
                formFlowCommand.setStatus(formFlowPO.getStatus());
                ObjectUtils.copying(formFlowCommand, formFlowPO);
                String flowKey = this.deployFlow(formFlowCommand, formFlowPO.getFlowKey());
                formFlowPO.setFlowKey(flowKey);
                //删除之前二维码连接
                if (Objects.nonNull(formFlowPO.getQrImage())) {
                    fileClient.remove(formFlowPO.getQrImage());
                    formFlowPO.setQrImage(null);
                }
                this.updateById(formFlowPO);
            }
        } else {
            FormFlowPO formFlowPO = ObjectUtils.copying(formFlowCommand, FormFlowPO.class);
            formKey = this.generateFormKey();
            String flowKey = this.deployFlow(formFlowCommand, StringUtils.EMPTY_STRING);
            formFlowPO.setId(IdWorker.getId());
            formFlowPO.setFormKey(formKey);
            formFlowPO.setFlowKey(flowKey);
            formFlowPO.setStatus(FormFlowStatusEnum.NOT_USED.name());
            this.save(formFlowPO);
            id = formFlowPO.getId();
        }
        //保存角色权限
        formPermissionService.saveOrUpdate(id, formFlowCommand.getPermissionType(), formFlowCommand.getPermissionIds());
        //保存webhook配置
        List<WebhookConfigVO> webhookList = webhookConfigService.getWebhookList(id);
        if (CollectionUtils.isNotEmpty(formFlowCommand.getWebhookConfigVOList())) {
            if (CollectionUtils.isNotEmpty(webhookList)) {
                //更新的id
                List<Long> existWebHookId = formFlowCommand.getWebhookConfigVOList().stream().filter(t -> Objects.nonNull(t.getId()))
                        .map(WebhookConfigVO::getId).collect(Collectors.toList());
                //过滤更新的进行删除
                Set<Long> deleteWebHookIds = webhookList.stream().filter(t -> !existWebHookId.contains(t.getId()))
                        .map(WebhookConfigVO::getId).collect(Collectors.toSet());
                if (CollectionUtils.isNotEmpty(deleteWebHookIds)) {
                    DatabaseContextHolder.softDelete(WebhookConfig.class,
                                                     deleteWebHookIds);
                }
            }
            webhookConfigService.saveOrUpdate(id.toString(), formFlowCommand.getWebhookConfigVOList());
        } else {
            if (CollectionUtils.isNotEmpty(webhookList)) {
                DatabaseContextHolder.softDelete(WebhookConfig.class,
                                                 webhookList.stream().map(WebhookConfigVO::getId).collect(Collectors.toSet()));
            }
        }
        this.deployForm(formFlowCommand, formKey);
        return id;
    }

    private String deployFlow(FormFlowCommand formFlowCommand, String flowKey) {
        //TODO 发布流程定义返回flowKey
        if (!FormFlowTypeEnum.FORM.name().equals(formFlowCommand.getType())) {
            FlowEngineDTO flowEngineDTO = new FlowEngineDTO();
            flowEngineDTO.setFormName(formFlowCommand.getName());
            flowEngineDTO.setProcess(formFlowCommand.getFlowJson());
            flowEngineDTO.setFlowKey(flowKey);
            ProcessDefinitionDTO processDefinitionDTO = processDefinitionCommandService.deployByJSON(flowEngineDTO);
            return processDefinitionDTO.getKey();
        }
        return StringUtils.EMPTY_STRING;
    }

    private void deployForm(FormFlowCommand formFlowCommand, String formKey) {
        if (!FormFlowTypeEnum.FLOW.name().equals(formFlowCommand.getType())) {
            definitionCommandService.publish(PublishFormDefinitionCommand.builder()
                                                     .name(formFlowCommand.getName())
                                                     .application(formFlowCommand.getApplication())
                                                     .type(formFlowCommand.getType())
                                                     .key(formKey)
                                                     .jsonSchema(formFlowCommand.getFormJson())
                                                     .remark(formFlowCommand.getRemark())
                                                     .build()
                                            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void updateStatus(Long formFlowId, boolean actionRemove) {
        if (Objects.isNull(formFlowId)) {
            return;
        }
        // 能够更改状态的不是删除操作，就是增加操作；凡是增加关联关系，那么表单的状态肯定是改为使用中
        if (!actionRemove) {
            this.lambdaUpdate().eq(FormFlowPO::getId, formFlowId).set(FormFlowPO::getStatus, FormFlowStatusEnum.IN_USE.name()).update();
        } else {
            if (!formFlowBusinessQueryService.isReferenced(formFlowId)) {
                this.lambdaUpdate().eq(FormFlowPO::getId, formFlowId).set(FormFlowPO::getStatus, FormFlowStatusEnum.NOT_USED.name())
                        .update();
            }
        }
    }

    @Override
    public Boolean enable(Long formFlowId, Boolean enable) {
        if (null == formFlowId || null == enable) {
            return false;
        }
        FormFlowPO formFlowPO = this.getById(formFlowId);
        if (null == formFlowPO) {
            return false;
        }
        if (!enable && FormFlowStatusEnum.IN_USE.name().equals(formFlowPO.getStatus())) {
            // 停用需要判断当前是否有流程正在进行中，若有不能操作停用并提示“当前有流程正在进行中，不支持停用操作”
            // 仅表单的时候通过表单实例的业务状态判断
            boolean required = true;
            if (FormFlowTypeEnum.FORM.name().equals(formFlowPO.getType())) {
                Long count = formInstanceMapper.selectCount(Wrappers.<FormInstancePO>lambdaQuery()
                                                                    .eq(FormInstancePO::getFormFlowId, formFlowId)
                                                                    .eq(FormInstancePO::getBusinessStatus, FormInstanceStatusEnum.IN_PROGRESS.name()));
                required = count < 1;
            } else {
                //TODO 流程表单、仅流程通过流程key查询是否有正在进行中的实例
                required = processInstanceQueryService.isRunning(formFlowPO.getFlowKey());
            }
            Assert.isNotFound(required, "当前有流程正在进行中，不支持停用操作");
        }
        return this.lambdaUpdate().eq(FormFlowPO::getId, formFlowId).set(FormFlowPO::getEnable, enable).update();
    }

    @Override
    public Boolean delete(Long formFlowId) {
        FormFlowPO formFlowPO = this.getById(formFlowId);
        if (formFlowPO == null) {
            return false;
        }
        boolean required = processInstanceQueryService.isRunning(formFlowPO.getFlowKey());
        Assert.isNotFound(required, "当前有流程正在进行中，不支持删除操作");
        if (FormFlowStatusEnum.NOT_USED.name().equals(formFlowPO.getStatus())
            || FormFlowTypeEnum.CUSTOM_FORM.name().equals(formFlowPO.getType())) {
            // 删除定义
            definitionCommandService.lambdaUpdate().eq(FormDefinitionPO::getKey, formFlowPO.getFormKey())
                    .set(FormDefinitionPO::getAbandon, true).update();
            return this.lambdaUpdate().set(FormFlowPO::getStatus, FormFlowStatusEnum.DELETED)
                    .eq(FormFlowPO::getId, formFlowId)
                    .update();
        } else {
            throw new ServiceException("表单正在使用中，不支持删除操作");
        }
    }

    /**
     * 生成表单定义key
     */
    private synchronized String generateFormKey() {
        return IdWorker.getIdStr();
    }

}
