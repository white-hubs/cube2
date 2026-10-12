package com.guhecloud.cube.srv.prepaid.server.service;

import com.guhecloud.cube.srv.prepaid.server.param.PowerUseDayPageParam;
import com.guhecloud.cube.srv.prepaid.server.param.UseDayStatParam;
import com.guhecloud.cube.srv.prepaid.server.result.PowerUseDayPageResult;
import com.guhecloud.cube.srv.prepaid.server.result.UseDayStatResult;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 日用电
 */
public interface PowerUseDayService {
    Pagination<PowerUseDayPageResult> getPowerUserDayPage(PowerUseDayPageParam param);

    List<UseDayStatResult> getPowerUseDayStat(UseDayStatParam param);
}
