package com.guhecloud.cube.biz.vmonitor.server.controller;

import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceGroupParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceGroupVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceGroupStdClient;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.StreamMediaConfigClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.auth.service.CameraAuthService;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.request.CameraDeviceQueryPageRequest;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.request.CameraDeviceQueryRequest;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.response.DeviceMonitoringResponse;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.service.CameraSecurityDeviceQueryService;
import com.guhecloud.cube.biz.vmonitor.server.entity.MultipleVideoCollectCamera;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraMultipleVideoQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.service.MultipleVideoCollectCameraService;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraAreaDeviceGroupVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraMultipleVideoPageVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.StreamMediaVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 多路视频接口
 *
 * @author lanchun
 * @date 2021/12/6 19:56
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MultipleVideoController {

    private final StreamMediaConfigClient streamMediaConfigClient;

    private final AreaClient areaClient;

    private final CameraAuthService cameraAuthService;

    private final CameraSecurityDeviceQueryService cameraSecurityDeviceQueryService;

    private final MultipleVideoCollectCameraService multipleVideoCollectCameraService;

    private final DeviceGroupStdClient deviceGroupStdClient;

    /**
     * 安全多路视频设备分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/safety-multiple-video/devices")
    public Pagination<CameraMultipleVideoPageVO> safetyListDevices(@Valid CameraMultipleVideoQueryParam param) {
        return this.getCameraMultipleVideoPageVOPagination(param, Collections.emptyList(), Collections.emptyList());
    }

    /**
     * 多路视频设备分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/multiple-video/devices")
    public Pagination<CameraMultipleVideoPageVO> listDevices(@Valid CameraMultipleVideoQueryParam param) {
        // 获取权限设备
        var deviceIds = cameraAuthService.getUserAuthScopeDeviceIds(SecurityContextHolder.getPrincipal().getId());
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Pagination.empty();
        }
        //获取已收藏设备
        var multipleVideoCollectCameras = this.multipleVideoCollectCameraService.listUserMultipleVideosBySource(SecurityContextHolder
                                                                                                                        .getPrincipal()
                                                                                                                        .getId(),
                                                                                                                SessionContextHolder
                                                                                                                        .getRequester()
                                                                                                                        .getTerminal());
        return getCameraMultipleVideoPageVOPagination(param, deviceIds, multipleVideoCollectCameras);
    }

    /**
     * 多路视频设备分组列表
     *
     * @param param 查询参数
     *
     * @return 设备分组列表
     */
    @GetMapping("/multiple-video/device-groups")
    public List<DeviceGroupVO> listDeviceGroups(QueryDeviceGroupParam param) {
        var authDeviceIds = cameraAuthService.getUserAuthScopeDeviceIds(SecurityContextHolder.getPrincipal().getId());
        if (CollectionUtils.isEmpty(authDeviceIds)) {
            return Collections.emptyList();
        }
        var deviceIds = new HashSet<>(authDeviceIds);
        if (Objects.nonNull(param) && CollectionUtils.isNotEmpty(param.getDeviceIds())) {
            deviceIds.retainAll(param.getDeviceIds());
        }
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyList();
        }
        var request = new QueryDeviceGroupParam();
        request.setDeviceIds(deviceIds);
        if (Objects.nonNull(param)) {
            request.setDeviceName(param.getDeviceName());
        }
        return deviceGroupStdClient.deviceGroupByTagAndDeviceIds(CameraSecurityDeviceQueryService.APP_TAG, request);
    }

    private Pagination<CameraMultipleVideoPageVO> getCameraMultipleVideoPageVOPagination(@Valid CameraMultipleVideoQueryParam param,
                                                                                         List<Long> deviceAuthId,
                                                                                         List<MultipleVideoCollectCamera> multipleVideoCollectCameras
                                                                                        ) {
        var request = CameraDeviceQueryPageRequest.builder()
                .pageNumber(param.getPageNumber())
                .pageSize(param.getPageSize())
                .keyword(param.getKeyword())
                .areaId(param.getAreaId())
                .deviceStatus(param.getStatus())
                .genre(param.getDeviceProdCategoryCodeConcat())
                .deviceIds(deviceAuthId)
                .tagCodes(param.getTagCodes())
                .build();
        var deviceMonitoringVoPagination = this.cameraSecurityDeviceQueryService.queryCameraDevicePage(request);
        if (CollectionUtils.isEmpty(deviceMonitoringVoPagination.getRecords())) {
            return Pagination.empty();
        }
        var streamMediaConfigDTO = this.streamMediaConfigClient.getStreamMediaConfig();
        var cameraMultipleVideoPageVOS = new ArrayList<CameraMultipleVideoPageVO>(deviceMonitoringVoPagination.getRecords().size());
        deviceMonitoringVoPagination.getRecords().forEach(deviceMonitoringVo -> {
            var cameraMultipleVideoPageVO = CameraMultipleVideoPageVO.builder()
                    .id(deviceMonitoringVo.getDeviceId())
                    .code(deviceMonitoringVo.getCode())
                    .name(deviceMonitoringVo.getDeviceName())
                    .areaId(deviceMonitoringVo.getAreaId())
                    .streamMedia(StreamMediaVO.convertStreamMediaInfo(streamMediaConfigDTO))
                    .thirdPartyId(deviceMonitoringVo.getThirdPartyId())
                    .deviceStatus(deviceMonitoringVo.getDeviceStatus())
                    .product(ObjectUtils.copying(deviceMonitoringVo.getProduct(), CameraMultipleVideoPageVO.DeviceProductVO.class))
                    .tags(ObjectUtils.copying(deviceMonitoringVo.getTags(), CameraMultipleVideoPageVO.DeviceTagVO.class))
                    .build();
            cameraMultipleVideoPageVO.setCollected(multipleVideoCollectCameras.stream()
                                                           .anyMatch(multipleVideoCollectCamera -> deviceMonitoringVo.getDeviceId()
                                                                   .equals(multipleVideoCollectCamera.getDeviceId())));
            if (StringUtils.isNotBlank(deviceMonitoringVo.getAreaId())) {
                var areaIds = Arrays.stream(deviceMonitoringVo.getAreaId().split("-")).map(Long::valueOf).collect(Collectors.toSet());
                var areaIdNameMapping = areaClient.getByIds(areaIds).stream()
                        .collect(Collectors.toMap(AreaDTO::getId, AreaDTO::getName));
                var areaNames = Arrays.stream(deviceMonitoringVo.getAreaId().split("-")).map(areaId -> {
                    if (areaIdNameMapping.containsKey(Long.parseLong(areaId))) {
                        return areaIdNameMapping.get(Long.parseLong(areaId));
                    }
                    return "";
                }).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(areaNames)) {
                    cameraMultipleVideoPageVO.setAreaName(String.join("-", areaNames));
                }
            }
            cameraMultipleVideoPageVOS.add(cameraMultipleVideoPageVO);
        });
        return new Pagination<>(deviceMonitoringVoPagination.getTotal(), deviceMonitoringVoPagination.getPages(), cameraMultipleVideoPageVOS);
    }

    /**
     * 摄像机列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/multiple-video/list-devices")
    public List<CameraMultipleVideoPageVO> listCameraDevices(CameraMultipleVideoQueryParam param) {
        var request = CameraDeviceQueryRequest.builder()
                .areaId(param.getAreaId())
                .genre(param.getDeviceProdCategoryCodeConcat())
                .build();
        var deviceMonitoringVos = this.cameraSecurityDeviceQueryService.queryCameraDeviceList(request);
        var streamMediaConfigDTO = this.streamMediaConfigClient.getStreamMediaConfig();
        var cameraMultipleVideoPageVOS = new ArrayList<CameraMultipleVideoPageVO>();
        deviceMonitoringVos.forEach(deviceMonitoringVo -> {
            var cameraMultipleVideoPageVO = CameraMultipleVideoPageVO.builder()
                    .id(deviceMonitoringVo.getDeviceId())
                    .code(deviceMonitoringVo.getCode())
                    .name(deviceMonitoringVo.getDeviceName())
                    .areaId(deviceMonitoringVo.getAreaId())
                    .streamMedia(StreamMediaVO.convertStreamMediaInfo(streamMediaConfigDTO))
                    .thirdPartyId(deviceMonitoringVo.getThirdPartyId())
                    .deviceStatus(deviceMonitoringVo.getDeviceStatus())
                    .product(ObjectUtils.copying(deviceMonitoringVo.getProduct(), CameraMultipleVideoPageVO.DeviceProductVO.class))
                    .tags(ObjectUtils.copying(deviceMonitoringVo.getTags(), CameraMultipleVideoPageVO.DeviceTagVO.class))
                    .build();
            cameraMultipleVideoPageVOS.add(cameraMultipleVideoPageVO);
        });
        return cameraMultipleVideoPageVOS;
    }

    /**
     * 区域摄像机树形列表
     *
     * @return
     */
    @GetMapping("/multiple-video/area-tree-devices")
    public List<CameraAreaDeviceGroupVO> cameraAreaTreeList() {
        //查询区域列表
        var areaDTOS = this.areaClient.listAreas();
        var cameraAreaDeviceGroupVOS = ObjectUtils.copying(areaDTOS, CameraAreaDeviceGroupVO.class);
        var treeCameraAreaDeviceGroupVOS = new ArrayList<CameraAreaDeviceGroupVO>();
        // 获取权限设备
        var deviceAuthId = cameraAuthService.getUserAuthScopeDeviceIds(SecurityContextHolder.getPrincipal().getId());
        if (CollectionUtils.isEmpty(deviceAuthId)) {
            cameraAreaDeviceGroupVOS.forEach(cameraAreaDeviceGroupVO -> {
                cameraAreaDeviceGroupVO.setDeviceCount(0);
                cameraAreaDeviceGroupVO.setType("area");
                treeCameraAreaDeviceGroupVOS.add(cameraAreaDeviceGroupVO);
            });
            return TreeUtils.assembleTrees(treeCameraAreaDeviceGroupVOS);
        }
        var request = CameraDeviceQueryRequest.builder()
                .deviceIds(deviceAuthId)
                .build();
        var deviceMonitoringVos = this.cameraSecurityDeviceQueryService.queryCameraDeviceList(request);
        //流媒体配置信息
        var streamMediaConfigDTO = this.streamMediaConfigClient.getStreamMediaConfig();
        cameraAreaDeviceGroupVOS.forEach(cameraAreaDeviceGroupVO -> {
            cameraAreaDeviceGroupVO.setDeviceCount((int) deviceMonitoringVos.stream()
                    .filter(device -> StringUtils.isNotBlank(device.getAreaId()) && Arrays.stream(device.getAreaId().split("-"))
                            .toList().contains(String.valueOf(cameraAreaDeviceGroupVO.getId()))).count());
            var areaDeviceInstanceWithH5SchemaDTOS = new ArrayList<DeviceMonitoringResponse>();
            for (var deviceMonitoringResponse : deviceMonitoringVos) {
                if (StringUtils.isNotBlank(deviceMonitoringResponse.getAreaId())) {
                    var deviceAreaIds = deviceMonitoringResponse.getAreaId().split("-");
                    if (deviceAreaIds.length == cameraAreaDeviceGroupVO.getSequence().split("-").length &&
                        cameraAreaDeviceGroupVO.getSequence().startsWith(deviceMonitoringResponse.getAreaId())) {
                        areaDeviceInstanceWithH5SchemaDTOS.add(deviceMonitoringResponse);
                    }
                }
            }
            cameraAreaDeviceGroupVO.setType("area");
            treeCameraAreaDeviceGroupVOS.add(cameraAreaDeviceGroupVO);
            //区域设备
            areaDeviceInstanceWithH5SchemaDTOS.forEach(deviceMonitoringVo -> {
                var cameraDevice = CameraAreaDeviceGroupVO
                        .builder()
                        .id(deviceMonitoringVo.getDeviceId())
                        .code(deviceMonitoringVo.getCode())
                        .name(deviceMonitoringVo.getDeviceName())
                        .type("device")
                        .status(deviceMonitoringVo.getDeviceStatus())
                        .streamMedia(StreamMediaVO.convertStreamMediaInfo(streamMediaConfigDTO))
                        .thirdPartyId(deviceMonitoringVo.getThirdPartyId())
                        .tags(ObjectUtils.copying(deviceMonitoringVo.getTags(), CameraMultipleVideoPageVO.DeviceTagVO.class))
                        .build();
                if (StringUtils.isNotBlank(deviceMonitoringVo.getAreaId())) {
                    var areaIds = Arrays.stream(deviceMonitoringVo.getAreaId().split("-")).toList();
                    cameraDevice.setParentId(Long.valueOf(areaIds.getLast()));
                }
                treeCameraAreaDeviceGroupVOS.add(cameraDevice);
            });
        });
        return TreeUtils.assembleTrees(treeCameraAreaDeviceGroupVOS);
    }
}
