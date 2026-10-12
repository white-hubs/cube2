package com.guhecloud.cube.base.device.center.scene.rocketmq.consumer;

import com.guhecloud.cube.base.device.center.metadata.lang.DeviceChangeEnum;
import com.guhecloud.cube.base.device.center.open.mapper.DeviceCenterMapper;
import com.guhecloud.cube.base.device.center.scene.model.DeviceSceneConfig;
import com.guhecloud.cube.base.device.center.scene.rocketmq.message.DeviceChangeEventMessage;
import com.guhecloud.cube.base.device.center.scene.service.DeviceSceneConfigService;
import com.guhecloud.cube.base.device.center.scene.service.VisualSceneTagService;
import com.guhecloud.cube.biz.mdata.client.SceneTypeClient;
import com.guhecloud.cube.biz.mdata.client.dto.SceneTypeDTO;
import com.guhecloud.cube.biz.mdata.client.param.SceneTypeQueryParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * todo新增设备后的同步新增场景标签
 *
 * @author lanchun
 * @date 2024/1/15 17:41
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "device_visual_scene_tag_group", topic = DeviceChangeEventMessage.TOPIC)
public class DeviceChangeSceneListenConsumer implements RocketMQListener<DeviceChangeEventMessage> {

    private final DeviceSceneConfigService deviceSceneConfigService;

    private final SceneTypeClient sceneTypeClient;

    private final DeviceCenterMapper deviceCenterMapper;

    private final VisualSceneTagService visualSceneTagService;

    @Override
    public void onMessage(DeviceChangeEventMessage message) {
        //目前只考虑新增设备的情况,不考虑编辑,编辑是不允许修改产品型号的
        message.getDeviceChangePairs().stream().forEach(createDevice -> {
            String prodCategoryCode = deviceCenterMapper.getProdCategoryCodeByProductId(createDevice.getKey().getDeviceProductId());
            List<DeviceSceneConfig> deviceSceneConfigs = deviceSceneConfigService.listBusinessSceneConfigByBusinessId(prodCategoryCode);
            if (DeviceChangeEnum.CREATE == createDevice.getVal() &&
                !CollectionUtils.isEmpty(deviceSceneConfigs)) {
                List<Long> sceneTypeIds = deviceSceneConfigs.stream().map(DeviceSceneConfig::getSceneType).collect(Collectors.toList());
                SceneTypeQueryParam sceneTypeQueryParam = new SceneTypeQueryParam();
                sceneTypeQueryParam.setIds(sceneTypeIds)
                ;
                List<SceneTypeDTO> sceneTypeDTOS = sceneTypeClient.listSceneTypes(sceneTypeQueryParam);
                for (SceneTypeDTO sceneTypeDTO : sceneTypeDTOS) {
                    visualSceneTagService.bindSceneTag(Collections.singleton(createDevice.getKey()
                                                                                     .getDeviceId()), sceneTypeDTO.getId(), sceneTypeDTO.getName());
                }
            }
        });
    }
}
