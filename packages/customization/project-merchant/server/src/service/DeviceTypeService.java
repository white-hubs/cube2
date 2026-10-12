package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.google.common.collect.ImmutableList;
import com.guhecloud.cube.deliver.project.merchant.dto.DeviceTypeDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.DeviceType;

import java.util.List;

public interface DeviceTypeService extends IService<DeviceType> {

    public static final String ELECTRICAL_FIRE = "electrical_fire";       // 电气终端
    public static final String CODE_ZHWD = "zhwd";       // 智慧微断

    public static final String CODE_COM_CAMERA = "com_camera";            // 普通摄像机
    public static final String CODE_INFRARED_CAMERA = "infrared_camera";  // 红外摄像机
    public static final String CODE_THERMAL_CAMERA = "thermal_camera";  // 热成像摄像机
    public static final String CODE_ELEVATOR = "elevator";  // 电梯

    public static final String CODE_UPS = "UPS";  // UPS电源
    public static final String CODE_TH = "TH";  // 温湿度
    public static final String CODE_KT = "KT";  // 空调

    // 目前接入消防设备只有无线水压变送器这一种了  2022-12-23
    //    public static final String CODE_HYDRANT = "hydrant"; // 消防栓
    //    public static final String CODE_FIRE_PUMP = "fire_pump"; // 消防水泵
    //    public static final String CODE_FIRE_POOL_LEVEL = "fire_pool_level"; // 消防水池液位
    public static final String CODE_FIRE_BSQ = "bsq"; // 无线水压变送器

    // 动环设备类型, 目前包括：ups、温湿度、空调
    public static final List<String> SUPERVISION_DEVICE_TYPES = ImmutableList.of(CODE_UPS, CODE_KT, CODE_TH);
    // 摄像机类型，包括：普通摄像机、红外摄像机、热成像摄像机
    public static final List<String> CAMERA_DEVICE_TYPES = ImmutableList.of(CODE_COM_CAMERA, CODE_INFRARED_CAMERA, CODE_THERMAL_CAMERA);
    // 消防设备类型，目前包括：无线水压变送器
    public static final List<String> FIRE_DEVICE_TYPES = ImmutableList.of(CODE_FIRE_BSQ);
    // 电气设备类型，目前包括：电气终端、智慧微断
    public static final List<String> ELECTRICAL_DEVICE_TYPES = ImmutableList.of(ELECTRICAL_FIRE, CODE_ZHWD);

    /**
     * 获取所有有在离线状态的设备类型ID
     *
     * @return
     */
    List<String> hasLineStatusOfTypeIds();

    void save(DeviceTypeDTO deviceTypeDTO);

    void update(DeviceTypeDTO deviceTypeDTO);

    void remove(String id);
}
