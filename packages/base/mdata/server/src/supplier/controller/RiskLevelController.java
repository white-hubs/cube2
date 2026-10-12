package com.guhecloud.cube.biz.mdata.server.supplier.controller;

import com.guhecloud.cube.biz.mdata.server.supplier.dto.RiskLevelDTO;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.RiskLevel;
import com.guhecloud.cube.biz.mdata.server.supplier.param.RiskLevelCreateParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.RiskLevelUpdateParam;
import com.guhecloud.cube.biz.mdata.server.supplier.service.RiskLevelService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 风险级别接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskLevelController {

    private final RiskLevelService riskLevelService;

    /**
     * 风险级别新增
     *
     * @param params 风险级别新增参数
     */
    @PostMapping("/risk-level")
    public void save(@RequestBody List<RiskLevelCreateParam> params) {
        this.riskLevelService.save(params);
    }

    /**
     * 风险级别编辑
     *
     * @param params 风险级别编辑参数
     */
    @PutMapping("/risk-level")
    public void update(@RequestBody List<RiskLevelUpdateParam> params) {
        this.riskLevelService.update(params);
    }

    /**
     * 风险级别查询
     *
     * @return 风险级别数据
     */
    @GetMapping("/risk-level")
    public List<RiskLevel> list() {
        return this.riskLevelService.listRiskLevels();
    }

    /**
     * 根据数值查询风险级别
     *
     * @param value 风险数值
     *
     * @return 风险级别信息
     */
    @GetMapping("/risk-level/value")
    public RiskLevelDTO getByValue(@RequestParam("value") Integer value) {
        List<RiskLevel> riskLevels = this.riskLevelService.list();
        RiskLevel riskLevel = this.riskLevelService.getByValue(value, riskLevels);
        return ObjectUtils.copying(riskLevel, RiskLevelDTO.class);
    }
}
