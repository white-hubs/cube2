package com.guhecloud.cube.biz.dispatch.server.controller;

import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceGroupReferParam;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceGroupReferService;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceGroupReferVO;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * @author Appet
 * @Description 调度组与设备关联
 * @date 2022-08-09 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchDeviceGroupReferController {
    private final DispatchDeviceGroupReferService dispatchDeviceGroupReferService;

    /**
     * 功能描述： 调度组与设备关联添加
     * groupId 和 deviceId 都支持多个id,多个id用逗号分割
     */
    @PostMapping(value = "/dispatch-device-group-refer")
    public void add(@Valid @RequestBody DispatchDeviceGroupReferParam param) {
        dispatchDeviceGroupReferService.add(param);
    }

    /**
     * 功能描述： 调度组与设备关联删除
     */
    @DeleteMapping(value = "/dispatch-device-group-refer/{id}")
    public void delete(@PathVariable Long id) {
        dispatchDeviceGroupReferService.delete(id);
    }

    /**
     * 功能描述： 调度组与设备关联批量删除
     */
    @DeleteMapping(value = "/dispatch-device-group-refer/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        dispatchDeviceGroupReferService.batchDelete(ids);
    }

    /**
     * 功能描述： 调度组与设备关联详情
     */
    @GetMapping(value = "/dispatch-device-group-refer/{id}")
    public DispatchDeviceGroupReferVO getById(@PathVariable Long id) {
        return dispatchDeviceGroupReferService.getById(id);
    }

}