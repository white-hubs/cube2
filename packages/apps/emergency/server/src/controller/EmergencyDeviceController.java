package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.param.EmergencyDevice2QueryParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyDeviceParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyDeviceService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyDeviceStatVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyDeviceVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
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

import java.util.List;

/**
 * @author Appet
 * @Description 应急设备管理
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyDeviceController {
    private final EmergencyDeviceService emergencyDeviceService;

    /**
     * 功能描述： 应急设备管理添加
     */
    @PostMapping(value = "/emergency-device")
    public Long add(@Valid @RequestBody EmergencyDeviceParam param) {
        return emergencyDeviceService.add(param);
    }

    /**
     * 功能描述： 应急设备管理修改
     */
    @PutMapping(value = "/emergency-device/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EmergencyDeviceParam param) {
        emergencyDeviceService.update(id, param);
    }

    /**
     * 功能描述： 应急设备管理删除
     */
    @DeleteMapping(value = "/emergency-device/{id}")
    public void delete(@PathVariable Long id) {
        emergencyDeviceService.delete(id);
    }

    /**
     * 功能描述： 应急设备管理批量删除
     */
    @DeleteMapping(value = "/emergency-device/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        emergencyDeviceService.batchDelete(ids);
    }

    /**
     * 功能描述： 应急设备管理分页
     */
    @GetMapping(value = "/emergency-device/page")
    public Pagination<EmergencyDeviceVO> getPage(EmergencyDevice2QueryParam param) {
        return emergencyDeviceService.getPage(param);
    }

    /**
     * 功能描述： 应急设备管理详情
     */
    @GetMapping(value = "/emergency-device/{id}")
    public EmergencyDeviceVO getById(@PathVariable Long id) {
        return emergencyDeviceService.getById(id);
    }

    @GetMapping(value = "/emergency-device/stat")
    EmergencyDeviceStatVO getEmergencyDeviceStatForUsed() {
        return emergencyDeviceService.getEmergencyDeviceStatForUsed();
    }

}