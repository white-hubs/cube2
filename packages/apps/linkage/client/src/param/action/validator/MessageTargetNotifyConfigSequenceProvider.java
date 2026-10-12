package com.guhecloud.cube.biz.linkage.client.param.action.validator;

import com.guhecloud.cube.biz.linkage.client.param.action.param.MessageTargetNotifyParam;
import com.guhecloud.cube.biz.linkage.client.validator.provider.ActionValidatorProvider;
import com.guhecloud.cube.biz.linkage.client.validator.util.GroupUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MessageTargetNotifyConfigSequenceProvider implements ActionValidatorProvider, DefaultGroupSequenceProvider<MessageTargetNotifyParam> {

    @Override
    public List<Class<?>> getValidationGroups(Class<?> klass, MessageTargetNotifyParam messageTargetNotifyParam) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(MessageTargetNotifyParam.class);
        if (messageTargetNotifyParam != null) {
            Boolean groupId = (Boolean) GroupUtil.getCurrentRequestValue();
            //启用状态继续执行校验逻辑
            log.info("启用状态继续执行校验逻辑");
            addGroupSequence(groupId, defaultGroupSequence);
        }
        return defaultGroupSequence;
    }
}
