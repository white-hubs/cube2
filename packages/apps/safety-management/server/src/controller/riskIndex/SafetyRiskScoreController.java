package com.guhecloud.cube.srv.gen.safety.management.controller.riskIndex;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.RiskScoreQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.RiskScoreVO;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyRiskScoreService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 分值区间
 *
 * @author wql
 * @describe 分值区间
 * @date 2023/10/12 19:56
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyRiskScoreController {
    private final SafetyRiskScoreService safetyRiskScoreService;

    /**
     * 保存分值区间
     *
     * @Author wql
     * @Description 保存分值区间
     * @Date 14:01 2023/10/13
     **/
    @PutMapping("/riskScore/save")
    public void saveRiskScore(@Valid @RequestBody RiskScoreVO riskScoreVO) {
        safetyRiskScoreService.saveRiskScore(riskScoreVO);
    }

    /**
     * 删除分值区间
     *
     * @Author wql
     * @Description 删除分值区间
     * @Date 9:29 2023/10/16
     **/
    @DeleteMapping("/riskScore/delete/{id}")
    public void deleteRiskScore(@PathVariable("id") Long id) {
        safetyRiskScoreService.deleteRiskScore(id);
    }

    /**
     * 分值区间列表
     *
     * @Author wql
     * @Description 分值区间列表
     * @Date 9:30 2023/10/16
     **/
    @GetMapping("/riskScore/list")
    public Pagination<RiskScoreVO> riskScoreList(RiskScoreQuery query) {
        return safetyRiskScoreService.riskScoreList(query);
    }

    /**
     * 分值区间详情
     *
     * @Author wql
     * @Description 分值区间详情
     * @Date 9:30 2023/10/16
     **/
    @GetMapping("/riskScore/{id}")
    public RiskScoreVO riskScoreDetail(@PathVariable("id") Long id) {
        return safetyRiskScoreService.riskScoreDetail(id);
    }

}
