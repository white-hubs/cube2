package com.guhecloud.cube.customized.youxianqu.park.management.server.estate.controller;

import com.guhecloud.cube.customized.youxianqu.park.management.server.common.vo.result.TrendResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.estate.param.EstateContractQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.estate.result.EstateContractResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.estate.service.EstateContractService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 物业合同接口
 *
 * @author lanchun
 * @date 2024/8/28 15:23
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EstateContractController {
    private final EstateContractService estateContractService;

    /**
     * 物业合同分页
     *
     * @param param 条件
     *
     * @return 分页
     */
    @GetMapping("/estateContract/page")
    public Pagination<EstateContractResult> getPage(EstateContractQueryParam param) {
        return estateContractService.getPage(param);
    }

    /**
     * 获取租赁趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @GetMapping("/estateContract/stat/trend")
    public List<TrendResult> getTrend(EstateContractQueryParam param) {
        return estateContractService.getTrend(param);
    }
}
