package com.guhecloud.cube.biz.subsystem.parking.server.guidance.controller;

import com.guhecloud.cube.biz.subsystem.parking.server.guidance.param.VehicleGuideBoardConfigParam;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.param.VehicleGuideBoardConfigQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.service.VehicleGuideBoardConfigService;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.vo.VehicleGuideBoardConfigVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
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

/**
 * 车辆引导牌配置 Controller
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/guidance/vehicle-guide-board-config")
public class VehicleGuideBoardConfigController {

    private final VehicleGuideBoardConfigService vehicleGuideBoardConfigService;

    /**
     * 新增车辆引导牌配置（包含内容配置）
     *
     * @param param 配置参数
     * @return 是否成功
     */
    @PostMapping("/save")
    public Boolean save(@Valid @RequestBody VehicleGuideBoardConfigParam param) {
        return vehicleGuideBoardConfigService.save(param);
    }

    /**
     * 更新车辆引导牌配置（包含内容配置）
     *
     * @param param 配置参数
     * @return 是否成功
     */
    @PostMapping("/update")
    public Boolean update(@Valid @RequestBody VehicleGuideBoardConfigParam param) {
        return vehicleGuideBoardConfigService.update(param);
    }

    /**
     * 删除车辆引导牌配置（同时删除关联的内容配置）
     *
     * @param id 配置id
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id) {
        return vehicleGuideBoardConfigService.delete(id);
    }

    /**
     * 分页查询车辆引导牌配置
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Pagination<VehicleGuideBoardConfigVO> page(@Valid VehicleGuideBoardConfigQueryParam param) {
        return vehicleGuideBoardConfigService.page(param);
    }

    /**
     * 获取车辆引导牌配置详情（包含内容配置）
     *
     * @param id 配置id
     * @return 配置详情
     */
    @GetMapping("/{id}")
    public VehicleGuideBoardConfigVO getDetail(@PathVariable Long id) {
        return vehicleGuideBoardConfigService.getDetail(id);
    }
}

