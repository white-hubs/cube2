package com.guhecloud.cube.biz.daily.task.domain.adapter.client;

import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.ProjectScheduleVO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.ProjectScheduleQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 小散工程client
 */
@FeignClient(name = "looseWorks")
public interface DailyTargetSmallClient {
    /**
     * 批量获取获取工程监管信息
     *
     * @param param 条件
     *
     * @return 列表
     */
    @PostMapping("/project/schedule/post/list")
    List<ProjectScheduleVO> projectScheduleBatchPost(@RequestBody ProjectScheduleQueryParam param);
}
