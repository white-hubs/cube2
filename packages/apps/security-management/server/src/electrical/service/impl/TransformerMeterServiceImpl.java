package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.dao.TransformerMeterMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.TransformerMeter;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.TransformerMeterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransformerMeterServiceImpl extends ServiceImpl<TransformerMeterMapper, TransformerMeter> implements TransformerMeterService {
    private final TransformerMeterMapper transformerMeterMapper;

    @Override
    public TransformerMeter getLastOne(Long deviceId) {
        return transformerMeterMapper.getLastOne(deviceId);

    }
}
