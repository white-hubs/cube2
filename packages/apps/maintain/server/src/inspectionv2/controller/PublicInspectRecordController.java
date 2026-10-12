package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 巡检记录公共接口
 *
 * @author lanchun
 * @date 2025/1/3 14:57
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicInspectRecordController {

    private final InspectRecordController inspectRecordController;

    /**
     * 对象巡检次数统计
     *
     * @param param 统计参数
     *
     * @return 巡检次数
     */
    @GetMapping("/public/inspect-record/objInspectNum")
    public Long objInspectNumStatistics(DeviceStatisticParam param) {
        return inspectRecordController.objInspectNumStatistics(param);
    }
}

