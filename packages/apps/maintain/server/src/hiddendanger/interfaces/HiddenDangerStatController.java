package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.HiddenDangerQueryService;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lzy
 * @date 2022/5/16
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerStatController {

    private final HiddenDangerQueryService hiddenDangerQueryService;

    /**
     * @Description 健康分析
     * @Author lrj
     */
    @GetMapping("/isHiddenDangerValid")
    public Integer isHiddenDangerValid(@RequestParam("deviceId") String deviceId, @RequestParam("period") Period period) {
        return hiddenDangerQueryService.isHiddenDangerValid(deviceId, period);
    }

}
