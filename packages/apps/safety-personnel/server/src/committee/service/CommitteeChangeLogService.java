package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.CommitteeChangeLog;

/**
 * 安委会变更记录服务。
 */
public interface CommitteeChangeLogService extends IService<CommitteeChangeLog> {

    /**
     * 记录安委会变更。
     *
     * @param committeeId   安委会ID
     * @param changeType    变更类型
     * @param changeContent 变更内容JSON
     */
    void log(Long committeeId, String changeType, String changeContent);
}
