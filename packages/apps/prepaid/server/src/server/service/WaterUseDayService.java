package com.guhecloud.cube.srv.prepaid.server.service;

import com.guhecloud.cube.srv.prepaid.server.param.UseDayStatParam;
import com.guhecloud.cube.srv.prepaid.server.param.WaterUseDayPageParam;
import com.guhecloud.cube.srv.prepaid.server.result.UseDayStatResult;
import com.guhecloud.cube.srv.prepaid.server.result.WaterUseDayPageResult;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 日用水
 */
public interface WaterUseDayService {
    Pagination<WaterUseDayPageResult> getWaterUserDayPage(WaterUseDayPageParam param);

    List<UseDayStatResult> getWaterUseDayStat(UseDayStatParam param);
}
