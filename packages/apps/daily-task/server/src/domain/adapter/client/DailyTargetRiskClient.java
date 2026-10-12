package com.guhecloud.cube.biz.daily.task.domain.adapter.client;

import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.RiskSourceLedgerVO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.RiskSourceLedgerBatchParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 风险源client
 */
@FeignClient(name = "propertyManagement")
public interface DailyTargetRiskClient {
    /**
     * 批量查询风险源台账详情
     *
     * @param param idList id集合
     *
     * @return 查询结果
     */
    @PostMapping("/risk/source/ledger/query/batch/post")
    List<RiskSourceLedgerVO> queryRiskSourceLedgerBatchPost(@RequestBody RiskSourceLedgerBatchParam param);
}
