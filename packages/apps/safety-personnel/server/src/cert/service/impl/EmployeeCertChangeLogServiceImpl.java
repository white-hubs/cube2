package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.EmployeeCertChangeLog;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.mapper.EmployeeCertChangeLogMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.EmployeeCertChangeLogService;
import com.guhecloud.platform.security.model.Principal;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 人员证书变更记录服务实现。
 */
@Service("safetyPersonnelEmployeeCertChangeLogService")
public class EmployeeCertChangeLogServiceImpl extends ServiceImpl<EmployeeCertChangeLogMapper, EmployeeCertChangeLog>
        implements EmployeeCertChangeLogService {

    @Override
    public void log(Long certId, String changeType, String changeContent) {
        EmployeeCertChangeLog log = new EmployeeCertChangeLog();
        log.setId(IdWorker.getId());
        log.setCertId(certId);
        log.setChangeType(changeType);
        log.setChangeContent(changeContent);
        Principal principal = SecurityContextHolder.getPrincipal(true);
        log.setOperatorId(principal == null ? null : principal.getId());
        log.setOperatorName(principal == null ? null : principal.getName());
        log.setChangeTime(LocalDateTime.now());
        save(log);
    }
}
