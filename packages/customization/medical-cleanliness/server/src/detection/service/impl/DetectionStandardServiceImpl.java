package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.Standard;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.repo.mapper.DetectionStandardMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionStandardService;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class DetectionStandardServiceImpl extends ServiceImpl<DetectionStandardMapper, Standard> implements IDetectionStandardService {

    @Override
    public List<Standard> listByBasicId(long basicId) {
        return lambdaQuery().eq(Standard::getBasicId, basicId).eq(Standard::getDeleted, BigInteger.ZERO).list();
    }

    @Override
    public Map<Long, List<Standard>> getMapByBasicIds(List<Long> basicIds) {
        if (ListUtils.isEmpty(basicIds)) {
            return Collections.emptyMap();
        }
        List<Standard> list = lambdaQuery()
                .in(Standard::getBasicId, basicIds)
                .eq(Standard::getDeleted, BigInteger.ZERO)
                .list();
        return MapUtils.listGroupToMap(list, Standard::getBasicId);
    }
}