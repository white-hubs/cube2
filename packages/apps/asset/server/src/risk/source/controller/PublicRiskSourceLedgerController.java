package com.guhecloud.cube.srv.gen.property.management.server.risk.source.controller;

import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 风险源台账公共接口
 *
 * @author lanchun
 * @date 2025/1/3 14:39
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicRiskSourceLedgerController {

    private final RiskSourceLedgerController riskSourceLedgerController;

    /**
     * 查询风险源台账详情
     *
     * @param id 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/public/risk/source/ledger/queryById/{id}")
    public RiskSourceLedgerVO publicQueryById(@PathVariable Long id) {
        return riskSourceLedgerController.queryById(id);
    }

    /**
     * 分页查询风险源台账
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/public/risk/source/ledger/page")
    public Pagination<RiskSourceLedgerVO> publicQueryByPage(RiskSourceLedgerPageParam param) {
        return riskSourceLedgerController.queryByPage(param);
    }
}
