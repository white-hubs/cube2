package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.controller;

import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.CarInfoPageQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.CarInfoVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CarInfoController {

    /**
     * 车辆分页列表查询
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/car-info")
    public Pagination<CarInfoVO> getCarInfoPage(@Valid CarInfoPageQueryParam param) {
        return Pagination.empty();
    }
}
