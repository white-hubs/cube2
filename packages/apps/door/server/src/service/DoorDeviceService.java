package com.guhecloud.cube.biz.door.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceListByGenreVO;
import com.guhecloud.cube.biz.door.server.entity.DoorDevice;
import com.guhecloud.cube.biz.door.server.param.ControlParam;
import com.guhecloud.cube.biz.door.server.param.DoorDeviceParam;
import com.guhecloud.cube.biz.door.server.queryparam.DoorDeviceQueryParam;
import com.guhecloud.cube.biz.door.server.vo.ControlResultVo;
import com.guhecloud.cube.biz.door.server.vo.DoorDeviceVo;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.AuthDoorStatusVo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface DoorDeviceService extends IService<DoorDevice> {

    List<DeviceListByGenreVO> getDevices();

    void addDoorDevices(List<DoorDeviceParam> doorDeviceParams);

    Page<DoorDeviceVo> getDoorDevicePage(DoorDeviceQueryParam doorDeviceQueryParam);

    void deleteDoorDevice(Long id);

    void deleteDoorDeviceBatch(List<Long> ids);

    void updateStatus(List<AuthDoorStatusVo> doorStates);

    String getStatus(Long id);

    ControlResultVo controlDoor(@RequestBody ControlParam param);

    List<DoorDeviceVo> getDoorDeviceList(DoorDeviceQueryParam doorDeviceQueryParam);

}
