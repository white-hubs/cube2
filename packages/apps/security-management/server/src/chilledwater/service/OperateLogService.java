package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.OperateLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.OperateLogParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.OperateLogVo;
import com.guhecloud.platform.base.model.Pagination;

public interface OperateLogService extends IService<OperateLog> {
    void saveBusinessLog(String remark);

    Pagination<OperateLogVo> getOperateLogs(OperateLogParam operateLogParam);

    Pagination<OperateLogVo> getOperateHardwareLogs(OperateLogParam operateLogParam);
}
