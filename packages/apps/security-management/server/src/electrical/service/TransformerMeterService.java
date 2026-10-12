package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.TransformerMeter;

public interface TransformerMeterService extends IService<TransformerMeter> {
    TransformerMeter getLastOne(Long deviceId);
}
