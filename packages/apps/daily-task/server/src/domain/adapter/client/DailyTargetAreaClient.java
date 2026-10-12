package com.guhecloud.cube.biz.daily.task.domain.adapter.client;

import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.AreaDTO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.AreaQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("mdata")
public interface DailyTargetAreaClient {
    /**
     * 获取区域列表
     */
    @PostMapping("/area-inner/list/post")
    List<AreaDTO> listInnerAreaPost(@RequestBody AreaQueryParam queryParam);
}
