package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.Standard;

import java.util.List;
import java.util.Map;

public interface IDetectionStandardService extends IService<Standard> {
    /**
     * 查询依据下的标准
     */
    List<Standard> listByBasicId(long basicId);

    /**
     * 批量查询依据下的标准
     */
    Map<Long, List<Standard>> getMapByBasicIds(List<Long> basicIds);
}