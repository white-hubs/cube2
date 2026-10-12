package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.controller;

import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleInfoBatchParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleInfoQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.VehicleInfoService;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleInfoDetailVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleInfoVO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 车辆管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleInfoController {
    private final VehicleInfoService vehicleInfoService;

    /**
     * 新车车辆信息
     */
    @PostMapping("/vehicle/saveOrUpdate")
    public Boolean saveOrUpdate(@RequestBody VehicleInfoVO param) {
        return vehicleInfoService.saveOrUpdate(param);
    }

    /**
     * 删除车辆信息
     */
    @DeleteMapping("/vehicle/{id}")
    public Boolean delete(@PathVariable Long id) {
        return DatabaseContextHolder.softDelete(vehicleInfoService, id);
    }

    /**
     * 车辆分页列表
     *
     * @return 分页
     */
    @GetMapping("/vehicle/page")
    public Pagination<VehicleInfoVO> getPage(VehicleInfoQueryParam param) {
        return vehicleInfoService.getPage(param);
    }

    /**
     * 获取车辆详情信息
     */
    @GetMapping("/vehicle/{id}")
    public VehicleInfoDetailVO getVehicleInfo(@PathVariable Long id) {
        return vehicleInfoService.getVehicleInfo(id);
    }

    /**
     * 根据车牌获取车辆信息
     */
    @GetMapping("/vehicle/code")
    public VehicleInfoDetailVO getVehicleInfo(@RequestParam("vehicleNo") String vehicleNo) {
        return vehicleInfoService.getVehicleInfoByCode(vehicleNo);
    }

    /**
     * 获取所有车辆信息(完整po信息)
     *
     * @return 车辆集合
     */
    @GetMapping("/vehicle/getAllVehicleEntity")
    public List<VehicleInfoVO> getAllVehicleEntity() {
        return vehicleInfoService.getAllVehicleEntity();
    }

    /**
     * 车辆批量更新
     */
    @PostMapping("/vehicle/saveOrUpdateBatch")
    public void saveOrUpdateBatch(@RequestBody VehicleInfoBatchParam param) {
        vehicleInfoService.saveOrUpdateBatch(param);
    }
}
