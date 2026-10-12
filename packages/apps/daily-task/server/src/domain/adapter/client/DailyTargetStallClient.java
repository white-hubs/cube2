package com.guhecloud.cube.biz.daily.task.domain.adapter.client;

import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.StallDTO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.StallMasterOtherParam;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.StallPostQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 档位client
 */
@FeignClient(name = "mdata")
public interface DailyTargetStallClient {
    @PostMapping("/stall/list/post/ids")
    List<StallDTO> postStallListByIds(@RequestBody StallPostQueryParam param);

    /**
     * 获取某档位档主的其他档位
     * 注意不包含自己
     *
     * @param param 条件
     *
     * @return 其他档位列表
     */
    @PostMapping("/stall/stallMasterOthers")
    List<StallDTO> postStallMasterOthers(@RequestBody StallMasterOtherParam param);
}
