package com.guhecloud.cube.srv.prepaid.server.service;

import com.guhecloud.cube.srv.prepaid.server.param.CollectRecordsPageParam;
import com.guhecloud.cube.srv.prepaid.server.result.CollectRecordsResult;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 抄表记录
 */
public interface CollectRecordsService {
    Pagination<CollectRecordsResult> getCollectRecordsPage(CollectRecordsPageParam param);
}
