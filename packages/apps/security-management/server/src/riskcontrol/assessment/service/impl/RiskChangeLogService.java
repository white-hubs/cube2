package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.dao.RiskChangeLogMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.entity.RiskChangeLogEntity;
import org.springframework.stereotype.Service;

@Service("securityManagementRiskChangeLogService")
public class RiskChangeLogService extends ServiceImpl<RiskChangeLogMapper, RiskChangeLogEntity> {
}
