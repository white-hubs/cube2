package com.guhecloud.cube.srv.gen.maintain.client;

import com.guhecloud.cube.srv.gen.maintain.param.MaintainTaskTotalParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 维保
 */
@FeignClient("maintain")
public interface MaintainStatisticsClient {

    /**
     * 获取任务总数
     *
     * @param param 条件
     *
     * @return 总数
     */
    @PostMapping("/maintain/task-total/post")
    Long maintainTaskTotalStat(@RequestBody MaintainTaskTotalParam param);
}
