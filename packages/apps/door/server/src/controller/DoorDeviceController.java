package com.guhecloud.cube.biz.door.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceListByGenreVO;
import com.guhecloud.cube.biz.door.server.param.ControlParam;
import com.guhecloud.cube.biz.door.server.param.DoorDeviceParam;
import com.guhecloud.cube.biz.door.server.queryparam.DoorDeviceQueryParam;
import com.guhecloud.cube.biz.door.server.service.DoorDeviceService;
import com.guhecloud.cube.biz.door.server.vo.ControlResultVo;
import com.guhecloud.cube.biz.door.server.vo.DoorDeviceVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author lrj
 * @Description 门禁设备
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoorDeviceController {
    private final DoorDeviceService doorDeviceService;

    /**
     * @Description 门禁基础设备数据导入接口
     * @Author lrj
     */
    @GetMapping("/devices")
    public List<DeviceListByGenreVO> getDevices() {

        return doorDeviceService.getDevices();
    }

    /**
     * @Description 导入门禁设备
     * @Author lrj
     */
    @PostMapping("/addDoorDevices")
    public void addDoorDevices(@RequestBody List<DoorDeviceParam> doorDeviceParams) {
        doorDeviceService.addDoorDevices(doorDeviceParams);
    }

    /**
     * @Description 门禁设备分页列表
     * @Author lrj
     */
    @GetMapping("/getDoorDevicePage")
    public Page<DoorDeviceVo> getDoorDevicePage(DoorDeviceQueryParam doorDeviceQueryParam) {
        return doorDeviceService.getDoorDevicePage(doorDeviceQueryParam);
    }

    /**
     * @Description 门禁设备查询
     * @Author lrj
     */
    @GetMapping("/get/door/device/list")
    public List<DoorDeviceVo> getDoorDeviceList(DoorDeviceQueryParam doorDeviceQueryParam) {
        return doorDeviceService.getDoorDeviceList(doorDeviceQueryParam);
    }

    /**
     * @Description 门禁设备移除
     * @Author lrj
     */
    @DeleteMapping("/removeDoorDevice/{id}")
    public void deleteDoorDevice(@PathVariable("id") Long id) {
        doorDeviceService.deleteDoorDeviceBatch(Collections.singletonList(id));
    }

    /**
     * @Description 门禁设备批量移除
     * @Author lrj
     */
    @DeleteMapping("/removeDoorDeviceBatch")
    public void deleteDoorDeviceBatch(@RequestParam("ids") List<Long> ids) {
        doorDeviceService.deleteDoorDeviceBatch(ids);
    }

    /**
     * @Description 通过设备id查询状态
     * @Author lrj
     */
    @GetMapping("/getStatus/{id}")
    public String getStatus(@PathVariable Long id) {
        return doorDeviceService.getStatus(id);
    }

    /**
     * @Description 门禁反控
     * @Author lrj
     */
    @PostMapping("/controlDoor")
    public ControlResultVo controlDoor(@RequestBody ControlParam param) {
        return doorDeviceService.controlDoor(param);
    }

}
