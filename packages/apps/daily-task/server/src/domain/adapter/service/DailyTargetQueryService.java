package com.guhecloud.cube.biz.daily.task.domain.adapter.service;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailyTargetDTO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.DailyTargetQueryParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DailyTargetQueryService {
    Map<Long, DailyTargetDTO> postDailyTargetList(DailyTargetQueryParam param);

    List<DailyTargetDTO> postStallMasterOthers(Long targetId, Set<Long> otherIdSet);
}
