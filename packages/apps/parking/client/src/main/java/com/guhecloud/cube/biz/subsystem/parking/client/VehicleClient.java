package com.guhecloud.cube.biz.subsystem.parking.client;

import com.guhecloud.cube.biz.subsystem.parking.client.param.VehicleInfoQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.VehicleInfoBatchParam;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.VehicleInfoVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 车辆client
 */
@FeignClient(name = "subsystemParking")
public interface VehicleClient {
    /**
     * 车辆分页列表
     *
     * @return 分页
     */
    @GetMapping("/vehicle/page")
    Pagination<VehicleInfoVO> getPage(@SpringQueryMap VehicleInfoQueryParam param);

    /**
     * 获取所有车辆信息(完整po信息)
     *
     * @return 车辆集合
     */
    @GetMapping("/vehicle/getAllVehicleEntity")
    List<VehicleInfoVO> getAllVehicleEntity();

    /**
     * 车辆批量更新
     */
    @PostMapping("/vehicle/saveOrUpdateBatch")
    void saveOrUpdateBatch(@RequestBody VehicleInfoBatchParam param);
}
