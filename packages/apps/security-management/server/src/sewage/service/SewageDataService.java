package com.guhecloud.cube.srv.gen.securitymanagement.server.sewage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.sewage.entity.SewageData;

import java.util.Map;

public interface SewageDataService extends IService<SewageData> {

    Map<String, Object> getDataWaterTreatment();
}
