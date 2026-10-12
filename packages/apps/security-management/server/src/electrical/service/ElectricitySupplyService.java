package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service;

import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.ElectricitySupplyParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.ElectricitySupplyDetailVO;

/**
 * 供电
 */
public interface ElectricitySupplyService {
    Boolean save(ElectricitySupplyParam param);

    ElectricitySupplyDetailVO detail();
}
