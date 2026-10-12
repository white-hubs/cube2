package com.guhecloud.cube.biz.vmonitor.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.videogw.client.VideoClient;
import com.guhecloud.cube.base.videogw.client.dto.VideoFileDTO;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.request.CameraDeviceQueryRequest;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.response.DeviceMonitoringResponse;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.service.CameraSecurityDeviceQueryService;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolRecord;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolRecordDevice;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolRecordDeviceRemarks;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordDeviceRemarkQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolRecordDeviceRemarksService;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolRecordDeviceService;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolRecordService;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolFileVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolRecordDeviceRemarksVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolRecordDeviceVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 视频巡逻记录接口
 *
 * @author lanchun
 * @date 2021/12/6 19:56
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPatrolRecordDeviceController {

    private final CameraPatrolRecordService cameraPatrolRecordService;

    private final CameraPatrolRecordDeviceService cameraPatrolRecordDeviceService;

    private final CameraPatrolRecordDeviceRemarksService cameraPatrolRecordDeviceRemarksService;

    private final CameraSecurityDeviceQueryService cameraSecurityDeviceQueryService;

    private final VideoClient videoClient;

    /**
     * 视频巡逻设备记录列表(根据巡逻记录获取)
     *
     * @param cameraPatrolRecordId 巡逻记录主键
     *
     * @return
     */
    @GetMapping(value = "/camera-patrol-record-devices/{cameraPatrolRecordId}")
    public List<CameraPatrolRecordDeviceVO> listCameraPatrolRecordDevices(@PathVariable("cameraPatrolRecordId") Long cameraPatrolRecordId) {
        //查询巡逻设备列表
        List<CameraPatrolRecordDevice> cameraPatrolRecordDevices = this.cameraPatrolRecordDeviceService.listCameraPatrolRecordDevices(cameraPatrolRecordId);
        if (CollectionUtils.isEmpty(cameraPatrolRecordDevices)) {
            return Collections.emptyList();
        }
        List<CameraPatrolRecordDeviceVO> cameraPatrolRecordDeviceVOS = ObjectUtils.copying(cameraPatrolRecordDevices, CameraPatrolRecordDeviceVO.class);
        List<Long> deviceIds = cameraPatrolRecordDeviceVOS.stream().map(CameraPatrolRecordDeviceVO::getDeviceId)
                .collect(Collectors.toList());
        Map<Long, DeviceMonitoringResponse> deviceDTOMap = getLongDeviceMonitoringResponseMap(deviceIds);
        cameraPatrolRecordDeviceVOS.stream().forEach(cameraPatrolRecordDeviceVO -> {
            DeviceMonitoringResponse detail = deviceDTOMap.get(cameraPatrolRecordDeviceVO.getDeviceId());
            if (Objects.nonNull(detail) && Objects.nonNull(detail.getProduct())) {
                cameraPatrolRecordDeviceVO.setImage(detail.getProduct().getImage());
            }
        });
        return cameraPatrolRecordDeviceVOS;
    }

    /**
     * 视频巡逻设备异常记录列表(根据巡逻记录获取)
     *
     * @param cameraPatrolRecordId
     *
     * @return
     */
    @GetMapping(value = "/camera-patrol-record-device-remarks/{cameraPatrolRecordId}")
    public List<CameraPatrolRecordDeviceRemarksVO> listPatrolRecordDeviceRemarks(
            @PathVariable("cameraPatrolRecordId") Long cameraPatrolRecordId
                                                                                ) {
        List<CameraPatrolRecordDeviceRemarks> cameraPatrolRecordDeviceRemarks = this.cameraPatrolRecordDeviceRemarksService.listByCameraPatrolRecordId(cameraPatrolRecordId);
        if (CollectionUtils.isEmpty(cameraPatrolRecordDeviceRemarks)) {
            return Collections.emptyList();
        }
        List<CameraPatrolRecordDeviceRemarksVO> cameraPatrolRecordDeviceRemarksVOS = ObjectUtils.copying(cameraPatrolRecordDeviceRemarks, CameraPatrolRecordDeviceRemarksVO.class);
        setRecordRemarkFiles(cameraPatrolRecordDeviceRemarksVOS);
        return cameraPatrolRecordDeviceRemarksVOS;
    }

    /**
     * 视频巡逻设备异常记录分页列表
     *
     * @param
     *
     * @return
     */
    @GetMapping(value = "/camera-patrol-record-device-remark/page")
    public Pagination<CameraPatrolRecordDeviceRemarksVO> patrolRecordDeviceRemarkPage(@Valid CameraPatrolRecordDeviceRemarkQueryParam param
                                                                                     ) {
        Page<CameraPatrolRecordDeviceRemarks> page = this.cameraPatrolRecordDeviceRemarksService.page(param);
        List<CameraPatrolRecordDeviceRemarksVO> cameraPatrolRecordDeviceRemarksVOS = ObjectUtils.copying(page.getRecords(), CameraPatrolRecordDeviceRemarksVO.class);
        if (CollectionUtils.isEmpty(cameraPatrolRecordDeviceRemarksVOS)) {
            return Pagination.empty();
        }
        setRecordRemarkFiles(cameraPatrolRecordDeviceRemarksVOS);
        return PagingUtils.convert(page, cameraPatrolRecordDeviceRemarksVOS);
    }

    private void setRecordRemarkFiles(List<CameraPatrolRecordDeviceRemarksVO> cameraPatrolRecordDeviceRemarksVOS) {
        //巡逻人信息
        List<Long> cameraRecordIds = cameraPatrolRecordDeviceRemarksVOS.stream()
                .map(CameraPatrolRecordDeviceRemarksVO::getCameraPatrolRecordId).collect(Collectors.toList());
        Map<Long, CameraPatrolRecord> cameraPatrolRecordMap = this.cameraPatrolRecordService
                .list(Wrappers.<CameraPatrolRecord>lambdaQuery().in(CameraPatrolRecord::getId, cameraRecordIds))
                .stream()
                .collect(Collectors.toMap(CameraPatrolRecord::getId, obj -> obj, (o, n) -> n));
        //查询摄像机设备列表
        List<Long> deviceIds = cameraPatrolRecordDeviceRemarksVOS.stream().map(CameraPatrolRecordDeviceRemarksVO::getDeviceId)
                .collect(Collectors.toList());
        Map<Long, DeviceMonitoringResponse> deviceDTOMap = getLongDeviceMonitoringResponseMap(deviceIds);
        //查询转存的文件地址
        Set<Long> fileIdList = cameraPatrolRecordDeviceRemarksVOS.stream()
                .filter(t -> StringUtils.isNotBlank(t.getFilePaths()))
                .map(t -> JsonUtils.deserialize(t.getFilePaths(), new TypeReference<List<CameraPatrolFileVO>>(){}))
                .filter(t -> !CollectionUtils.isEmpty(t))
                .map(t -> t.stream().map(CameraPatrolFileVO::getFileId).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, VideoFileDTO> videoFileDTOMap = CollectionUtils.isEmpty(fileIdList) ?
                                                  new HashMap<>() : this.videoClient.getVideoFilesByIds(fileIdList).stream()
                                                          .collect(Collectors.toMap(VideoFileDTO::getId, videoFileDTO -> videoFileDTO, (o, n) -> n));

        cameraPatrolRecordDeviceRemarksVOS.stream().forEach(cameraPatrolRecordDeviceRemarksVO -> {
            if (StringUtils.isNotEmpty(cameraPatrolRecordDeviceRemarksVO.getFilePaths())) {
                List<CameraPatrolFileVO> cameraPatrolFileVOS = JsonUtils.deserialize(cameraPatrolRecordDeviceRemarksVO.getFilePaths(), new TypeReference<List<CameraPatrolFileVO>>() {});
                cameraPatrolFileVOS.forEach(t -> {
                    if (videoFileDTOMap.containsKey(t.getFileId())) {
                        VideoFileDTO videoFileDTO = videoFileDTOMap.get(t.getFileId());
                        t.setExtranetFilePath(videoFileDTO.getPath());
                        t.setIntranetFilePath(videoFileDTO.getPath());
                    }
                });
                cameraPatrolRecordDeviceRemarksVO.setFilePathList(cameraPatrolFileVOS);
            }
            DeviceMonitoringResponse detail = deviceDTOMap.get(cameraPatrolRecordDeviceRemarksVO.getDeviceId());
            if (Objects.nonNull(detail) && Objects.nonNull(detail.getProduct())) {
                cameraPatrolRecordDeviceRemarksVO.setImage(detail.getProduct().getImage());
            }
            if (cameraPatrolRecordMap.containsKey(cameraPatrolRecordDeviceRemarksVO.getCameraPatrolRecordId())) {
                CameraPatrolRecord cameraPatrolRecord = cameraPatrolRecordMap.get(cameraPatrolRecordDeviceRemarksVO.getCameraPatrolRecordId());
                cameraPatrolRecordDeviceRemarksVO.setCreateByName(cameraPatrolRecord.getUserName());
            }
        });
    }

    public Map<Long, DeviceMonitoringResponse> getLongDeviceMonitoringResponseMap(List<Long> deviceIds) {
        CameraDeviceQueryRequest request = CameraDeviceQueryRequest.builder()
                .deviceIds(deviceIds)
                .build();
        //查询摄像机设备列表
        return this.cameraSecurityDeviceQueryService.queryCameraDeviceList(request)
                .stream()
                .collect(Collectors.toMap(DeviceMonitoringResponse::getDeviceId, deviceMonitoringVo -> deviceMonitoringVo, (o, n) -> n));
    }
}