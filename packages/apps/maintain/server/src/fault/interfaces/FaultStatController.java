package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultCaseSummaryVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultCase;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.FaultCaseQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.FaultCaseCollectionQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author lzy
 * @date 2022/5/16
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultStatController {

    private final FaultCaseQueryService faultCaseQueryService;

    /**
     * 故障案例库信息
     *
     * @return 汇总信息
     */
    @GetMapping("/fault/stat/fault-case/summary")
    public FaultCaseSummaryVO getFaultCaseSummary() {
        // 获取案例信息
        List<FaultCase> faultCases = this.faultCaseQueryService.listFaultCase(new FaultCaseCollectionQuery());
        return FaultCaseSummaryVO.builder()
                .total(faultCases.size())
                .build();
    }

    /**
     * @Description 案例故障频次近十二月趋势
     * @Author lrj
     */
    @GetMapping("/get-fault-trend/{id}")
    public Map<String, Object> getFaultTrend(@PathVariable Long id) {
        return faultCaseQueryService.getFaultTrend(id);
    }

}
