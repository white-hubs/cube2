package com.guhecloud.cube.biz.vmonitor.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.StreamMediaConfigClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StreamMediaConfigDTO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.request.CameraDeviceQueryRequest;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.response.DeviceMonitoringResponse;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.service.CameraSecurityDeviceQueryService;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolDevice;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolLine;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolPerson;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolDeviceService;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolLineService;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolPersonService;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolRecordService;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolDeviceVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolLinePageVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolLineVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolPersonVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolRecordUserVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.StreamMediaVO;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 视频巡逻路线接口
 *
 * @author lanchun
 * @date 2021/12/6 19:55
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPatrolLineController {

    private final CameraPatrolLineService cameraPatrolLineService;

    private final CameraPatrolDeviceService cameraPatrolDeviceService;

    private final CameraPatrolPersonService cameraPatrolPersonService;

    private final CameraPatrolRecordService cameraPatrolRecordService;

    private final AreaClient areaClient;

    private final UserClient userClient;

    private final StreamMediaConfigClient streamMediaConfigClient;

    private final CameraSecurityDeviceQueryService cameraSecurityDeviceQueryService;

    /**
     * 视频巡逻路线添加
     *
     * @param param 新增参数
     *
     * @return
     */
    @PostMapping(value = "/camera-patrol-line")
    public Long save(@Valid @RequestBody CameraPatrolLineModifyParam param) {
        return this.cameraPatrolLineService.save(param);
    }

    /**
     * 视频巡逻路线修改
     *
     * @param id    巡逻线路主键
     * @param param 修改参数
     */
    @PutMapping(value = "/camera-patrol-line/{id}")
    public void update(@PathVariable @Min(value = 1, message = "巡逻线路主键不能小于1") Long id,
                       @Valid @RequestBody CameraPatrolLineModifyParam param
                      ) {
        this.cameraPatrolLineService.update(id, param);
    }

    /**
     * 巡逻路线状态修改
     *
     * @param id
     * @param enabled
     */
    @PutMapping(value = "/camera-patrol-line/{id}/{enabled}")
    public void updateEnabled(@PathVariable("id") @Min(value = 1, message = "巡逻线路主键不能小于1") Long id,
                              @PathVariable("enabled") Boolean enabled
                             ) {
        this.cameraPatrolLineService.updateEnabled(id, enabled);
    }

    /**
     * 视频巡逻路线分页列表
     *
     * @param param 查询参数
     *
     * @return
     */
    @GetMapping("/camera-patrol-lines")
    public Pagination<CameraPatrolLinePageVO> listCameraPatrolLines(@Valid CameraPatrolLineQueryParam param) {
        Page<CameraPatrolLine> page = this.cameraPatrolLineService.page(param);
        List<CameraPatrolLinePageVO> pageVOS = ObjectUtils.copying(page.getRecords(), CameraPatrolLinePageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        setPageCreateUser(pageVOS);
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 视频巡逻路线权限分页列表
     *
     * @param param 查询参数
     *
     * @return
     */
    @GetMapping("/camera-patrol-line/auth-page")
    public Pagination<CameraPatrolLinePageVO> listAuthCameraPatrolLines(@Valid CameraPatrolLineQueryParam param) {
        Page<CameraPatrolLine> page = this.cameraPatrolLineService.listAuthCameraPatrolLines(param);
        List<CameraPatrolLinePageVO> pageVOS = ObjectUtils.copying(page.getRecords(), CameraPatrolLinePageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        setPageCreateUser(pageVOS);
        return PagingUtils.convert(page, pageVOS);
    }

    private void setPageCreateUser(List<CameraPatrolLinePageVO> pageVOS) {
        List<Long> ids = pageVOS.stream().map(CameraPatrolLinePageVO::getId).collect(Collectors.toList());
        Map<Long, CameraPatrolRecordUserVO> patrolRecordUserMap =
                this.cameraPatrolRecordService.getPatrolUserNameByLineIds(ids).stream()
                        .collect(Collectors.toMap(CameraPatrolRecordUserVO::getCameraPatrolLineId, cameraPatrolRecordUserVO -> cameraPatrolRecordUserVO, (o, n) -> n));
        pageVOS.forEach(cameraPatrolLinePageVO -> {
            cameraPatrolLinePageVO.setCreateByName(getCreateUserName(cameraPatrolLinePageVO.getCreateBy(),
                                                                     cameraPatrolLinePageVO.getCreatorId(),
                                                                     cameraPatrolLinePageVO.getCreatorType()));
            CameraPatrolRecordUserVO cameraPatrolRecordUserVO = patrolRecordUserMap.get(cameraPatrolLinePageVO.getId());
            if (Objects.nonNull(cameraPatrolRecordUserVO)) {
                cameraPatrolLinePageVO.setPatrolUserNames(String.join(",", cameraPatrolRecordUserVO.getUserNames()));
            }
        });
    }

    private String getCreateUserName(String createBy, String creatorId, ActorTypeEnum creatorType) {
        //查询用户
        UserDTO userDTO;
        if (StringUtils.isNotBlank(creatorId) &&
            ActorTypeEnum.USER == creatorType) {
            userDTO = userClient.getUser(Long.valueOf(creatorId));
        } else {
            userDTO = userClient.getUserByUserName(createBy);
        }
        if (Objects.nonNull(userDTO)) {
            return userDTO.getUsername();
        }
        return StringUtils.EMPTY_STRING;
    }

    /**
     * 巡逻路线列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/camera-patrol-line/list")
    public List<CameraPatrolLinePageVO> getCameraPatrolLines(@Valid CameraPatrolLineQueryParam param) {
        List<CameraPatrolLine> list = this.cameraPatrolLineService.getList(param);
        return ObjectUtils.copying(list, CameraPatrolLinePageVO.class);
    }

    /**
     * 视频巡逻路线详情
     *
     * @param id 巡逻线路主键
     *
     * @return
     */
    @GetMapping("/camera-patrol-line/{id}")
    public CameraPatrolLineVO getCameraPatrolLine(@PathVariable("id") @Min(value = 1, message = "巡逻线路主键不能小于1") Long id) {
        CameraPatrolLine cameraPatrolLine = this.cameraPatrolLineService.getById(id);
        Assert.isNotFound(cameraPatrolLine, "巡逻线路不存在");
        CameraPatrolLineVO cameraPatrolLineVO = ObjectUtils.copying(cameraPatrolLine, CameraPatrolLineVO.class);
        //查询视频配置列表
        List<CameraPatrolDevice> cameraPatrolDevices = this.cameraPatrolDeviceService.listCameraPatrolDevices(id);
        if (CollectionUtils.isEmpty(cameraPatrolDevices)) {
            return cameraPatrolLineVO;
        }
        //查询人员配置列表
        List<CameraPatrolPerson> cameraPatrolPeople = this.cameraPatrolPersonService.listCameraPatrolPersons(id);
        List<CameraPatrolPersonVO> cameraPatrolPersonVOS = new ArrayList<>(cameraPatrolPeople.size());
        cameraPatrolPeople.forEach(cameraPatrolPerson ->
                                           cameraPatrolPersonVOS.add(new CameraPatrolPersonVO(
                                                   cameraPatrolPerson.getId(),
                                                   cameraPatrolPerson.getCameraPatrolLineId(),
                                                   cameraPatrolPerson.getAuthType(),
                                                   StringUtils.isNotEmpty(cameraPatrolPerson.getAuthConfig())
                                                   ? JsonUtils.deserialize(cameraPatrolPerson.getAuthConfig(), Object.class)
                                                   : null))
                                  );
        cameraPatrolLineVO.setCameraPatrolPersonVOS(cameraPatrolPersonVOS);
        List<CameraPatrolDeviceVO> cameraPatrolDeviceVOS = new ArrayList<>();
        List<Long> deviceIds = cameraPatrolDevices.stream().map(CameraPatrolDevice::getDeviceId).collect(Collectors.toList());
        CameraDeviceQueryRequest request = CameraDeviceQueryRequest.builder()
                .deviceIds(deviceIds)
                .build();
        //流媒体配置信息
        StreamMediaConfigDTO streamMediaConfigDTO = this.streamMediaConfigClient.getStreamMediaConfig();
        //查询摄像机设备列表
        Map<Long, DeviceMonitoringResponse> deviceDTOMap = this.cameraSecurityDeviceQueryService.queryCameraDeviceList(request)
                .stream()
                .collect(Collectors.toMap(DeviceMonitoringResponse::getDeviceId, deviceMonitoringVo -> deviceMonitoringVo, (o, n) -> n));
        // 获取区域信息
        List<AreaDTO> areas = this.areaClient.listAreas();
        Map<Long, List<AreaDTO>> areaMap = areas.stream().collect(Collectors.groupingBy(AreaDTO::getId));
        cameraPatrolDevices.forEach(cameraPatrolDevice -> {
            CameraPatrolDeviceVO cameraPatrolDeviceVO = ObjectUtils.copying(cameraPatrolDevice, CameraPatrolDeviceVO.class);
            if (StringUtils.isNotEmpty(cameraPatrolDevice.getArea())) {
                cameraPatrolDeviceVO.setAreaIds(Arrays.stream(cameraPatrolDevice.getArea().split("-")).map(Long::parseLong)
                                                        .collect(Collectors.toList()));
                String areaNames = Arrays.stream(cameraPatrolDevice.getArea().split("-")).map(areaId -> {
                    if (areaMap.containsKey(Long.parseLong(areaId))) {
                        AreaDTO area = areaMap.get(Long.parseLong(areaId)).getFirst();
                        return area.getName();
                    }
                    return "";
                }).collect(Collectors.joining("-"));
                cameraPatrolDeviceVO.setAreaNames(areaNames);
            }
            DeviceMonitoringResponse deviceDTO = deviceDTOMap.get(cameraPatrolDevice.getDeviceId());
            if (Objects.nonNull(deviceDTO)) {
                cameraPatrolDeviceVO.setThirdPartyId(deviceDTO.getThirdPartyId());
                cameraPatrolDeviceVO.setStreamMedia(StreamMediaVO.convertStreamMediaInfo(streamMediaConfigDTO));
            }
            cameraPatrolDeviceVOS.add(cameraPatrolDeviceVO);
        });
        //查询用户
        cameraPatrolLineVO.setCreateByName(getCreateUserName(cameraPatrolLineVO.getCreateBy(),
                                                             cameraPatrolLineVO.getCreatorId(),
                                                             cameraPatrolLineVO.getCreatorType()));
        cameraPatrolLineVO.setCameraPatrolDeviceVOS(cameraPatrolDeviceVOS);
        return cameraPatrolLineVO;
    }

    /**
     * 视频巡逻路线删除
     *
     * @param id 巡逻线路主键
     */
    @DeleteMapping("/camera-patrol-line/{id}")
    public void remove(@PathVariable("id") @Min(value = 1, message = "巡逻线路主键不能小于1") Long id) {
        this.cameraPatrolLineService.remove(id);
    }

    /**
     * 视频巡逻路线批量删除
     *
     * @param ids 巡逻路线主键集合
     */
    @DeleteMapping("/camera-patrol-line/batch")
    public void removeByIds(@RequestParam(name = "ids") @NotBlank(message = "巡逻路线主键不能为空") List<Long> ids) {
        this.cameraPatrolLineService.removeByIds(ids);
    }
}