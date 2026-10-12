package com.guhecloud.cube.base.flow.context.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowBusinessCommandService;
import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowCommandService;
import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowBusinessCommand;
import com.guhecloud.cube.base.flow.context.dao.FormFlowBusinessMapper;
import com.guhecloud.cube.base.flow.context.po.FormFlowBusinessPO;
import com.guhecloud.cube.base.flow.form.commandgateway.FormControlBusinessCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormControlBusinessCommand;
import com.guhecloud.cube.base.flow.form.po.FormControlBusinessPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author bq
 * @date 2022/10/28
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormFlowBusinessCommandServiceImpl extends ServiceImpl<FormFlowBusinessMapper, FormFlowBusinessPO> implements FormFlowBusinessCommandService {

    private final FormFlowBusinessMapper formFlowBusinessMapper;
    private final FormFlowCommandService formFlowCommandService;
    private final FormControlBusinessCommandService formControlBusinessCommandService;

    @Override
    public void saveOrUpdate(FormFlowBusinessCommand formFlowBusinessCommand) {
        // 查询原有的关系
        FormFlowBusinessPO originPO = formFlowBusinessMapper.selectOne(Wrappers.<FormFlowBusinessPO>lambdaQuery()
                                                                               .eq(FormFlowBusinessPO::getBusinessType, formFlowBusinessCommand.getBusinessType()));
        // 根据业务类型删除原有的绑定
        formFlowBusinessMapper.delete(Wrappers.<FormFlowBusinessPO>lambdaQuery()
                                              .eq(FormFlowBusinessPO::getBusinessType, formFlowBusinessCommand.getBusinessType()));
        // 如果formFlowId不为空就添加新的绑定关系
        if (Objects.nonNull(formFlowBusinessCommand.getFormFlowId())) {
            formFlowBusinessMapper.insert(ObjectUtils.copying(formFlowBusinessCommand, FormFlowBusinessPO.class));
            formFlowCommandService.updateStatus(formFlowBusinessCommand.getFormFlowId(), false);
        }
        formFlowCommandService.updateStatus(Objects.isNull(originPO) ? null : originPO.getFormFlowId(), true);
        //调用控件与业务绑定service
        Assert.isParameterError(CollectionUtils.isEmpty(formFlowBusinessCommand.getFieldMapList()), "没有表单映射数据，无法绑定");
        //根据表单定义ID 删除之前的绑定关系
        formControlBusinessCommandService.remove(Wrappers.<FormControlBusinessPO>lambdaQuery()
                                                         .eq(FormControlBusinessPO::getFormDefinitionId, formFlowBusinessCommand.getFormDefinitionId()));

        formFlowBusinessCommand.getFieldMapList().stream().forEach(m -> {
            formControlBusinessCommandService.saveOrUpdate(FormControlBusinessCommand.builder()
                                                                   .formDefinitionId(formFlowBusinessCommand.getFormDefinitionId())
                                                                   .controlId(m.getControlId())
                                                                   .businessField(m.getBusinessField())
                                                                   .controlType(m.getControlType())
                                                                   .build());
            if (ObjectUtils.isNotEmpty(m.getChildren())) {
                m.getChildren().stream().forEach(children -> {
                    formControlBusinessCommandService.saveOrUpdate(FormControlBusinessCommand.builder()
                                                                           .formDefinitionId(formFlowBusinessCommand.getFormDefinitionId())
                                                                           .controlId(children.getControlId())
                                                                           .businessField(children.getBusinessField())
                                                                           .controlType(children.getControlType())
                                                                           .build());
                });
            }
        });
    }

}
