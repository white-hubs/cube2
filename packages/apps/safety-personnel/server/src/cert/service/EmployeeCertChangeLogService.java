package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.EmployeeCertChangeLog;

/**
 * 人员证书变更记录服务。
 */
public interface EmployeeCertChangeLogService extends IService<EmployeeCertChangeLog> {

    /**
     * 记录证书变更日志。
     *
     * @param certId        人员证书ID
     * @param changeType    变更类型
     * @param changeContent 变更内容JSON
     */
    void log(Long certId, String changeType, String changeContent);
}
