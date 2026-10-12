package com.guhecloud.cube.biz.dispatch.server.controller;

import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceStatusParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceStatusQueryParam;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceStatusService;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceStatusVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
 * @Description 调度设备-状态
 * @date 2022-07-26 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchDeviceStatusController {
    private final DispatchDeviceStatusService dispatchDeviceStatusService;

    /**
     * 功能描述： 调度设备-状态添加
     */
    @PostMapping(value = "/dispatch-device-status")
    public String add(@Valid @RequestBody DispatchDeviceStatusParam param) {
        return dispatchDeviceStatusService.add(param);
    }

    /**
     * 功能描述： 调度设备-状态修改
     */
    @PutMapping(value = "/dispatch-device-status/{id}")
    public void update(@PathVariable String id, @Valid @RequestBody DispatchDeviceStatusParam param) {
        dispatchDeviceStatusService.update(id, param);
    }

    /**
     * 功能描述： 调度设备-状态删除
     */
    @DeleteMapping(value = "/dispatch-device-status/{id}")
    public void delete(@PathVariable String id) {
        dispatchDeviceStatusService.delete(id);
    }

    /**
     * 功能描述： 调度设备-状态批量删除
     */
    @DeleteMapping(value = "/dispatch-device-status/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<String> ids) {
        dispatchDeviceStatusService.batchDelete(ids);
    }

    /**
     * 功能描述： 调度设备-状态分页
     */
    @GetMapping(value = "/dispatch-device-status/page")
    public Pagination<DispatchDeviceStatusVO> getPage(DispatchDeviceStatusQueryParam param) {
        return dispatchDeviceStatusService.getPage(param);
    }

    /**
     * 功能描述： 调度设备-状态列表
     * 可用于前端的定时轮询，查询设备的当前位置
     */
    @GetMapping(value = "/dispatch-device-status/list")
    public List<DispatchDeviceStatusVO> getList(@Valid @NotNull String codes, Boolean onlineStatus) {
        return dispatchDeviceStatusService.getListByCode(codes, onlineStatus);
    }

    /**
     * 功能描述： 调度设备-状态详情
     */
    @GetMapping(value = "/dispatch-device-status/{id}")
    public DispatchDeviceStatusVO getById(@PathVariable String id) {
        return dispatchDeviceStatusService.getById(id);
    }

}