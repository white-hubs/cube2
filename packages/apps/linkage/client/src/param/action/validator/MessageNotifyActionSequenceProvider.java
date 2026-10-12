package com.guhecloud.cube.biz.linkage.client.param.action.validator;

import com.guhecloud.cube.biz.linkage.client.param.action.MessageNotifyActionParam;
import com.guhecloud.cube.biz.linkage.client.validator.provider.ActionValidatorProvider;
import com.guhecloud.cube.biz.linkage.client.validator.util.GroupUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class MessageNotifyActionSequenceProvider implements ActionValidatorProvider, DefaultGroupSequenceProvider<MessageNotifyActionParam> {

    @Override
    public List<Class<?>> getValidationGroups(Class<?> klass, MessageNotifyActionParam actionParam) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(MessageNotifyActionParam.class);
        if (actionParam != null) {
            Boolean groupId = (Boolean) GroupUtil.getRequestGroupValue(actionParam);
            //启用状态继续执行校验逻辑
            if (Objects.nonNull(groupId)) {
                log.info("启用状态继续执行校验逻辑");
                addGroupSequence(groupId, defaultGroupSequence);
            }
        }
        return defaultGroupSequence;
    }
}
