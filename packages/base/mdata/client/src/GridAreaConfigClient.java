package com.guhecloud.cube.biz.mdata.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/14 16:47
 * @Description:
 */
@FeignClient(name = "mdata")
public interface GridAreaConfigClient {
    /**
     * 获取指定网格区域负责人
     */
    @PostMapping("/grid/area/grid-leader/list")
    Map<Long, Set<Long>> getGridAreaConfigWithGridLeaderList(@RequestBody List<Long> areaIdList);
}
