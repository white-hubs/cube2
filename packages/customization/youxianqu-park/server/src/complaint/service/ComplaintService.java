package com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.service;

import com.guhecloud.cube.customized.youxianqu.park.management.server.common.vo.result.TrendResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.param.ComplaintQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.result.ComplaintResult;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 投诉服务
 */
public interface ComplaintService {

    /**
     * 投诉分页列表
     *
     * @param param 条件
     * @return 分页
     */
    Pagination<ComplaintResult> getPage(ComplaintQueryParam param);

    /**
     * 获取投诉趋势
     *
     * @param param 条件
     * @return 趋势
     */
    List<TrendResult> getTrend(ComplaintQueryParam param);
}
