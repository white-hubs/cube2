package com.guhecloud.cube.base.device.center.scene.service;

import com.guhecloud.cube.base.device.center.core.service.CenterDeviceTagService;
import com.guhecloud.cube.base.device.center.metadata.param.TagBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.param.TagUnBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterParam;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceInfoVO;
import com.guhecloud.cube.base.device.center.open.mapper.DeviceCenterMapper;
import com.guhecloud.cube.base.device.center.open.service.DeviceCenterTagService;
import com.guhecloud.cube.base.device.center.scene.constant.VisualConfigConstants;
import com.guhecloud.cube.base.device.center.scene.param.BusinessSceneConfigModifyParam;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisualSceneTagService {

    private final CenterDeviceTagService centerDeviceTagService;

    private final DeviceCenterTagService deviceCenterTagService;

    private final DeviceCenterMapper deviceCenterMapper;

    @Deprecated
    public Optional<Long> findTagTypeIdOpt() {
        return this.deviceCenterTagService.findTagTypeIdOpt(VisualConfigConstants.DEVICE_SCENE_TAG_TYPE);
    }

    public Optional<Long> findTagIdOptional(String sceneType) {
        return this.deviceCenterTagService.findTagIdOptional(this.buildTagCod(sceneType));
    }

    public void unBindSceneTag(Long sceneType, Set<Long> deviceIds) {
        //解绑tag
        TagUnBindDeviceCmd tagUnBindDeviceCmd = new TagUnBindDeviceCmd();
        tagUnBindDeviceCmd.setTagTypeCode("custom.app::visual." + VisualConfigConstants.DEVICE_SCENE_TAG_TYPE);
        tagUnBindDeviceCmd.setTagCode(this.buildTagCod(sceneType));
        tagUnBindDeviceCmd.setBusinessKeys(deviceIds);
        this.centerDeviceTagService.unbindTag(tagUnBindDeviceCmd);
    }

    public void bindSceneTag(Set<Long> deviceIds, Long sceneType, String sceneTypeName) {
        CreateTagCmd createTagCmd = CreateTagCmd.createTopTag(VisualConfigConstants.DEVICE_SCENE_TAG_TYPE, this.buildTagCod(sceneType), this.buildTagCod(
                sceneType + ".name:" + sceneTypeName));
        this.centerDeviceTagService.createTagIfNeed(createTagCmd);
        TagBindDeviceCmd tagBindDeviceCmd = new TagBindDeviceCmd();
        tagBindDeviceCmd.setTagCode(this.buildTagCod(sceneType));
        tagBindDeviceCmd.setBusinessKeys(deviceIds);
        this.centerDeviceTagService.bindTag(tagBindDeviceCmd);
    }

    public Set<Long> getSceneBindDevices(BusinessSceneConfigModifyParam param) {
        Set<Long> deviceIds = new HashSet<>();
        switch (param.getDeviceBindWay()) {
            case APPOINT_EQUIPMENT:
                deviceIds = param.getBusinessIds();
                break;
            case ALL_EQUIPMENT:
                deviceIds = getConditionFilterDeviceIds(param);
                break;
            default:
                log.debug("not found device selected way");
        }
        return deviceIds;
    }

    public Set<Long> getConditionFilterDeviceIds(BusinessSceneConfigModifyParam param) {
        DeviceFilterParam deviceFilterParam = new DeviceFilterParam();
        deviceFilterParam.setCategoryCode(param.getDeviceProdCategoryCodeConcat());
        deviceFilterParam.setKeywordLike(param.getKeyword());
        List<DeviceInfoVO> deviceInfoVOS = this.deviceCenterMapper.deviceInfoList(deviceFilterParam, Collections.emptyList());
        if (CollectionUtils.isEmpty(deviceInfoVOS)) {
            return Collections.emptySet();
        }
        return deviceInfoVOS.stream().map(DeviceInfoVO::getId).collect(Collectors.toSet());
    }

    private String buildTagCod(Long sceneType) {
        return this.buildTagCod(sceneType.toString());
    }

    private String buildTagCod(String sceneType) {
        return new StringBuilder(VisualConfigConstants.VISUAL_TAG_PREFIX)
                .append(".")
                .append(sceneType)
                .toString();
    }
}
