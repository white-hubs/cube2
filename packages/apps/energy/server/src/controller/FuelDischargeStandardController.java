package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.service.FuelDischargeStandardService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.FuelSelectListVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 燃料类型控制层
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FuelDischargeStandardController {

    private final FuelDischargeStandardService fuelDischargeStandardService;

    /**
     * 获取燃料类型下拉列表
     *
     * @param keyword code或名称模糊查询条件
     *
     * @return 下拉列表
     */
    @GetMapping("/fuel/select/list")
    public List<FuelSelectListVO> getSelectList(@RequestParam("keyword") String keyword) {
        return fuelDischargeStandardService.getSelectList(keyword);
    }

    /**
     * 初始化燃料类型
     */
    @GetMapping("/fuel/inert/init")
    public void insertDefaultFuel() {
        fuelDischargeStandardService.insertDefaultFuel();
    }

}
