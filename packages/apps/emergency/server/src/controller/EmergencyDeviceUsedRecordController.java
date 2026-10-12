package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.param.EmergencyDeviceUsedRecordParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyDeviceUsedRecordQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyDeviceUsedRecordService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyDeviceUsedRecordVO;
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
 * @Description 应急设备使用记录
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyDeviceUsedRecordController {
    private final EmergencyDeviceUsedRecordService emergencyDeviceUsedRecordService;

    /**
     * 功能描述： 应急设备使用记录添加
     */
    @PostMapping(value = "/emergency-device-used-record")
    public Long add(@Valid @RequestBody EmergencyDeviceUsedRecordParam param) {
        return emergencyDeviceUsedRecordService.add(param);
    }

    /**
     * 功能描述： 应急设备使用记录修改
     */
    @PutMapping(value = "/emergency-device-used-record/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EmergencyDeviceUsedRecordParam param) {
        emergencyDeviceUsedRecordService.update(id, param);
    }

    /**
     * 功能描述： 应急设备使用记录删除
     */
    @DeleteMapping(value = "/emergency-device-used-record/{id}")
    public void delete(@PathVariable Long id) {
        emergencyDeviceUsedRecordService.delete(id);
    }

    /**
     * 功能描述： 应急设备使用记录批量删除
     */
    @DeleteMapping(value = "/emergency-device-used-record/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        emergencyDeviceUsedRecordService.batchDelete(ids);
    }

    /**
     * 功能描述： 应急设备使用记录分页
     */
    @GetMapping(value = "/emergency-device-used-record/page")
    public Pagination<EmergencyDeviceUsedRecordVO> getPage(EmergencyDeviceUsedRecordQueryParam param) {
        return emergencyDeviceUsedRecordService.getPage(param);
    }

    /**
     * 功能描述： 应急设备使用记录详情
     */
    @GetMapping(value = "/emergency-device-used-record/{id}")
    public EmergencyDeviceUsedRecordVO getById(@PathVariable Long id) {
        return emergencyDeviceUsedRecordService.getById(id);
    }

}