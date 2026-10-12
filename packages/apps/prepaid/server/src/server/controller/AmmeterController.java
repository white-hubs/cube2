package com.guhecloud.cube.srv.prepaid.server.controller;

import com.guhecloud.cube.srv.prepaid.server.param.AmmeterPageParam;
import com.guhecloud.cube.srv.prepaid.server.result.AmmeterPageResult;
import com.guhecloud.cube.srv.prepaid.server.result.UnConnectAmmeterStatResult;
import com.guhecloud.cube.srv.prepaid.server.service.AmmeterService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 电表控制层
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AmmeterController {

    private final AmmeterService ammeterService;

    /**
     * 电表分页列表
     *
     * @param param 条件入参
     *
     * @return 分页对象
     */
    @GetMapping("/ammeter/page")
    public Pagination<AmmeterPageResult> getAmmeterPage(AmmeterPageParam param) {
        return ammeterService.getAmmeterPage(param);
    }

    /**
     * 获取电表编号下拉列表
     *
     * @return 电表编号列表
     */
    @GetMapping("/ammeter/idList")
    public List<String> getAmmeterIdList() {
        return ammeterService.getAmmeterIdList();
    }

    /**
     * 获取电表状态统计
     *
     * @return 统计对象
     */
    @GetMapping("/ammeter/stat/status")
    public UnConnectAmmeterStatResult getUnConnectAmmeterStat() {
        return ammeterService.getUnConnectAmmeterStat();
    }
}
