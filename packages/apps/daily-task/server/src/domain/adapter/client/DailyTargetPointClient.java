package com.guhecloud.cube.biz.daily.task.domain.adapter.client;

import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.ScenePointVO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.InspectScenePointQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 现场点client
 */
@FeignClient(name = "maintain")
public interface DailyTargetPointClient {
    /**
     * 批量获取现场点
     *
     * @param param 查询参数
     *
     * @return 现场点列表
     */
    @PostMapping("/inspect/point/batch")
    List<ScenePointVO> scenePointListBatch(@RequestBody InspectScenePointQueryParam param);
}
