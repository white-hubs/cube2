package com.guhecloud.cube.base.device.center.open.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.std.vo.AssociatedVideoVO;
import com.guhecloud.cube.base.device.center.open.mapper.DeviceCenterAssociatedMapper;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.VIDEO_DEVICE_TAG_CODE;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceCenterAssociatedService {

    private final DeviceCenterAssociatedMapper mapper;
    private final DeviceCenterTagService deviceCenterTagService;

    public List<AssociatedVideoVO> getAssociatedVideosByDeviceId(Long deviceInstanceId) {
        Long tagId = deviceCenterTagService.findTagId(VIDEO_DEVICE_TAG_CODE);
        return mapper.getAssociatedVideosByDeviceId(deviceInstanceId, tagId);
    }

    public Set<Long> getAssociatedDeviceIds(Long deviceInstanceId) {
        return mapper.getAssociatedDevices(deviceInstanceId);
    }

    public Set<Long> filterNotVideoDeviceIds(Set<Long> deviceIds) {
        return mapper.filterNotVideoDeviceIds(deviceIds);
    }

    public Pagination<AssociatedVideoVO> getAssociatedVideos(Paging paging) {
        Long tagId = deviceCenterTagService.findTagId(VIDEO_DEVICE_TAG_CODE);

        Page<AssociatedVideoVO> page = mapper.getAssociatedVideos(PagingUtils.convert(paging), tagId);
        return PagingUtils.convert(page);
    }
}
