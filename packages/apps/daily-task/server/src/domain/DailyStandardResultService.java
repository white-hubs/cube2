package com.guhecloud.cube.biz.daily.task.domain;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailyStandardResultDTO;

import java.util.Map;
import java.util.Set;

public interface DailyStandardResultService {
    Map<Long, DailyStandardResultDTO> handlerStandardContent(Set<Long> standardIdSet);
}
