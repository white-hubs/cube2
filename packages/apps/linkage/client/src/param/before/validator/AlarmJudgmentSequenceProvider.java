package com.guhecloud.cube.biz.linkage.client.param.before.validator;

import com.guhecloud.cube.biz.linkage.client.param.before.AlarmJudgmentModifyParam;
import com.guhecloud.cube.biz.linkage.client.validator.provider.ActionValidatorProvider;
import com.guhecloud.cube.biz.linkage.client.validator.util.GroupUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class AlarmJudgmentSequenceProvider implements ActionValidatorProvider, DefaultGroupSequenceProvider<AlarmJudgmentModifyParam> {

    @Override
    public List<Class<?>> getValidationGroups(Class<?> klass, AlarmJudgmentModifyParam param) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(AlarmJudgmentModifyParam.class);
        if (param != null) {
            Boolean groupId = (Boolean) GroupUtil.getRequestGroupValue(param);
            //启用状态继续执行校验逻辑
            if (Objects.nonNull(groupId)) {
                log.info("启用状态继续执行校验逻辑");
                addGroupSequence(groupId, defaultGroupSequence);
            }
        }
        return defaultGroupSequence;
    }
}
