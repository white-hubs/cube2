package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.dto.DeviceTypeDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.DeviceType;
import com.guhecloud.cube.deliver.project.merchant.mapper.DeviceTypeMapper;
import com.guhecloud.cube.deliver.project.merchant.service.DeviceTypeService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType> implements DeviceTypeService {

    private final DeviceTypeMapper deviceTypeMapper;

    @Override
    public List<String> hasLineStatusOfTypeIds() {
        List<String> deviceTypeCodes = new ArrayList<>(); // 有在线状态的设备类型ID
        deviceTypeCodes.add(ELECTRICAL_FIRE);
        deviceTypeCodes.addAll(CAMERA_DEVICE_TYPES);
        deviceTypeCodes.addAll(SUPERVISION_DEVICE_TYPES);
        deviceTypeCodes.addAll(FIRE_DEVICE_TYPES);
        return deviceTypeMapper.selectList(Wrappers.<DeviceType>lambdaQuery().in(DeviceType::getCode, deviceTypeCodes)).stream()
                .map(DeviceType::getId).collect(Collectors.toList());
    }

    @Override
    public void save(DeviceTypeDTO deviceTypeDTO) {
        if (deviceTypeMapper.selectOne(Wrappers.<DeviceType>lambdaQuery().eq(DeviceType::getName, deviceTypeDTO.getName())) != null) {
            throw new ServiceException("类型名称已存在");
        }

        if (deviceTypeMapper.selectOne(Wrappers.<DeviceType>lambdaQuery().eq(DeviceType::getCode, deviceTypeDTO.getCode())) != null) {
            throw new ServiceException("类型编码已存在");
        }
        deviceTypeMapper.insert(deviceTypeDTO.convert());
    }

    @Override
    public void update(DeviceTypeDTO deviceTypeDTO) {
        DeviceType deviceType_id = deviceTypeMapper.selectById(deviceTypeDTO.getId());
        if (null == deviceType_id) {
            throw new ServiceException("类型不存在");
        }

        DeviceType deviceType_name = deviceTypeMapper.selectOne(
                Wrappers.<DeviceType>lambdaQuery()
                        .eq(DeviceType::getName, deviceTypeDTO.getName())
                                                               );
        if (deviceType_name != null && !deviceType_name.getId().equals(deviceTypeDTO.getId())) {
            throw new ServiceException("名称已经存在");
        }

        DeviceType deviceType_code = deviceTypeMapper.selectOne(
                Wrappers.<DeviceType>lambdaQuery()
                        .eq(DeviceType::getCode, deviceTypeDTO.getCode())
                                                               );
        if (deviceType_code != null && !deviceType_code.getId().equals(deviceTypeDTO.getId())) {
            throw new ServiceException("编码已经存在");
        }

        deviceTypeMapper.updateById(deviceTypeDTO.convert());
    }

    @Override
    public void remove(String id) {
        if (null == deviceTypeMapper.selectById(id)) {
            throw new ServiceException("类型ID不存在");
        }
        deviceTypeMapper.deleteById(id);
    }
}
