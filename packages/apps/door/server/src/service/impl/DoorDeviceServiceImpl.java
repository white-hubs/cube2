package com.guhecloud.cube.biz.door.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.metadata.param.TagBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceListByGenreVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterTagStdClient;
import com.guhecloud.cube.biz.door.server.dao.DoorDeviceMapper;
import com.guhecloud.cube.biz.door.server.entity.DoorDevice;
import com.guhecloud.cube.biz.door.server.event.vo.DoorDeviceUpdateEvent;
import com.guhecloud.cube.biz.door.server.param.ControlParam;
import com.guhecloud.cube.biz.door.server.param.DoorDeviceParam;
import com.guhecloud.cube.biz.door.server.queryparam.DoorDeviceQueryParam;
import com.guhecloud.cube.biz.door.server.service.DoorDeviceService;
import com.guhecloud.cube.biz.door.server.vo.ControlResultVo;
import com.guhecloud.cube.biz.door.server.vo.DoorDeviceVo;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ResourceNotAcceptableException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.subsystem.requester.sdk.client.AccessControlClient;
import com.guhecloud.subsystem.requester.sdk.param.accesscontrol.DoorControlParam;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.AuthDoorStatusVo;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.DoorControlResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoorDeviceServiceImpl extends ServiceImpl<DoorDeviceMapper, DoorDevice> implements DoorDeviceService {

    //todo 后续外化出去
    public static String applicationConstants = "builtin.app::";
    public static String applicationCode = "DOORMANAGE";
    private final DoorDeviceMapper doorDeviceMapper;
    private final AccessControlClient accessControlClient;
    private final DeviceCenterStdClient deviceCenterClient;
    private final AreaClient areaClient;
    private final ApplicationClient applicationClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final DeviceCenterTagStdClient deviceCenterTagClient;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 这个方法已经废弃，统一由设备中心处理
     */
    @Override
    public List<DeviceListByGenreVO> getDevices() {
        return Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDoorDevices(List<DoorDeviceParam> doorDeviceParams) {
        List<DoorDevice> doorDevices = new ArrayList<>();
        List<Long> deviceIds = new ArrayList<>();
        for (DoorDeviceParam doorDeviceParam : doorDeviceParams) {
            DeviceDetailDTO detail = deviceCenterClient.getDeviceById(doorDeviceParam.getDeviceId());
            long count = this.lambdaQuery().eq(DoorDevice::getBusinessId, doorDeviceParam.getDeviceId()).count();
            Assert.isConflict(count != 0, "设备:" + detail.getBaseArchivesInfo().getName() + "已存在绑定关系");
            //基础信息
            DeviceBaseArchivesInfoDTO baseArchivesInfo = detail.getBaseArchivesInfo();
            //设备产品信息
            DeviceProductInfoDTO productInfo = detail.getProductInfo();
            Pair<Long, String> areaPair = baseArchivesInfo.getArea();
            Long areaId = areaPair != null ? areaPair.getKey() : null;
            AreaDTO area = areaId != null ? areaClient.getAreaById(areaId) : null;
            DoorDevice doorDevice = DoorDevice.builder()
                    .doorCode(doorDeviceParam.getDoorCode())
                    .doorArea(area != null ? area.getId().toString() : null)
                    .doorAreaIds(area != null ? area.getSequence() : null)
                    .doorModel(productInfo != null ? productInfo.getId() : null)
                    .businessId(baseArchivesInfo.getId())
                    .name(baseArchivesInfo.getName())
                    .type(doorDeviceParam.getType())
                    .outBusinessId(baseArchivesInfo.getThirdPartyId())
                    .build();
            doorDevices.add(doorDevice);
            deviceIds.add(doorDeviceParam.getDeviceId());
        }
        if (CollectionUtils.isNotEmpty(doorDevices)) {
            this.saveBatch(doorDevices);
        }
        //设备打标签
        Integer maxDeviceNumber = 5000;
        ApplicationDTO application = applicationClient.getApplicationByCode(applicationCode);
        String tag = applicationConstants + application.getCode();
        List<DeviceBaseArchivesInfoDTO> listByTag = this.deviceCenterStdClient.getListByTag(Collections.singleton(tag));
        List<Long> binDeviceIds = listByTag.stream().map(DeviceBaseArchivesInfoDTO::getId).toList();
        //根据条件获取设备,若存在分页的情况需多次获取打标签

        DeviceFilterPageParam deviceFilterPageParam = new DeviceFilterPageParam();
        deviceFilterPageParam.setMatchedDeviceIds(deviceIds);
        deviceFilterPageParam.setPageNumber(1);
        deviceFilterPageParam.setPageSize(maxDeviceNumber);
        Pagination<DevicePageInfoVO> devicePageInfoVOPagination = this.deviceCenterStdClient.deviceInfoPage(deviceFilterPageParam);
        if (com.guhecloud.platform.base.util.CollectionUtils.isEmpty(devicePageInfoVOPagination.getRecords())) {
            return;
        }
        Integer integer = deviceCenterTagClient.checkTag(tag);
        if (integer.equals(0)) {
            CreateTagCmd createTagCmd = CreateTagCmd.createTopTag(null, tag, application.getName());
            deviceCenterTagClient.createTag(createTagCmd);
        }
        List<DevicePageInfoVO> devicePageInfoVOS = devicePageInfoVOPagination.getRecords();
        for (int statPage = 1; statPage <= devicePageInfoVOPagination.getPages(); statPage++) {
            if (statPage > 1) {
                //再次查询除第一页后的数据
                deviceFilterPageParam.setPageNumber(statPage);
                deviceFilterPageParam.setPageSize(maxDeviceNumber);
                log.info("时间开始{}", System.currentTimeMillis());
                devicePageInfoVOS = this.deviceCenterStdClient.deviceInfoPage(deviceFilterPageParam).getRecords();
                log.info("时间结束{}", System.currentTimeMillis());
            }

            Set<Long> filterDeviceIds = devicePageInfoVOS.stream()
                    .map(DevicePageInfoVO::getId)
                    .filter(id -> !binDeviceIds.contains(id))
                    .collect(Collectors.toSet());

            TagBindDeviceCmd tagBindDeviceParam = new TagBindDeviceCmd();
            tagBindDeviceParam.setBusinessKeys(filterDeviceIds);
            tagBindDeviceParam.setTagCode(tag);
            deviceCenterTagClient.bindTag(tagBindDeviceParam);
        }
    }

    @Override
    public Page<DoorDeviceVo> getDoorDevicePage(DoorDeviceQueryParam doorDeviceQueryParam) {
        Page<DoorDeviceVo> doorDevicePage = doorDeviceMapper.getDoorDevicePage(PagingUtils.convert(doorDeviceQueryParam), doorDeviceQueryParam);
        List<DoorDeviceVo> records = doorDevicePage.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<DoorDevice> updateList = new ArrayList<>();
            records.forEach(s -> {
                DeviceDetailDTO detail = deviceCenterClient.getDeviceById(s.getBusinessId());
                if (Objects.nonNull(detail)) {
                    //处理门禁设备信息同步数据
                    this.handleDoorDevice(s, detail, updateList);

                    //扩展区域
                    DeviceBaseArchivesInfoDTO baseArchivesInfo = detail.getBaseArchivesInfo();
                    if (ObjectUtils.isNotEmpty(baseArchivesInfo)) {
                        Pair<Long, String> areaPair = baseArchivesInfo.getArea();
                        Long areaId = areaPair != null ? areaPair.getKey() : null;
                        AreaDTO area = areaId != null ? areaClient.getAreaById(areaId) : null;
                        s.setAreaName(area != null ? area.getName() : null);
                    }
                    //扩展产品名称
                    DeviceProductInfoDTO productInfo = detail.getProductInfo();
                    if (ObjectUtils.isNotEmpty(productInfo)) {
                        s.setDoorModelName(productInfo.getName());
                    }
                    s.setStatus("0");
                }

            });
            if (CollectionUtils.isNotEmpty(updateList)) {
                applicationEventPublisher.publishEvent(new DoorDeviceUpdateEvent(this, updateList));
            }
        }

        return doorDevicePage;
    }

    @Override
    public void deleteDoorDevice(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public void deleteDoorDeviceBatch(List<Long> ids) {
        //解除与标签的绑定
        DoorDeviceQueryParam param = new DoorDeviceQueryParam();
        param.setIds(ids);
        List<DoorDeviceVo> doorDeviceList = doorDeviceMapper.getDoorDeviceList(param);
        if (CollectionUtils.isNotEmpty(doorDeviceList)) {
            ApplicationDTO application = applicationClient.getApplicationByCode(applicationCode);
            String tag = applicationConstants + application.getCode();

            TagBindDeviceCmd cmd = new TagBindDeviceCmd();
            cmd.setBusinessKeys(doorDeviceList.stream().map(DoorDeviceVo::getBusinessId)
                                        .filter(businessId -> !Objects.isNull(businessId))
                                        .collect(Collectors.toSet()));
            cmd.setTagCode(tag);
            deviceCenterTagClient.unbindTag(cmd);
        }
        //删除门禁自己这边的设备
        doorDeviceMapper.deleteDoorDeviceList(ids);
    }

    @Override
    public void updateStatus(List<AuthDoorStatusVo> doorStates) {
        doorDeviceMapper.updateStatus(doorStates);
    }

    @Override
    public String getStatus(Long id) {
        DoorDevice byId = this.getById(id);
        ArrayList<String> ids = new ArrayList<>();
        Assert.isNotFound(StringUtils.isEmpty(byId.getOutBusinessId()), "未找到第三方设备id,请关联第三方id");
        ids.add(byId.getOutBusinessId());

        List<AuthDoorStatusVo> doorStates = null;
        try {
            doorStates = accessControlClient.getDoorStates(ids);
        } catch (Exception e) {
            throw new ResourceNotAcceptableException("获取第三方设备数据失败");
        }
        Assert.isNotAcceptable(CollectionUtils.isEmpty(doorStates), "未获取到门禁状态");
        return doorStates.getFirst().getDoorState().toString();
    }

    @Override
    public ControlResultVo controlDoor(ControlParam param) {
        DoorControlParam doorControlParam = new DoorControlParam();
        doorControlParam.setControlType(param.getControlType());
        ArrayList<String> list = new ArrayList<>();
        list.add(param.getDoorIndexCodes());
        doorControlParam.setDoorIndexCodes(list);
        doorControlParam.setOperator(SecurityContextHolder.getPrincipal().getName());
        List<DoorControlResultVo> doorControlResultVos = null;
        try {
            doorControlResultVos = accessControlClient.doorControl(doorControlParam);
        } catch (Exception e) {
            throw new ResourceNotAcceptableException("控制第三方设备数据失败");
        }
        return ObjectUtils.copying(doorControlResultVos.getFirst(), ControlResultVo.class);
    }

    @Override
    public List<DoorDeviceVo> getDoorDeviceList(DoorDeviceQueryParam doorDeviceQueryParam) {
        List<DoorDeviceVo> doorDeviceVos = doorDeviceMapper.getDoorDevicePage(doorDeviceQueryParam);

        if (CollectionUtils.isNotEmpty(doorDeviceVos)) {
            doorDeviceVos.forEach(s -> {
                DeviceDetailDTO detail = deviceCenterClient.getDeviceById(s.getBusinessId());
                if (Objects.nonNull(detail)) {
                    //扩展区域
                    DeviceBaseArchivesInfoDTO baseArchivesInfo = detail.getBaseArchivesInfo();
                    if (ObjectUtils.isNotEmpty(baseArchivesInfo)) {
                        Pair<Long, String> areaPair = baseArchivesInfo.getArea();
                        Long areaId = areaPair != null ? areaPair.getKey() : null;
                        AreaDTO area = areaId != null ? areaClient.getAreaById(areaId) : null;
                        s.setAreaName(area != null ? area.getName() : null);
                    }
                    //扩展产品名称
                    DeviceProductInfoDTO productInfo = detail.getProductInfo();
                    if (ObjectUtils.isNotEmpty(productInfo)) {
                        s.setDoorModelName(productInfo.getName());
                    }
                    s.setStatus("0");
                }

            });
        }
        return doorDeviceVos;
    }

    private void handleDoorDevice(DoorDeviceVo s, DeviceDetailDTO detail, List<DoorDevice> updateList) {
        boolean isUpdate = false;
        DoorDevice doorDevice = new DoorDevice();
        doorDevice.setId(s.getId());

        DeviceBaseArchivesInfoDTO baseArchives = detail.getBaseArchivesInfo();
        if (baseArchives != null) {
            String name = baseArchives.getName();
            s.setName(name);
            if (name != null && !name.equals(s.getName())) {
                doorDevice.setName(name);
            }
            String doorCode = baseArchives.getCode();
            s.setDoorCode(doorCode);
            if (StringUtils.isNotEmpty(doorCode) && !doorCode.equals(s.getDoorCode())) {
                doorDevice.setDoorCode(doorCode);
                isUpdate = true;
            }
            if (baseArchives.getArea() != null) {
                String areaName = baseArchives.getArea().getVal();
                if (!Objects.equals(areaName, s.getAreaName())) {
                    doorDevice.setDoorArea(baseArchives.getArea().getVal());
                }
            }
        }
        if (detail.getProductInfo() != null) {
            if (!Objects.equals(detail.getProductInfo().getId(), s.getDoorModel())) {
                doorDevice.setDoorModel(detail.getProductInfo().getId());
            }
        }

        if (isUpdate) {
            updateList.add(doorDevice);
        }
    }
}
