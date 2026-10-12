package com.guhecloud.cube.biz.collection.analysis.gm.controller;

import com.guhecloud.cube.biz.collection.analysis.repository.service.SceneScheduleJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 全局操作控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GmGlobalOperateController {

    private final SceneScheduleJobService sceneScheduleJobService;

    /**
     * 刷新三方数据
     *
     * @return 是否成功
     */
    @PostMapping("/gm/global/refresh-third-data")
    public boolean refreshThirdData() {
        LocalDateTime time = LocalDateTime.now();
        return sceneScheduleJobService.updateNextTriggerTime(time, Arrays.asList("GM_DAY_COLLECTION", "GM_WEEK_COLLECTION"));
    }

}
