package com.guhecloud.cube.base.flow.form.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.form.commandgateway.FormInstanceCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.FormInstanceDataCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormInstanceDataCommand;
import com.guhecloud.cube.base.flow.form.dao.FormInstanceDataMapper;
import com.guhecloud.cube.base.flow.form.enumeration.FormInstanceStatusEnum;
import com.guhecloud.cube.base.flow.form.po.FormInstanceDataPO;
import com.guhecloud.cube.base.flow.form.po.FormInstancePO;
import com.guhecloud.cube.base.flow.form.resolver.FormResultDataResolver;
import com.guhecloud.cube.base.flow.form.rocketmq.produce.FormEventProduce;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author bq
 * @date 2022/09/08
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormInstanceDataCommandServiceImpl extends ServiceImpl<FormInstanceDataMapper, FormInstanceDataPO> implements FormInstanceDataCommandService {

    private final FormResultDataResolver formResultDataResolver;
    private final FormEventProduce formEventProduce;
    private final FormInstanceCommandService formInstanceCommandService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdateData(FormInstanceDataCommand formInstanceDataCommand) {
        //TODO 处理的不完善
        FormInstanceDataPO dataPO = ObjectUtils.copying(formInstanceDataCommand, FormInstanceDataPO.class);
        String dataJson = formResultDataResolver.mergeDataListJson(formInstanceDataCommand.getFormData());
        String parseToBusinessObject = formResultDataResolver.parse(formInstanceDataCommand.getFormInstanceId(), dataJson);
        dataPO.setBusinessData(parseToBusinessObject);
        dataPO.setFormData(dataJson);
        if (null == formInstanceDataCommand.getId()) {
            // 现在默认是业务结束之后保存一份表单数据，所以这里可以确认业务的状态为已结束
            this.remove(Wrappers.<FormInstanceDataPO>lambdaQuery()
                                .eq(FormInstanceDataPO::getFormInstanceId, formInstanceDataCommand.getFormInstanceId()));
            this.save(dataPO);
            // ==》更新实例的业务状态
            formInstanceCommandService.lambdaUpdate().eq(FormInstancePO::getId, formInstanceDataCommand.getFormInstanceId())
                    .set(FormInstancePO::getBusinessStatus, FormInstanceStatusEnum.COMPLETED.name()).update();
        } else {
            this.updateById(dataPO);
        }
        formEventProduce.sendFormDataToBusinessEvent(formInstanceDataCommand.getFormInstanceId(), parseToBusinessObject);
        return parseToBusinessObject;
    }

    @Override
    public String parseFormInstanceDataJson(Long formInstanceId, String dataJson) {
        return formResultDataResolver.parse(formInstanceId, dataJson);
    }

}
