package com.guhecloud.cube.srv.prepaid.server.controller;

import com.guhecloud.cube.srv.prepaid.server.param.UseDayStatParam;
import com.guhecloud.cube.srv.prepaid.server.param.WaterUseDayPageParam;
import com.guhecloud.cube.srv.prepaid.server.result.UseDayStatResult;
import com.guhecloud.cube.srv.prepaid.server.result.WaterUseDayPageResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 用水查询
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WaterUseDayController {

    /**
     * 日用水分页列表
     *
     * @param param 条件入参对象
     *
     * @return 分页列表
     */
    @GetMapping("/waterUserDay/page")
    public Pagination<WaterUseDayPageResult> getWaterUserDayPage(WaterUseDayPageParam param) {
        return Pagination.empty();
    }

    /**
     * 获取水表用能统计
     *
     * @param param 条件(时间段)
     *
     * @return 用能统计集合
     */
    @GetMapping("/waterUserDay/stat")
    public List<UseDayStatResult> getWaterUseDayStat(UseDayStatParam param) {
        return Collections.emptyList();
    }
}
