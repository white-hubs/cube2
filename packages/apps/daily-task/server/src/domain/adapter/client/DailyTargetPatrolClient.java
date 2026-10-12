package com.guhecloud.cube.biz.daily.task.domain.adapter.client;

import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.PatrolLocationVO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.PatrolLocationPageParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("maintain")
public interface DailyTargetPatrolClient {
    /**
     * 批量获取巡更点详情
     */
    @PostMapping("/patrol/location/batch")
    List<PatrolLocationVO> queryLocationBatch(@RequestBody PatrolLocationPageParam param);
}
