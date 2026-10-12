package com.guhecloud.cube.biz.linkage.client.param.action.validator;

import com.guhecloud.cube.biz.linkage.client.param.action.DeviceCtlActionParam;
import com.guhecloud.cube.biz.linkage.client.validator.provider.ActionValidatorProvider;
import com.guhecloud.cube.biz.linkage.client.validator.util.GroupUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DeviceCtlConfigSequenceProvider implements ActionValidatorProvider, DefaultGroupSequenceProvider<DeviceCtlActionParam.LinkageIotDeviceProParam> {

    @Override
    public List<Class<?>> getValidationGroups(Class<?> klass, DeviceCtlActionParam.LinkageIotDeviceProParam linkageIotDeviceProParam) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(DeviceCtlActionParam.LinkageIotDeviceProParam.class);
        if (linkageIotDeviceProParam != null) {
            Boolean groupId = (Boolean) GroupUtil.getCurrentRequestValue();
            //启用状态继续执行校验逻辑
            log.info("启用状态继续执行校验逻辑");
            addGroupSequence(groupId, defaultGroupSequence);
        }
        return defaultGroupSequence;
    }
}
