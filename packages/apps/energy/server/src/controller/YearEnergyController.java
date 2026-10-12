package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.param.YearEnergyParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageStatParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.YearEnergyQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.YearEnergyService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyTypeStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.YearEnergyVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 年用能
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class YearEnergyController {

    private final YearEnergyService yearEnergyService;

    /**
     * 获取年用能分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/yearEnergy/page")
    public Pagination<YearEnergyVO> getYearEnergyPage(YearEnergyQueryParam param) {
        return yearEnergyService.getYearEnergyPage(param);
    }

    /**
     * 年用能详情
     *
     * @param param id
     *
     * @return 详情
     */
    @GetMapping("/yearEnergy/detail")
    public YearEnergyVO getYearEnergyDetail(YearEnergyQueryParam param) {
        return yearEnergyService.getYearEnergyDetail(param);
    }

    /**
     * 新增年用能
     *
     * @param param 用能
     *
     * @return id
     */
    @PostMapping("/yearEnergy/add")
    public Long addYearEnergy(@RequestBody YearEnergyParam param) {
        param.setSourceType("handle");
        return yearEnergyService.addYearEnergy(param);
    }

    /**
     * 编辑年用能
     *
     * @param param 用能
     */
    @PutMapping("/yearEnergy/edit")
    public void editYearEnergy(@RequestBody YearEnergyParam param) {
        param.setSourceType("handle");
        yearEnergyService.editYearEnergy(param);
    }

    /**
     * 删除年用能
     *
     * @param id 主键
     */
    @DeleteMapping("/yearEnergy/remove")
    public void removeYearEnergy(@RequestParam("id") Long id) {
        yearEnergyService.removeYearEnergy(id);
    }

    /**
     * 年用能趋势
     *
     * @param param 时间段，用能类型
     *
     * @return 趋势
     */
    @GetMapping("/yearEnergy/stat/yearTrend")
    public List<EnergyTypeStatVO> getYearEnergyTrend(YearEnergyQueryParam param) {
        return yearEnergyService.getYearEnergyTrend(param);
    }

    /**
     * 年用能日折算
     *
     * @param param 类型和时间段
     *
     * @return 用能
     */
    @GetMapping("/yearEnergy/stat/conversion")
    public EnergyTypeStatVO getYearEnergyConversionDaily(EnergyDailyUsageStatParam param) {
        return yearEnergyService.getYearEnergyConversionDaily(param);
    }
}
