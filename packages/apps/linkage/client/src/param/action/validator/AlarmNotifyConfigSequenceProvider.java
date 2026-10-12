package com.guhecloud.cube.biz.linkage.client.param.action.validator;

import com.guhecloud.cube.biz.linkage.client.param.action.AlarmNotifyActionParam;
import com.guhecloud.cube.biz.linkage.client.validator.provider.ActionValidatorProvider;
import com.guhecloud.cube.biz.linkage.client.validator.util.GroupUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class AlarmNotifyConfigSequenceProvider implements ActionValidatorProvider, DefaultGroupSequenceProvider<AlarmNotifyActionParam.AlarmNotifyConfigParam> {

    @Override
    public List<Class<?>> getValidationGroups(Class<?> klass, AlarmNotifyActionParam.AlarmNotifyConfigParam alarmNotifyConfigParam) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(AlarmNotifyActionParam.AlarmNotifyConfigParam.class);
        if (alarmNotifyConfigParam != null) {
            Boolean groupId = (Boolean) GroupUtil.getCurrentRequestValue();
            //启用状态继续执行校验逻辑
            if (Objects.nonNull(groupId)) {
                log.info("启用状态继续执行校验逻辑");
                addGroupSequence(groupId, defaultGroupSequence);
            }
        }
        return defaultGroupSequence;
    }
}
