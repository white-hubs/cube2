package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.CommitteeChangeLog;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.mapper.CommitteeChangeLogMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.service.CommitteeChangeLogService;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 安委会变更记录服务实现。
 */
@Service("safetyPersonnelCommitteeChangeLogService")
public class CommitteeChangeLogServiceImpl extends ServiceImpl<CommitteeChangeLogMapper, CommitteeChangeLog>
        implements CommitteeChangeLogService {

    @Override
    public void log(Long committeeId, String changeType, String changeContent) {
        Principal principal = SecurityContextHolder.getPrincipal(true);
        CommitteeChangeLog log = new CommitteeChangeLog();
        log.setId(IdWorker.getId());
        log.setCommitteeId(committeeId);
        log.setChangeType(changeType);
        log.setChangeContent(changeContent);
        log.setOperatorId(principal == null ? null : principal.getId());
        log.setOperatorName(principal == null ? null : principal.getName());
        log.setChangeTime(LocalDateTime.now());
        save(log);
    }
}
