package com.guhecloud.cube.biz.daily.task.domain.adapter.client;

import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.BaseTargetInfoResponse;
import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.TagBusinessResponse;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.TagRefListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/8 16:49
 * @Description:
 */

@FeignClient(name = "tag")
public interface DailyTargetTagClient {
    @PostMapping("/business-tag/ref/data")
    List<BaseTargetInfoResponse> tagRefData(@RequestBody TagRefListRequest tagRefListRequest);

    /**
     * 获取标签信息
     */
    @PostMapping("/business-tag/ref/list")
    List<TagBusinessResponse> tagRefList(@RequestBody TagRefListRequest tagRefListRequest);

}
