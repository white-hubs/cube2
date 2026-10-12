package com.guhecloud.cube.biz.work.center.server.service;

import com.guhecloud.cube.biz.work.center.server.param.WorkHomePageBatchParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkHomePageParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTaskQueryParam;
import com.guhecloud.cube.biz.work.center.server.vo.WorkHomePageVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskPageVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/4 11:59
 * @Description:
 */
public interface WorkCenterService {

    /**
     * 获取工作中心首页统计信息
     *
     * @param param
     *
     * @return
     */
    WorkHomePageVO getHomePageNum(WorkHomePageParam param);

    Map<Long, WorkHomePageVO> getHomePageNumBatch(WorkHomePageBatchParam param);

    /**
     * 获取工作中心任务列表
     *
     * @param param
     *
     * @return
     */
    Pagination<WorkTaskPageVO> taskQuery(WorkTaskQueryParam param);

    List<Long> getHandTaskUser(Set<Long> userIds);
}
