package com.guhecloud.cube.srv.prepaid.server.controller;

import com.guhecloud.cube.srv.prepaid.server.param.PowerUseDayPageParam;
import com.guhecloud.cube.srv.prepaid.server.param.UseDayStatParam;
import com.guhecloud.cube.srv.prepaid.server.result.PowerUseDayPageResult;
import com.guhecloud.cube.srv.prepaid.server.result.UseDayStatResult;
import com.guhecloud.cube.srv.prepaid.server.service.PowerUseDayService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用电查询
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PowerUseDayController {

    private final PowerUseDayService powerUseDayService;

    /**
     * 日用电分页查询
     *
     * @param param 条件入参
     *
     * @return 分页
     */
    @GetMapping("/powerUserDay/page")
    public Pagination<PowerUseDayPageResult> getPowerUserDayPage(PowerUseDayPageParam param) {
        return powerUseDayService.getPowerUserDayPage(param);
    }

    /**
     * 获取电表日用能统计
     *
     * @param param 条件
     *
     * @return 列表
     */
    @GetMapping("/powerUserDay/stat")
    public List<UseDayStatResult> getPowerUseDayStat(UseDayStatParam param) {
        return powerUseDayService.getPowerUseDayStat(param);
    }
}
