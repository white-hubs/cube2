package com.guhecloud.cube.biz.vmonitor.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.StreamMediaConfigClient;
import com.guhecloud.cube.biz.mdata.client.dto.StreamMediaConfigDTO;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.request.CameraDeviceQueryRequest;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.response.DeviceMonitoringResponse;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.service.CameraSecurityDeviceQueryService;
import com.guhecloud.cube.biz.vmonitor.server.entity.MultipleVideoCollect;
import com.guhecloud.cube.biz.vmonitor.server.entity.MultipleVideoCollectCamera;
import com.guhecloud.cube.biz.vmonitor.server.param.MultipleVideoCollectModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.param.MultipleVideoCollectQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.service.MultipleVideoCollectCameraService;
import com.guhecloud.cube.biz.vmonitor.server.service.MultipleVideoCollectService;
import com.guhecloud.cube.biz.vmonitor.server.vo.MultipleVideoCollectCameraVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.MultipleVideoCollectPageVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.MultipleVideoCollectVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.StreamMediaVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 多路视频收藏接口
 *
 * @author lanchun
 * @date 2021/12/6 19:56
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MultipleVideoCollectController {

    private final MultipleVideoCollectService multipleVideoCollectService;

    private final MultipleVideoCollectCameraService multipleVideoCollectCameraService;

    private final StreamMediaConfigClient streamMediaConfigClient;

    private final CameraSecurityDeviceQueryService cameraSecurityDeviceQueryService;

    /**
     * 多路视频收藏添加
     *
     * @param param 保存参数
     *
     * @return
     */
    @PostMapping("/multiple-video-collect")
    public Long save(@Valid @RequestBody MultipleVideoCollectModifyParam param) {
        return this.multipleVideoCollectService.save(param);
    }

    /**
     * 多路视频收藏修改
     *
     * @param id    多路视频收藏主键
     * @param param 修改参数
     */
    @PutMapping("/multiple-video-collect/{id}")
    public void update(@PathVariable @Min(value = 1, message = "多路视频收藏主键不能小于1") Long id,
                       @Valid @RequestBody MultipleVideoCollectModifyParam param
                      ) {
        this.multipleVideoCollectService.update(id, param);
    }

    /**
     * 多路视频收藏详情
     *
     * @param id 多路视频收藏主键
     *
     * @return
     */
    @GetMapping("/multiple-video-collect/{id}")
    public MultipleVideoCollectVO getMultipleVideoCollect(
            @PathVariable("id") @Min(value = 1, message = "多路视频收藏主键不能小于1") Long id
                                                         ) {
        MultipleVideoCollect multipleVideoCollect = this.multipleVideoCollectService.getById(id);
        Assert.isNotFound(multipleVideoCollect, "多路视频收藏不存在");
        MultipleVideoCollectVO multipleVideoCollectVO = ObjectUtils.copying(multipleVideoCollect, MultipleVideoCollectVO.class);
        //查询配置的摄像机
        List<MultipleVideoCollectCamera> multipleVideoCollectCameras = multipleVideoCollectCameraService.listMultipleVideoCollectCameras(multipleVideoCollectVO.getId());
        if (CollectionUtils.isEmpty(multipleVideoCollectCameras)) {
            return multipleVideoCollectVO;
        }
        List<MultipleVideoCollectCameraVO> multipleVideoCollectCameraVOS = Collections.synchronizedList(new ArrayList<>());
        List<Long> deviceIds = multipleVideoCollectCameras.stream().map(MultipleVideoCollectCamera::getDeviceId)
                .collect(Collectors.toList());
        CameraDeviceQueryRequest request = CameraDeviceQueryRequest.builder()
                .deviceIds(deviceIds)
                .build();
        Map<Long, DeviceMonitoringResponse> deviceMonitoringVoMap = this.cameraSecurityDeviceQueryService.queryCameraDeviceList(request)
                .stream()
                .collect(Collectors.toMap(DeviceMonitoringResponse::getDeviceId, deviceMonitoringVo -> deviceMonitoringVo, (o, n) -> n));
        StreamMediaConfigDTO streamMediaConfigDTO = this.streamMediaConfigClient.getStreamMediaConfig();
        multipleVideoCollectCameras.parallelStream().forEach(multipleVideoCollectCamera -> {
            DeviceMonitoringResponse deviceDTO = deviceMonitoringVoMap.get(multipleVideoCollectCamera.getDeviceId());
            if (Objects.nonNull(deviceDTO)) {
                MultipleVideoCollectCameraVO multipleVideoCollectCameraVO = ObjectUtils.copying(multipleVideoCollectCamera, MultipleVideoCollectCameraVO.class);
                multipleVideoCollectCameraVO.setId(deviceDTO.getDeviceId());
                multipleVideoCollectCameraVO.setCode(deviceDTO.getCode());
                multipleVideoCollectCameraVO.setName(deviceDTO.getDeviceName());
                multipleVideoCollectCameraVO.setThirdPartyId(deviceDTO.getThirdPartyId());
                multipleVideoCollectCameraVO.setStreamMedia(StreamMediaVO.convertStreamMediaInfo(streamMediaConfigDTO));
                multipleVideoCollectCameraVO.setDeviceStatus(deviceDTO.getDeviceStatus());
                if (Objects.nonNull(deviceDTO.getProduct())) {
                    multipleVideoCollectCameraVO.setImage(deviceDTO.getProduct().getImage());
                }
                multipleVideoCollectCameraVOS.add(multipleVideoCollectCameraVO);
            }
        });
        multipleVideoCollectVO.setCameraVOS(multipleVideoCollectCameraVOS);
        return multipleVideoCollectVO;
    }

    /**
     * 多路视频收藏分页列表
     *
     * @param param 查询参数
     *
     * @return
     */
    @GetMapping("/multiple-video-collects")
    public Pagination<MultipleVideoCollectPageVO> listMultipleVideoCollects(@Valid MultipleVideoCollectQueryParam param) {
        Page<MultipleVideoCollect> page = this.multipleVideoCollectService.page(param);
        List<MultipleVideoCollectPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), MultipleVideoCollectPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 多路视频收藏删除
     *
     * @param id 多路视频收藏主键
     */
    @DeleteMapping("/multiple-video-collect/{id}")
    public void remove(@PathVariable("id") @Min(value = 1, message = "多路视频收藏主键不能小于1") Long id) {
        this.multipleVideoCollectService.remove(id);
    }
}