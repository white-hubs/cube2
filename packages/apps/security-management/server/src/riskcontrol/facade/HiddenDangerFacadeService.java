package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Service("securityManagementRiskHiddenDangerFacadeService")
public class HiddenDangerFacadeService {
    public Map<Long, Long> countActiveByUnitIds(Collection<Long> unitIds) {
        return Collections.emptyMap();
    }
}
