package com.guhecloud.cube.base.flow.dcm.support;

import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowCommandService;
import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowCommand;
import com.guhecloud.cube.base.flow.context.po.FormFlowPO;
import com.guhecloud.cube.base.flow.dcm.entity.FormFlowDCM;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.cube.base.flow.dcm.support.FlowDcmSupport.CONFIG_TYPE_FORM;

@Slf4j
public class FlowDcmHelper extends AbstractSimpleDcmHelper<FormFlowPO, FormFlowDCM> {
    private final FormFlowCommandService formFlowCommandService;
    private final Map<String, Long> formFlowIdByName;

    public FlowDcmHelper(FormFlowCommandService formFlowCommandService, List<FormFlowPO> formFlowPOList) {
        super(FormFlowPO.class, FormFlowDCM.class);
        this.formFlowCommandService = formFlowCommandService;
        this.formFlowIdByName = formFlowPOList.stream().collect(Collectors.toMap(FormFlowPO::getName, FormFlowPO::getId));
    }

    public static IDcmRciConverter<FormFlowDCM> createDcmRciConverter() {
        return new FlowDcmHelper(null, Collections.emptyList());
    }

    @Override
    public String getBizKey(FormFlowPO formFlowPO) {
        return formFlowPO.getName();
    }

    @Override
    protected FormFlowDCM dcmFromMap(Map<String, Object> object) {
        return FormFlowDCM.from(object);
    }

    //

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_FORM;
    }

    @Override
    public void updateBizWithDcm(FormFlowPO formFlowPO, FormFlowDCM formFlowDCM) {
        formFlowPO.setName(formFlowDCM.getName());
        formFlowPO.setStatus(formFlowDCM.getStatus());
        formFlowPO.setApplication(formFlowDCM.getApplication());
        formFlowPO.setAutoRemoveDuplication(formFlowDCM.getAutoRemoveDuplication());
        formFlowPO.setType(formFlowDCM.getType());
        formFlowPO.setRemark(formFlowDCM.getRemark());
    }

    public int batchUpdate(Collection<FormFlowPO> bizEntities, Collection<FormFlowDCM> dcmEntities) {
        Map<String, FormFlowDCM> formFlowDCMMap = dcmEntities.stream()
                .collect(Collectors.toMap(FormFlowDCM::getName, Function.identity()));
        for (FormFlowPO bizEntity : bizEntities) {
            FormFlowCommand copying = ObjectUtils.copying(bizEntity, FormFlowCommand.class);
            FormFlowDCM orDefault = formFlowDCMMap.getOrDefault(bizEntity.getName(), new FormFlowDCM());
            copying.setId(formFlowIdByName.get(copying.getName()));
            copying.setFlowJson(orDefault.getFlowJson());
            copying.setFormJson(orDefault.getFormJson());
            formFlowCommandService.saveOrUpdate(copying);
        }
        return bizEntities.size();
    }

    public int batchAdd(Collection<FormFlowPO> bizEntities, Collection<FormFlowDCM> dcmEntities) {
        Map<String, FormFlowDCM> formFlowDCMMap = dcmEntities.stream()
                .collect(Collectors.toMap(FormFlowDCM::getName, Function.identity()));
        for (FormFlowPO bizEntity : bizEntities) {
            FormFlowCommand copying = ObjectUtils.copying(bizEntity, FormFlowCommand.class);
            FormFlowDCM orDefault = formFlowDCMMap.getOrDefault(bizEntity.getName(), new FormFlowDCM());
            copying.setFlowJson(orDefault.getFlowJson());
            copying.setFormJson(orDefault.getFormJson());
            formFlowCommandService.saveOrUpdate(copying);
        }
        return bizEntities.size();
    }

    public int batchRemove(Collection<FormFlowPO> bizEntities, Collection<FormFlowDCM> dcmEntities) {
        for (FormFlowPO bizEntity : bizEntities) {
            formFlowCommandService.delete(formFlowIdByName.get(bizEntity.getName()));
        }
        return bizEntities.size();
    }
}
