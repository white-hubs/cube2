package com.guhecloud.cube.biz.vmonitor.server.controller;

import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.StreamMediaConfigClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StreamMediaConfigDTO;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.request.CameraDeviceQueryPageRequest;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.response.DeviceMonitoringResponse;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.service.CameraSecurityDeviceQueryService;
import com.guhecloud.cube.biz.vmonitor.server.entity.MultipleVideoCollectCamera;
import com.guhecloud.cube.biz.vmonitor.server.param.MultipleVideoCollectCameraModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.param.MultipleVideoCollectQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.service.MultipleVideoCollectCameraService;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraMultipleVideoPageVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.StreamMediaVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MultipleVideoCollectCameraController {

    private final MultipleVideoCollectCameraService multipleVideoCollectCameraService;

    private final StreamMediaConfigClient streamMediaConfigClient;

    private final CameraSecurityDeviceQueryService cameraSecurityDeviceQueryService;

    private final AreaClient areaClient;

    /**
     * 用户收藏视频
     *
     * @param param
     */
    @PostMapping("/multiple-video-collect-camera")
    public void save(@RequestBody MultipleVideoCollectCameraModifyParam param) {
        this.multipleVideoCollectCameraService.save(param);
    }

    /**
     * 用户收藏视频分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/multiple-video-collect-camera/page")
    public Pagination<CameraMultipleVideoPageVO> getUserCollectVideos(@Valid MultipleVideoCollectQueryParam param) {
        List<MultipleVideoCollectCamera> multipleVideoCollectCameras = this.multipleVideoCollectCameraService.listUserMultipleVideosBySource(SecurityContextHolder
                                                                                                                                                     .getPrincipal()
                                                                                                                                                     .getId(),
                                                                                                                                             SessionContextHolder
                                                                                                                                                     .getRequester()
                                                                                                                                                     .getTerminal());
        if (CollectionUtils.isEmpty(multipleVideoCollectCameras)) {
            return Pagination.empty();
        }
        List<Long> deviceIds = multipleVideoCollectCameras.stream().map(MultipleVideoCollectCamera::getDeviceId)
                .collect(Collectors.toList());
        List<CameraMultipleVideoPageVO> cameraMultipleVideoPageVOS = Collections.synchronizedList(new ArrayList<>());
        CameraDeviceQueryPageRequest deviceParam = new CameraDeviceQueryPageRequest();
        deviceParam.setKeyword(param.getKeyword());
        deviceParam.setDeviceIds(deviceIds);
        deviceParam.setAreaIds(param.getAreaId());
        deviceParam.setTagCodes(param.getTagCodes());
        Pagination<DeviceMonitoringResponse> insideDeviceMonitoringPage = this.cameraSecurityDeviceQueryService.queryCameraDevicePage(deviceParam);
        StreamMediaConfigDTO streamMediaConfigDTO = this.streamMediaConfigClient.getStreamMediaConfig();
        insideDeviceMonitoringPage.getRecords().forEach(deviceMonitoringVo -> {
            CameraMultipleVideoPageVO cameraMultipleVideoPageVO = CameraMultipleVideoPageVO.builder()
                    .id(deviceMonitoringVo.getDeviceId())
                    .code(deviceMonitoringVo.getCode())
                    .name(deviceMonitoringVo.getDeviceName())
                    .product(ObjectUtils.copying(deviceMonitoringVo.getProduct(), CameraMultipleVideoPageVO.DeviceProductVO.class))
                    .areaId(deviceMonitoringVo.getAreaId())
                    .deviceStatus(deviceMonitoringVo.getDeviceStatus())
                    .streamMedia(StreamMediaVO.convertStreamMediaInfo(streamMediaConfigDTO))
                    .thirdPartyId(deviceMonitoringVo.getThirdPartyId())
                    .tags(ObjectUtils.copying(deviceMonitoringVo.getTags(), CameraMultipleVideoPageVO.DeviceTagVO.class))
                    .build();
            if (StringUtils.isNotBlank(deviceMonitoringVo.getAreaId())) {
                Set<Long> areaIds = Arrays.stream(deviceMonitoringVo.getAreaId().split("-"))
                        .map(Long::valueOf).collect(Collectors.toSet());
                Map<Long, String> areaIdNameMapping = areaClient.getByIds(areaIds).stream()
                        .collect(Collectors.toMap(AreaDTO::getId, AreaDTO::getName));
                String areaNames = Arrays.stream(deviceMonitoringVo.getAreaId().split("-")).map(areaId -> {
                    if (areaIdNameMapping.containsKey(Long.parseLong(areaId))) {
                        return areaIdNameMapping.get(Long.parseLong(areaId));
                    }
                    return "";
                }).collect(Collectors.joining("-"));
                cameraMultipleVideoPageVO.setAreaName(areaNames);
            }
            cameraMultipleVideoPageVOS.add(cameraMultipleVideoPageVO);
        });
        return new Pagination<>(insideDeviceMonitoringPage.getTotal(), insideDeviceMonitoringPage.getPages(), cameraMultipleVideoPageVOS);
    }

    /**
     * 用户取消收藏视频
     *
     * @param deviceId
     */
    @DeleteMapping("/multiple-video-collect-camera/{deviceId}")
    public void remove(@PathVariable("deviceId") Long deviceId) {
        this.multipleVideoCollectCameraService.removeBySource(SecurityContextHolder.getPrincipal().getId(),
                                                              SessionContextHolder.getRequester().getTerminal(),
                                                              deviceId);
    }
}
