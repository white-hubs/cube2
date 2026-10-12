package com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.service;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceSortFieldEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.SortEnum;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceSortParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByAreasOrGroupsAndTagParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceGroupParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.vo.CustomerTagVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceGroupVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterTagStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceGroupStdClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.request.CameraDeviceQueryPageRequest;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.request.CameraDeviceQueryRequest;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.response.DeviceMonitoringResponse;
import com.guhecloud.cube.srv.gen.securitymanagement.client.SecurityDeviceClient;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraSecurityDeviceQueryService {

    private final SecurityDeviceClient securityDeviceClient;

    private final DeviceCenterTagStdClient deviceCenterTagStdClient;

    private final DeviceCenterStdClient deviceCenterStdClient;

    private final DeviceGroupStdClient deviceGroupStdClient;

    private final FileClient fileClient;

    private static final String APPLICATION_CODE = "SECURITY_MONITORING";

    public static final String APP_TAG = "builtin.app::" + APPLICATION_CODE;

    private static final String CAMERA_DEVICE_TAG_CODE = "builtin.usage::camera";

    private static final List<String> CAMERA_DEVICE_TAG_CODES = List.of(CAMERA_DEVICE_TAG_CODE, APP_TAG);

    private static final Integer DEFAULT_PAGE_NUMBER = 1;

    private static final Integer DEFAULT_PAGE_SIZE = 1000;

    /**
     * 查询监控设备分页列表
     *
     * @param request
     *
     * @return
     */
    public Pagination<DeviceMonitoringResponse> queryCameraDevicePage(CameraDeviceQueryPageRequest request) {
        var param = new DeviceFilterPageParam();
        param.setKeyword(request.getKeyword());
        param.setAnyMatchedAreaId(request.getAreaId());
        param.setDeviceState(request.getDeviceStatus());
        param.setCategoryCode(request.getGenre());
        param.setMatchedDeviceIds(request.getDeviceIds());
        param.setAllMatchedTagCodes(CAMERA_DEVICE_TAG_CODES);
        param.setPageNumber(request.getPageNumber());
        param.setPageSize(request.getPageSize());
        //按状态排序
        var sortParam = new DeviceSortParam();
        sortParam.setSortField(DeviceSortFieldEnum.ONLINE_STATUS);
        sortParam.setSort(SortEnum.DESC);
        param.setDeviceSort(sortParam);
        if (CollectionUtils.isNotEmpty(request.getAreaIds())) {
            param.setAnyMatchedAreaId(String.join("-", request.getAreaIds()));
        }
        if (!CollectionUtils.isEmpty(request.getTagCodes())) {
            List<Long> filterTagDeviceIds = this.filterTagDevices(request.getTagCodes());
            if (CollectionUtils.isEmpty(filterTagDeviceIds)) {
                return Pagination.empty();
            }
            if (!CollectionUtils.isEmpty(param.getMatchedDeviceIds())) {
                param.setMatchedDeviceIds(param.getMatchedDeviceIds().stream().filter(filterTagDeviceIds::contains)
                                                  .collect(Collectors.toList()));
                if (CollectionUtils.isEmpty(param.getMatchedDeviceIds())) {
                    return Pagination.empty();
                }
            } else {
                param.setMatchedDeviceIds(filterTagDeviceIds);
                if (CollectionUtils.isEmpty(param.getMatchedDeviceIds())) {
                    return Pagination.empty();
                }
            }
        }
        var devicePageInfoVOPagination = this.deviceCenterStdClient.deviceInfoPage(param);
        if (CollectionUtils.isEmpty(devicePageInfoVOPagination.getRecords())) {
            return Pagination.empty();
        }
        return Pagination.<DeviceMonitoringResponse>builder()
                .records(this.convertAndExtendDeviceMonitoring(devicePageInfoVOPagination.getRecords()))
                .total(devicePageInfoVOPagination.getTotal())
                .pages(devicePageInfoVOPagination.getPages())
                .build();
    }

    public Integer queryCameraDeviceCountByAreasOrGroups(Set<Long> areaIds, Set<Long> groupIds, Set<Long> deviceIds) {
        if (CollectionUtils.isEmpty(areaIds) && CollectionUtils.isEmpty(groupIds) && CollectionUtils.isEmpty(deviceIds)) {
            return 0;
        }
        var param = new QueryDeviceByAreasOrGroupsAndTagParam();
        param.setTagCode(APP_TAG);
        param.setAreaIds(areaIds);
        param.setGroupIds(groupIds);
        param.setDeviceIds(deviceIds);
        return deviceCenterStdClient.queryCameraDeviceIdsByAreasOrGroups(param).size();
    }

    public Set<Long> queryCameraDeviceIdsByAreasOrGroups(Set<Long> areaIds, Set<Long> groupIds, Set<Long> deviceIds) {
        if (CollectionUtils.isEmpty(areaIds) && CollectionUtils.isEmpty(groupIds) && CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptySet();
        }
        var param = new QueryDeviceByAreasOrGroupsAndTagParam();
        param.setTagCode(APP_TAG);
        param.setAreaIds(areaIds);
        param.setGroupIds(groupIds);
        param.setDeviceIds(deviceIds);
        return deviceCenterStdClient.queryCameraDeviceIdsByAreasOrGroups(param);
    }

    /**
     * 查询监控设备列表
     *
     * @param request
     *
     * @return
     */
    public List<DeviceMonitoringResponse> queryCameraDeviceList(CameraDeviceQueryRequest request) {
        var param = new DeviceFilterPageParam();
        param.setKeyword(request.getKeyword());
        param.setAnyMatchedAreaId(request.getAreaId());
        param.setCategoryCode(request.getGenre());
        param.setMatchedDeviceIds(request.getDeviceIds());
        param.setAllMatchedTagCodes(CAMERA_DEVICE_TAG_CODES);
        param.setPageNumber(DEFAULT_PAGE_NUMBER);
        param.setPageSize(DEFAULT_PAGE_SIZE);
        var devicePageInfoVOPage = new ArrayList<DevicePageInfoVO>();
        var devicePageInfoVOPagination = this.deviceCenterStdClient.deviceInfoPage(param);
        if (CollectionUtils.isNotEmpty(devicePageInfoVOPagination.getRecords())) {
            devicePageInfoVOPage.addAll(devicePageInfoVOPagination.getRecords());
        }
        for (int pageNumber = 2; pageNumber <= devicePageInfoVOPagination.getPages(); pageNumber++) {
            param.setPageNumber(pageNumber);
            var pagination = this.deviceCenterStdClient.deviceInfoPage(param);
            if (CollectionUtils.isNotEmpty(pagination.getRecords())) {
                devicePageInfoVOPage.addAll(pagination.getRecords());
            }
        }
        if (CollectionUtils.isEmpty(devicePageInfoVOPage)) {
            return Collections.emptyList();
        }
        return this.convertAndExtendDeviceMonitoring(devicePageInfoVOPage);
    }

    public List<DeviceGroupVO> getDeviceGroups(List<Long> deviceIds) {
        var param = new QueryDeviceGroupParam();
        param.setDeviceIds(new HashSet<>(deviceIds));
        return deviceGroupStdClient.deviceGroupByTagAndDeviceIds(APP_TAG, param);
    }

    public DeviceGroupVO getDeviceGroupById(Long id) {
        return deviceGroupStdClient.getGroup(id);
    }

    private List<DeviceMonitoringResponse> convertAndExtendDeviceMonitoring(List<DevicePageInfoVO> deviceMonitoringVos) {
        Set<DeviceProductInfoDTO> productInfos = deviceMonitoringVos.stream().map(DevicePageInfoVO::getProductInfo)
                .collect(Collectors.toSet());
        List<Long> productFileIds = productInfos.stream().map(DeviceProductInfoDTO::getPicture).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(productInfos) || CollectionUtils.isEmpty(productFileIds)) {
            return deviceMonitoringVos.stream().map(this::buildDeviceMonitoringResponse).collect(Collectors.toList());
        }
        Set<Long> deviceIds = deviceMonitoringVos.stream().map(DevicePageInfoVO::getId).collect(Collectors.toSet());
        Map<Long, List<CustomerTagVO>> deviceAllTagMap = this.deviceCenterTagStdClient.deviceAllTag(deviceIds);
        Map<Long, DeviceProductInfoDTO> productInfoMaps = productInfos.stream()
                .collect(Collectors.toMap(DeviceProductInfoDTO::getId,
                                          deviceProductInfoDTO -> deviceProductInfoDTO,
                                          (o, n) -> n));
        Map<Long, List<FileVO>> productFileMaps =
                this.fileClient.getByIds(productFileIds).stream().collect(Collectors.groupingBy(FileVO::getId));
        return deviceMonitoringVos.stream().map(deviceMonitoringVo -> {
            DeviceMonitoringResponse deviceMonitoringResponse = this.buildDeviceMonitoringResponse(deviceMonitoringVo);
            if (productInfoMaps.containsKey(deviceMonitoringVo.getProductInfo().getId())) {
                DeviceProductInfoDTO deviceProductInfoDTO = productInfoMaps.get(deviceMonitoringVo.getProductInfo().getId());
                if (productFileMaps.containsKey(deviceProductInfoDTO.getPicture())) {
                    deviceMonitoringResponse.getProduct().setImage(productFileMaps.get(deviceProductInfoDTO.getPicture()).getFirst());
                }
            }
            if (deviceAllTagMap.containsKey(deviceMonitoringVo.getId())) {
                deviceMonitoringResponse.setTags(DeviceMonitoringResponse.DeviceTags.buildDeviceTags(deviceAllTagMap.get(deviceMonitoringVo.getId())));
            }
            return deviceMonitoringResponse;
        }).collect(Collectors.toList());
    }

    private DeviceMonitoringResponse buildDeviceMonitoringResponse(DevicePageInfoVO deviceMonitoringVo) {
        return DeviceMonitoringResponse.buildDeviceMonitoringResponse(deviceMonitoringVo);
    }

    public List<Long> getDeviceIds() {
        return this.securityDeviceClient.getSecurityDeviceIds(APPLICATION_CODE);
    }

    public List<Long> filterTagDevices(List<String> tagCodes) {
        return new ArrayList<>(deviceCenterTagStdClient.queryBusinessCustomerTagDevices(APP_TAG, tagCodes));
    }
}
