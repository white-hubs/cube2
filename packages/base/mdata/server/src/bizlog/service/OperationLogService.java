package com.guhecloud.cube.biz.mdata.server.bizlog.service;

import com.guhecloud.cube.biz.mdata.server.bizlog.param.OperationLogParam;
import com.guhecloud.cube.biz.mdata.server.bizlog.vo.OperationLogVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @Author: lijie
 * @CreateTime: 2024-11-28  15:04
 * @Description:
 * @Version: 1.0
 */
public interface OperationLogService {
    Pagination<OperationLogVO> queryLogRecordPage(OperationLogParam param);

}
