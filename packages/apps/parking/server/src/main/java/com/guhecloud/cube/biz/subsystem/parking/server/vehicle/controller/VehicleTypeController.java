package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.controller;

import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleTypeParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleTypeQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.VehicleTypeService;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleTypeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * 车辆类型
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 16:39
 * @Description:
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/vehicle/type")
public class VehicleTypeController {

    private final VehicleTypeService vehicleTypeService;

    /**
     * 新增或更新
     *
     * @param param
     */
    @PostMapping("/save-or-update")
    public Boolean saveOrUpdate(@RequestBody VehicleTypeParam param) {
        return vehicleTypeService.saveOrUpdate(param);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Boolean add(@PathVariable Long id) {
        return DatabaseContextHolder.softDelete(vehicleTypeService, id);
    }

    /**
     * 车辆类型-分页
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/page")
    public Pagination<VehicleTypeVO> getVehicleTypPage(VehicleTypeQueryParam param) {
        return vehicleTypeService.getVehicleTypPage(param);
    }

    /**
     * 车辆类型-列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/list")
    public List<VehicleTypeVO> getVehicleTypList(VehicleTypeQueryParam param) {
        return vehicleTypeService.getVehicleTypList(param);
    }

}
