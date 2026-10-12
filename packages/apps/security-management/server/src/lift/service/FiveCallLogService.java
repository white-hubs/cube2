package com.guhecloud.cube.srv.gen.securitymanagement.server.lift.service;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.param.FiveCallLogParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.vo.FiveCallLogVo;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 五方通话记录服务
 */
public interface FiveCallLogService {
    /**
     * 五方通话记录分页列表
     *
     * @param param 条件入参
     *
     * @return 分页列表
     */
    Pagination<FiveCallLogVo> fiveCallLogPage(FiveCallLogParam param);
}
