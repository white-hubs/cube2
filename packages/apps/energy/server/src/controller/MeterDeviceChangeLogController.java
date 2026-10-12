package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterDeviceChangeLogParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterDeviceChangeLogQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.MeterDeviceChangeLogService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterDeviceChangeLogVO;
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
 * @Description 仪表设备更换记录
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeterDeviceChangeLogController {
    private final MeterDeviceChangeLogService meterDeviceChangeLogService;

    /**
     * 功能描述： 仪表设备更换记录添加
     */
    @PostMapping(value = "/meter-device-change-log")
    public Long add(@Valid @RequestBody MeterDeviceChangeLogParam param) {
        return meterDeviceChangeLogService.add(param);
    }

    /**
     * 功能描述： 仪表设备更换记录修改
     */
    @PutMapping(value = "/meter-device-change-log/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody MeterDeviceChangeLogParam param) {
        meterDeviceChangeLogService.update(id, param);
    }

    /**
     * 功能描述： 仪表设备更换记录删除
     */
    @DeleteMapping(value = "/meter-device-change-log/{id}")
    public void delete(@PathVariable Long id) {
        meterDeviceChangeLogService.delete(id);
    }

    /**
     * 功能描述： 仪表设备更换记录批量删除
     */
    @DeleteMapping(value = "/meter-device-change-log/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        meterDeviceChangeLogService.batchDelete(ids);
    }

    /**
     * 功能描述： 仪表设备更换记录分页
     */
    @GetMapping(value = "/meter-device-change-log/page")
    public Pagination<MeterDeviceChangeLogVO> getPage(MeterDeviceChangeLogQueryParam param) {
        return meterDeviceChangeLogService.getPage(param);
    }

    /**
     * 功能描述： 仪表设备更换记录详情
     */
    @GetMapping(value = "/meter-device-change-log/{id}")
    public MeterDeviceChangeLogVO getById(@PathVariable Long id) {
        return meterDeviceChangeLogService.getById(id);
    }

}