package com.guhecloud.cube.base.device.center.core.service;

import com.guhecloud.cube.base.device.center.core.dto.DeviceRefInfoDTO;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceRefMapper;
import com.guhecloud.cube.base.device.center.core.model.BizDevice;
import com.guhecloud.cube.base.device.center.core.param.CreateDeviceRefParam;
import com.guhecloud.cube.base.device.center.metadata.param.TagBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.param.TagUnBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagCmd;
import com.guhecloud.platform.base.standard.exception.ResourcePreconditionFailedException;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.VIDEO_DEVICE_TAG_CODE;
import static com.guhecloud.cube.base.device.center.metadata.lang.Const.VIDEO_DEVICE_TAG_TYPE;

@Service
public class CenterDeviceRefService {

    @Resource
    private CenterBizDeviceRefMapper centerBizDeviceRefMapper;

    @Resource
    private CenterBizDeviceMapper centerBizDeviceMapper;

    @Resource
    private CenterDeviceTagService centerDeviceTagService;

    /**
     * 根据设备ID查询关联设备ID
     *
     * @param deviceId 设备ID
     *
     * @return 关联设备ID
     */
    public DeviceRefInfoDTO findDeviceRefInfo(Long deviceId) {
        BizDevice bizDevice = centerBizDeviceMapper.selectById(deviceId);
        if (Objects.isNull(bizDevice)) {
            throw new ResourcePreconditionFailedException(String.valueOf(deviceId), "设备不存在");
        }

        DeviceRefInfoDTO deviceRefInfoDTO = new DeviceRefInfoDTO();
        deviceRefInfoDTO.setDeviceId(deviceId);
        deviceRefInfoDTO.setRefDeviceIds(centerBizDeviceRefMapper.findRefDeviceIds(deviceId));
        return deviceRefInfoDTO;
    }

    public boolean refDevice(Long deviceId, CreateDeviceRefParam createDeviceRefParam) {
        List<String> oldRefDeviceIds = centerBizDeviceRefMapper.findRefDeviceIds(deviceId);
        centerBizDeviceRefMapper.removeRefDevice(deviceId, LocalDateTime.now());

        if (Objects.nonNull(createDeviceRefParam.getRefDeviceIds()) &&
            !createDeviceRefParam.getRefDeviceIds().isEmpty()) {

            for (Long refDeviceId : createDeviceRefParam.getRefDeviceIds()) {
                centerBizDeviceRefMapper.saveRef(deviceId, refDeviceId, SecurityContextHolder.getPrincipal().getAccount());
            }
        }

        // 创建视频标签
        centerDeviceTagService.createTagIfNeed(buildCreateTagParam());

        // 移除旧的视频标签关联
        List<Long> oldRefDeviceIdLongs = (Objects.isNull(oldRefDeviceIds) || oldRefDeviceIds.isEmpty()) ?
                                         Collections.emptyList() : oldRefDeviceIds.stream().map(Long::parseLong)
                                                 .toList();
        centerDeviceTagService.unbindTag(TagUnBindDeviceCmd.of(VIDEO_DEVICE_TAG_TYPE, VIDEO_DEVICE_TAG_CODE, new HashSet<>(oldRefDeviceIdLongs)));

        // 绑定新的视频标签关联
        List<Long> newRefDeviceIdLongs =
                (Objects.isNull(createDeviceRefParam.getRefDeviceIds()) || createDeviceRefParam.getRefDeviceIds().isEmpty()) ?
                Collections.emptyList() : new ArrayList<>(createDeviceRefParam.getRefDeviceIds());
        centerDeviceTagService.bindTag(TagBindDeviceCmd.of(VIDEO_DEVICE_TAG_CODE, new HashSet<>(newRefDeviceIdLongs)));

        return true;
    }

    private CreateTagCmd buildCreateTagParam() {
        CreateTagCmd createTagParam = new CreateTagCmd();
        createTagParam.setTagCode(VIDEO_DEVICE_TAG_CODE);
        createTagParam.setTagName("视频标签");
        return createTagParam;
    }
}
