package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.TimeControlLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.TimeControlLogQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo.TimeControlLogVo;
import com.guhecloud.platform.base.model.Pagination;

public interface TimeControlLogService extends IService<TimeControlLog> {
    Pagination<TimeControlLogVo> getTimeControlLogs(TimeControlLogQueryParam param);

    Pagination<TimeControlLogVo> getTimeControlPlan(TimeControlLogQueryParam param);
}
