package com.guhecloud.cube.base.flow.form.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.context.dao.FormFlowMapper;
import com.guhecloud.cube.base.flow.context.po.FormFlowPO;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.base.flow.form.commandgateway.FormInstanceCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormInstanceCommand;
import com.guhecloud.cube.base.flow.form.dao.FormDefinitionMapper;
import com.guhecloud.cube.base.flow.form.dao.FormInstanceDataMapper;
import com.guhecloud.cube.base.flow.form.dao.FormInstanceMapper;
import com.guhecloud.cube.base.flow.form.enumeration.FormInstanceStatusEnum;
import com.guhecloud.cube.base.flow.form.po.FormDefinitionPO;
import com.guhecloud.cube.base.flow.form.po.FormInstanceDataPO;
import com.guhecloud.cube.base.flow.form.po.FormInstancePO;
import com.guhecloud.cube.base.message2.client.MsgRecordClient;
import com.guhecloud.cube.biz.todo.client.TodoTaskClient;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import instance.event.ProcessDeleteMessage;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/09/08
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormInstanceCommandServiceImpl extends ServiceImpl<FormInstanceMapper, FormInstancePO> implements FormInstanceCommandService {

    private final FormInstanceDataMapper dataMapper;
    private final FormDefinitionMapper formDefinitionMapper;
    private final FormFlowMapper formFlowMapper;
    private final ExtProcessInstanceMapper extProcessInstanceMapper;
    private final TodoTaskClient todoTaskClient;
    private final MsgRecordClient msgRecordClient;
    private final HistoryService historyService;

    @Override
    public Long start(FormInstanceCommand formInstanceCommand) {
        FormInstancePO instancePO = ObjectUtils.copying(formInstanceCommand, FormInstancePO.class);
        // 查询表单基础信息是否存在，是否被禁用
        FormFlowPO formFlowPO = formFlowMapper.selectOne(Wrappers.<FormFlowPO>lambdaQuery()
                                                                 .eq(FormFlowPO::getFormKey, formInstanceCommand.getFormKey()));
        Assert.isNotFound(formFlowPO, "流程表单不存在!");
        Assert.isNotAcceptable(!formFlowPO.getEnable(), "流程表单已禁用!");
        instancePO.setFormFlowId(formFlowPO.getId());
        instancePO.setBusinessStatus(FormInstanceStatusEnum.IN_PROGRESS.name());
        List<FormDefinitionPO> formDefinitionPOS = formDefinitionMapper.selectList(Wrappers.<FormDefinitionPO>lambdaQuery()
                                                                                           .eq(FormDefinitionPO::getKey, formInstanceCommand.getFormKey())
                                                                                           .orderByDesc(FormDefinitionPO::getVersion)
                                                                                           .last("limit 1"));
        Assert.isNotFound(CollectionUtils.isEmpty(formDefinitionPOS), "表单定义不存在");
        // 启动表单实例的时候，如果未指定表单定义的版本就使用最新的，反之则用指定版本
        if (null == formInstanceCommand.getVersion()) {
            instancePO.setVersion(formDefinitionPOS.getFirst().getVersion());
        }
        this.save(instancePO);
        return instancePO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 删除实例的数据
        new LambdaUpdateChainWrapper<>(dataMapper).eq(FormInstanceDataPO::getFormInstanceId, id).set(BasicEntity::getDeleted, true)
                .update();
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteProcessInstance(Set<String> processInsIds) {
        long timeMillis = System.currentTimeMillis();
        List<ExtProcessInstancePO> extProcessInstancePOS = extProcessInstanceMapper.selectList(Wrappers.<ExtProcessInstancePO>lambdaQuery()
                                                                                                       .select(ExtProcessInstancePO::getId, ExtProcessInstancePO::getBusinessType)
                                                                                                       .in(ExtProcessInstancePO::getId, processInsIds));

        //删除流程实例
        extProcessInstanceMapper.update(null, Wrappers.<ExtProcessInstancePO>lambdaUpdate()
                                                .set(ExtProcessInstancePO::getDeleted, timeMillis)
                                                .in(ExtProcessInstancePO::getId, processInsIds)
                                       );
        // 获取任务
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceIdIn(new ArrayList<>(processInsIds)).list();
        // 清除待办和消息
        try {
            msgRecordClient.removeBatchByBusinessIds(processInsIds);
            if (!CollectionUtils.isEmpty(historicTaskInstances)) {
                Set<String> businessIds = historicTaskInstances.stream().map(HistoricTaskInstance::getId).collect(Collectors.toSet());
                todoTaskClient.removeBatchByBusinessIds(businessIds);
            }
            Map<String, List<String>> groupByBusinessType = extProcessInstancePOS.stream()
                    .collect(Collectors.groupingBy(ExtProcessInstancePO::getBusinessType,
                                                   Collectors.mapping(ExtProcessInstancePO::getId, Collectors.toList())));
            //删除业务表单
            for (Map.Entry<String, List<String>> entry : groupByBusinessType.entrySet()) {
                RocketMQContextHolder.send(ProcessDeleteMessage.TOPIC + ":" + entry.getKey(),
                                           ProcessDeleteMessage.builder()
                                                   .processInstanceId(entry.getValue())
                                                   .build());
            }
        } catch (Exception e) {
            log.error("remove process instance error：{}", e);
        }
        return false;
    }

}
