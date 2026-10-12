package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.DateUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.PinYinUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.BimInfoDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.DeviceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.DeviceExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Device;
import com.guhecloud.cube.deliver.project.merchant.mapper.DeviceMapper;
import com.guhecloud.cube.deliver.project.merchant.service.DeviceService;
import com.guhecloud.cube.deliver.project.merchant.dto.*;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    private final DeviceMapper deviceMapper;
    //    private final FileApi fileApi;
    //    private final BimInfoService bimInfoService;
    //    private final CameraServiceApi cameraServiceApi;
    //    private final ElementApi elementApi;
    //    private final SysDeptApi sysDeptApi;
    //    private final StreamConfigApi streamConfigApi;
    //    private final DeviceLogsService deviceLogsService;
    //    private final DeviceOnlineRecordService recordService;
    //    private final DeviceElevatorPropertyService elevatorPropertyService;
    //    private final CampatrolApi campatrolApi;
    //    private final AlarmApi alarmApi;
    //    private final SysUserApi userApi;
    //    private final SysProjectUserApi projectUserApi;

    @Override
    public void save(DeviceDTO dto, LoginUser loginUser) {

        if (deviceMapper.selectOne(Wrappers.<Device>lambdaQuery().eq(Device::getProjectId, dto.getProjectId())
                                           .eq(Device::getCode, dto.getCode())) != null) {
            throw new ServiceException("编码已经存在");
        }
        if (deviceMapper.selectOne(Wrappers.<Device>lambdaQuery().eq(Device::getProjectId, dto.getProjectId())
                                           .eq(Device::getName, dto.getName())) != null) {
            throw new ServiceException("名称已经存在");
        }
        //if (dto.getMaintCycle() != null && dto.getMaintCycle() < 0) throw new ServiceException("维保周期不能小于0");
        dto.setPinyin(PinYinUtils.getHead(dto.getName()));
        dto.setCode(BizCodeUtils.generate("DEV"));

        Device device = dto.convert();
        device.setNearbyCams(this.convertNearbyCamIds(dto.getNearbyCamIds()));
        List<String> areaIds = dto.getAreaIds();
        if (areaIds != null && areaIds.size() > 0) {
            device.setAreaId1(areaIds.get(0));
            device.setAreaId2(areaIds.size() > 1 ? areaIds.get(1) : "");
            device.setAreaId3(areaIds.size() > 2 ? areaIds.get(2) : "");
        }
        if (dto.getExpireDate() != null) {
            device.setTimeLimit(DateUtils.getIntervalByDays(LocalDateTime.now(), dto.getExpireDate()));
        }
        device.setStatus(STATUS_NORMAL);
        //向H5流媒体服务器中添加设备
        this.saveOrUpdateH5StreamDev(device);
        // 根据用户默认填写管理部门
        this.saveManageDept(device, loginUser);
        deviceMapper.insert(device);
        dto.setId(device.getId());
        // 保存bim绑定信息
        this.saveBimInfo(device.getId(), dto.getProjectId(), dto.getBimInfo());
        // 如果有上传的图片就关联图纸 采购合同 资产实景
        this.saveFile(device.getId(), dto);
        // 保存特定设备的属性
        this.saveOrUpdateSpecialDevice(dto);
    }

    private void saveManageDept(Device device, LoginUser loginUser) {
        //        DeptDTO deptDTO = userApi.getUserDeptById(device.getProjectId(), loginUser.getId());
        //        if (deptDTO != null && StringUtils.isEmpty(device.getDeptId())) {
        //            device.setDeptId(deptDTO.getId());
        //        }
    }

    @Override
    public void update(DeviceDTO deviceDTO, String userId) {
        if (null == deviceMapper.selectById(deviceDTO.getId())) {
            throw new ServiceException("设备不存在");
        }
        Device device_code = deviceMapper.selectOne(
                Wrappers.<Device>lambdaQuery()
                        .eq(Device::getProjectId, deviceDTO.getProjectId())
                        .eq(Device::getCode, deviceDTO.getCode())
                                                   );
        if (device_code != null && !device_code.getId().equals(deviceDTO.getId())) {
            throw new ServiceException("编码已经存在");
        }

        Device device_name = deviceMapper.selectOne(
                Wrappers.<Device>lambdaQuery()
                        .eq(Device::getProjectId, deviceDTO.getProjectId())
                        .eq(Device::getName, deviceDTO.getName())
                                                   );
        if (device_name != null && !device_name.getId().equals(deviceDTO.getId())) {
            throw new ServiceException("名称已经存在");
        }

        //Iot设备解绑并且设备为在线时，需要设为离线
        if (deviceDTO.getOnline() == 1 && StringUtils.isEmpty(deviceDTO.getIotDeviceId())) {
            if (this.lambdaQuery().eq(Device::getId, deviceDTO.getId()).ne(Device::getIotDeviceId, "").one() != null) {
                deviceDTO.setOnline(0);
            }
        }

        //同新增，判断三个
        this.saveFile(deviceDTO.getId(), deviceDTO);
        deviceDTO.setPinyin(PinYinUtils.getHead(deviceDTO.getName()));
        Device device = deviceDTO.convert();
        device.setNearbyCams(this.convertNearbyCamIds(deviceDTO.getNearbyCamIds()));
        List<String> areaIds = deviceDTO.getAreaIds();
        if (areaIds == null || areaIds.size() == 0) {
            throw new ServiceException("区域不能为空");
        }
        device.setAreaId1(areaIds.get(0));
        device.setAreaId2(areaIds.size() > 1 ? areaIds.get(1) : "");
        device.setAreaId3(areaIds.size() > 2 ? areaIds.get(2) : "");
        //向H5流媒体服务器中添加设备
        this.saveOrUpdateH5StreamDev(device);

        if (deviceDTO.getExpireDate() != null) {
            device.setTimeLimit(DateUtils.getIntervalByDays(LocalDateTime.now(), deviceDTO.getExpireDate()));
        }

        Device oldDevice = deviceMapper.selectById(device.getId());

        deviceMapper.updateById(device);
        // 保存bim绑定信息
        this.saveBimInfo(device.getId(), deviceDTO.getProjectId(), deviceDTO.getBimInfo());

        //        deviceLogsService.save(oldDevice, device, device.getProjectId(), userId);
        // 保存特定设备的属性
        this.saveOrUpdateSpecialDevice(deviceDTO);
    }

    @Override
    public void remove(String id) {
        //        List<CampatrolDTO> campatrolDTOS = campatrolApi.getByDeviceId(id);
        //        if (null != campatrolDTOS && campatrolDTOS.size() > 0) {
        //            throw new ServiceException("该设备绑定了视频巡逻路线【" + campatrolDTOS.get(0).getName() + "】等,请先在巡逻路线中删除设备！");
        //        }
        //        deviceMapper.deleteById(id);
        //        bimInfoService.remove(BIMINFO_BUSINESS_TYPE, id);
        //        this.removeFile(id);
    }

    @Override
    public void removeBatch(String[] ids) {
        if (ids != null) {
            Arrays.asList(ids).forEach(this::remove);
        }
    }

    @Override
    public void updateField(DeviceDTO deviceDTO) {
        //        if (null == deviceMapper.selectById(deviceDTO.getId())) throw new ServiceException("设备不存在");
        deviceMapper.updateById(deviceDTO.convert());
    }

    @Override
    public void updateData(String projectId, Map<String, String> nameMap) {
        List<Device> devices = deviceMapper.selectList(Wrappers.<Device>lambdaQuery().eq(Device::getProjectId, projectId));
        devices.forEach(device -> {
            String newName = nameMap.getOrDefault(device.getName(), null);
            if (StringUtils.isNotBlank(newName)) {
                device.setName(newName);
                device.setPinyin(PinYinUtils.getHead(device.getName()));
                deviceMapper.updateById(device);
            }
        });
    }

    @Override
    public void updateCamOnLineJob() {
        //        try {
        //            List<StreamConfigDTO> streamConfigDTOS = streamConfigApi.getList();
        //            streamConfigDTOS.forEach(this::updateCamOnLine);
        //        } catch (Exception e) {
        //            log.error("获取流媒体配置信息异常", e);
        //            throw new ServiceException("获取流媒体配置信息异常");
        //        }
    }

    @Override
    public List<DeviceDTO> filterCameraByAuth(String projectId, LoginUser loginUser, List<DeviceDTO> dtos) {
        //        List<String> areaIds = userApi.getUserAuthAreaIds(projectId, loginUser.getId());
        //        List<String> userProjectIds = projectUserApi.getUserProjectIds(loginUser.getId()); //查询用户所在的项目
        //        userProjectIds.removeIf(projectId::equals); // 删除当前项目
        //        if (userProjectIds.size() > 0) { // 用户不止在当前项目,设备的可见性生效
        //            dtos = dtos.parallelStream().filter(DeviceDTO::getVisibility).collect(Collectors.toList());
        //        }
        //        boolean isProjectManager = projectUserApi.checkUserIsManager(projectId, loginUser.getId());
        //        // 非管理员受到用户区域权限的限制
        //        if (!isProjectManager) {
        //            dtos = this.checkedAuth(dtos, areaIds);
        //        }
        return dtos;
    }

    private List<DeviceDTO> checkedAuth(List<DeviceDTO> devDTOPage, List<String> areaIds) {
        if (areaIds == null || areaIds.size() < 1) {
            return new ArrayList<>();
        }
        return devDTOPage.stream().map(dto -> {
            String areaId1 = dto.getAreaId1();
            String areaId2 = dto.getAreaId2();
            String areaId3 = dto.getAreaId3();
            boolean flag = true;
            if (StringUtils.isNotEmpty(areaId1)) {
                flag = areaIds.parallelStream().anyMatch(id -> id.equals(areaId1));
            }
            if (StringUtils.isNotEmpty(areaId2)) {
                flag = areaIds.parallelStream().anyMatch(id -> id.equals(areaId2));
            }
            if (StringUtils.isNotEmpty(areaId3)) {
                flag = areaIds.parallelStream().anyMatch(id -> id.equals(areaId3));
            }
            if (flag) {
                return dto;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private String convertNearbyCamIds(List<String> nearByCamIds) {
        String cams = "";
        if (nearByCamIds != null && nearByCamIds.size() > 0) {
            cams = String.join(",", nearByCamIds);
        }
        return cams;
    }

    private String getDeptId(Map<String, String> deptNameIdMap, String projectId, String deptName) {
        String deptId = deptNameIdMap.getOrDefault(deptName, "");
        //        if (StringUtils.isEmpty(deptId)) {
        //            try {
        //                DeptDTO deptDTO = sysDeptApi.getByName(projectId, deptName);
        //                if (deptDTO != null) {
        //                    deptId = deptDTO.getId();
        //                    deptNameIdMap.put(deptName, deptId);
        //                }
        //            } catch (Exception e) {
        //                log.error("通过部门名查ID异常", e);
        //            }
        //        }
        return deptId;
    }

    private void saveOrUpdateSpecialDevice(DeviceDTO deviceDTO) {
        // 电梯设备
        //        if (DeviceTypeService.CODE_ELEVATOR.equals(deviceDTO.getTypeCode())) {
        //            DeviceElevatorProperty elevatorProperty = deviceDTO.getElevatorProperty();
        //            if (null == elevatorProperty) return;
        //            if (StringUtils.isNotEmpty(elevatorProperty.getId())) {
        //                elevatorPropertyService.updateById(elevatorProperty);
        //            } else {
        //                elevatorProperty.setDeviceId(deviceDTO.getId());
        //                elevatorPropertyService.save(elevatorProperty);
        //            }
        //        }
    }

    private void updateExcelDev(Device oldDev, Device device) {
        oldDev.setAreaId1(device.getAreaId1());
        oldDev.setAreaId2(device.getAreaId2());
        oldDev.setAreaId3(device.getAreaId3());
        oldDev.setTypeId(device.getTypeId());
        oldDev.setIotDeviceId(device.getIotDeviceId());
        oldDev.setIotDeviceType(device.getIotDeviceType());
        oldDev.setPrice(device.getPrice());
        oldDev.setInstallAddress(device.getInstallAddress());
        oldDev.setInstallDate(device.getInstallDate());
        oldDev.setStartDate(device.getStartDate());
        oldDev.setExpireDate(device.getExpireDate());
        oldDev.setProductionDate(device.getProductionDate());
        oldDev.setMaintDate(device.getMaintDate());
        oldDev.setManufacturer(device.getManufacturer());
        oldDev.setAccount(device.getAccount());
        oldDev.setPassword(device.getPassword());
        oldDev.setIpAddr(device.getIpAddr());
        oldDev.setPort(device.getPort());
        oldDev.setToken(device.getToken());
        oldDev.setSpecification(device.getSpecification());
        oldDev.setDeptId(device.getDeptId());
        oldDev.setContactPerson(device.getContactPerson());
        oldDev.setContactPhone(device.getContactPhone());
        oldDev.setContactAddress(device.getContactAddress());

        oldDev.setUpdateBy(device.getUpdateBy());
        oldDev.setUpdateTime(device.getUpdateTime());
    }

    private void saveFile(String deviceId, DeviceDTO deviceDTO) {
        //        if (deviceDTO.getRelatedDrawingIds() != null)
        //            deviceDTO.getRelatedDrawingIds().forEach(id -> fileApi.mark(id, RELATEDDRAWING, deviceId));
        //        if (deviceDTO.getLiveActionIds() != null)
        //            deviceDTO.getLiveActionIds().forEach(id -> fileApi.mark(id, LIVEACTION, deviceId));
        //        if (deviceDTO.getPurchaseContractIds() != null)
        //            deviceDTO.getPurchaseContractIds().forEach(id -> fileApi.mark(id, PURCHASECONTRACT, deviceId));
    }

    private void removeFile(String deviceId) {
        //        fileApi.removeBatch(RELATEDDRAWING, deviceId);
        //        fileApi.removeBatch(LIVEACTION, deviceId);
        //        fileApi.removeBatch(PURCHASECONTRACT, deviceId);
    }

    private void saveBimInfo(String id, String projectId, String ecode) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(ecode)) {
            return;
        }
        BimInfoDTO dto = new BimInfoDTO();
        dto.setProjectId(projectId);
        dto.setBusinessType(BIMINFO_BUSINESS_TYPE);
        dto.setBusinessKey(id);
        dto.setEcode(ecode);
        dto.setIsImport(true);

        //        try {
        //            ElementDTO elementDTO = elementApi.getByCode(ecode);
        //            if (elementDTO != null) dto.setObjectId(elementDTO.getObjectId());
        //        } catch (Exception e) {
        //            log.error("根据Ecode查询构建信息异常", e);
        //        }
        //
        //        bimInfoService.saveOrUpdate(dto);
    }

    private void saveBimInfo(String id, String projectId, BimInfoDTO bimInfoDTO) {
        if (bimInfoDTO != null) {
            bimInfoDTO.setProjectId(projectId);
            bimInfoDTO.setBusinessType(BIMINFO_BUSINESS_TYPE);
            bimInfoDTO.setBusinessKey(id);
            //            bimInfoService.saveOrUpdate(bimInfoDTO);
        }
    }

    private void saveOrUpdateH5StreamDev(Device device) {
        //        if (StringUtils.isEmpty(device.getIpAddr()) || StringUtils.isEmpty(device.getPort())) return;
        //        if (StringUtils.isEmpty(device.getAccount()) || StringUtils.isEmpty(device.getPassword())) return;
        //        String deviceId = device.getId();
        //        String token = device.getToken().trim();
        //        if (StringUtils.isEmpty(token)) throw new ServiceException("摄像机Token不能为空");
        //        //项目ID，设备ID，token，判断是否有重复的Token
        //        List<Device> devices_Token = deviceMapper.selectList(Wrappers.<Device>lambdaQuery().eq(Device::getProjectId, device.getProjectId())
        //                .eq(Device::getToken, token).ne(StringUtils.isNotEmpty(deviceId), Device::getId, deviceId));
        //        if (devices_Token.size() != 0) throw new ServiceException("Token不能重复");
        //        //修改时，token未被修改，则返回
        //        if (deviceId != null && token.equals(deviceMapper.selectById(deviceId).getToken())) return;
        //        addH5Dev(device);
    }

    private void exportH5StreamDevs(Device device) {
        if (StringUtils.isEmpty(device.getIpAddr()) || StringUtils.isEmpty(device.getPort())) {
            return;
        }
        if (StringUtils.isEmpty(device.getAccount()) || StringUtils.isEmpty(device.getPassword())) {
            return;
        }
        String token = device.getToken().trim();
        if (StringUtils.isNotEmpty(token)) {
            /*List<Device> devices_Token = deviceMapper.selectList(Wrappers.<Device>lambdaQuery().eq(Device::getProjectId, device.getProjectId()).eq(Device::getToken, token));
            if (devices_Token.size() != 0) throw new ServiceException("Token不能重复");*/
            Device deviceByToken = deviceMapper.selectOne(Wrappers.<Device>lambdaQuery().eq(Device::getProjectId, device.getProjectId())
                                                                  .eq(Device::getToken, token));
            if (deviceByToken != null && !deviceByToken.getId().equals(device.getId())) {
                //                throw new ServiceException("Token不能重复");
            }

        } else {
            String ipstr = device.getIpAddr().replace(".", "");
            String timeStamp = DateUtils.getDate(LocalDateTime.now(), "HHmmss");
            device.setToken(ipstr + "_" + timeStamp);
        }

    }

    private void getIdOrCode(DeviceExcelDTO device) {
        String[] areaId1 = device.getAreaId1().split("_");
        device.setAreaId1(areaId1[areaId1.length - 1]);
        String[] areaId2 = device.getAreaId2().split("_");
        device.setAreaId2(areaId2[areaId2.length - 1]);
        String[] areaId3 = device.getAreaId3().split("_");
        device.setAreaId3(areaId3[areaId3.length - 1]);
        String[] typeId = device.getTypeId().split("_");
        device.setTypeId(typeId[typeId.length - 1]);
        String[] specifications = device.getSpecification().split("_", 3);
        device.setSpecification(specifications[specifications.length - 1]);
    }

}
