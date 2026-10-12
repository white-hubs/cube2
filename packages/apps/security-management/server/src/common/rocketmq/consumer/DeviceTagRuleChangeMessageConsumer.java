package com.guhecloud.cube.srv.gen.securitymanagement.server.common.rocketmq.consumer;

import com.guhecloud.cube.base.device.center.metadata.event.DeviceTagRuleChange;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.convert.SecurityDeviceConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.util.SecurityManagementCommonUtil;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "security_device_tag_rule_update",
                         topic = DeviceTagRuleChange.TOPIC, selectorType = SelectorType.TAG
)
public class DeviceTagRuleChangeMessageConsumer implements RocketMQListener<DeviceTagRuleChange> {

    private final DeviceCenterStdClient deviceCenterStdClient;

    private final SecurityCommonMapper securityCommonMapper;

    private static final Integer maxDeviceNumber = 5000;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onMessage(DeviceTagRuleChange message) {
        //设备打标签
        String appCode = message.getTagCode().replace(SecurityManagementCommonUtil.applicationConstants, "");
        securityCommonMapper.delAppDevices(appCode, System.currentTimeMillis());
        List<Long> binDeviceIds = this.securityCommonMapper.selectDeviceIds(appCode);
        //根据标签获取设备,若存在分页的情况需多次获取
        DeviceFilterPageParam deviceFilterPageParam = new DeviceFilterPageParam();
        deviceFilterPageParam.setTagCodes(Collections.singletonList(message.getTagCode()));
        deviceFilterPageParam.setPageNumber(1);
        deviceFilterPageParam.setPageSize(maxDeviceNumber);
        Pagination<DevicePageInfoVO> devicePageInfoVOPagination = this.deviceCenterStdClient
                .deviceInfoPage(deviceFilterPageParam);
        if (CollectionUtils.isEmpty(devicePageInfoVOPagination.getRecords())) {
            return;
        }
        List<DevicePageInfoVO> devicePageInfoVOS = devicePageInfoVOPagination.getRecords();
        for (int statPage = 1; statPage <= devicePageInfoVOPagination.getPages(); statPage++) {
            if (statPage > 1) {
                //再次查询除第一页后的数据
                deviceFilterPageParam.setPageNumber(statPage);
                deviceFilterPageParam.setPageSize(maxDeviceNumber);
                log.info("时间开始{}", System.currentTimeMillis());
                devicePageInfoVOS = this.deviceCenterStdClient.deviceInfoPage(deviceFilterPageParam).getRecords();
                log.info("时间结束{}", System.currentTimeMillis());
            }
            List<SecurityDevice> copying = SecurityDeviceConvert.convert(binDeviceIds, appCode, devicePageInfoVOS);
            this.securityCommonMapper.insertBatch(copying);
        }
    }
}
