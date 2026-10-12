package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeRulesParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeRulesQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyChargeRulesService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeRulesVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 能耗计费规则
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyChargeRulesController {

    private final EnergyChargeRulesService energyChargeRulesService;

    /**
     * 获取计费规则分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/energy-charge-rules/page")
    public Pagination<EnergyChargeRulesVO> getEnergyChargeRulePage(EnergyChargeRulesQueryParam param) {
        return energyChargeRulesService.getEnergyChargeRulePage(param);
    }

    /**
     * 规则详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    @GetMapping("/energy-charge-rules/detail")
    public EnergyChargeRulesVO getEnergyChargeRuleDetail(@RequestParam("id") Long id) {
        return energyChargeRulesService.getEnergyChargeRuleDetail(id);
    }

    /**
     * 计费规则添加
     *
     * @param param 计费规则
     *
     * @return 主键
     */
    @PostMapping("/energy-charge-rules/add")
    public Long addEnergyChargeRule(@RequestBody @Valid EnergyChargeRulesParam param) {
        return energyChargeRulesService.saveOrUpdateEnergyChargeRule(param);
    }

    /**
     * 计费规则编辑
     *
     * @param param 计费规则
     *
     * @return 主键
     */
    @PostMapping("/energy-charge-rules/edit")
    public Long editEnergyChargeRule(@RequestBody @Valid EnergyChargeRulesParam param) {
        return energyChargeRulesService.saveOrUpdateEnergyChargeRule(param);
    }

    /**
     * 删除规则
     *
     * @param id 主键
     */
    @DeleteMapping("/energy-charge-rules/remove")
    public void removeEnergyChargeRule(@RequestParam("id") Long id) {
        energyChargeRulesService.removeEnergyChargeRule(id);
    }
}
