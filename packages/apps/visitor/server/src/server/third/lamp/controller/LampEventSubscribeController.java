package com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.controller;

import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.LampReservationPassService;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.EventLampCallbackParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LampEventSubscribeController {

    private final LampReservationPassService lampReservationPassService;

    /**
     * 预约状态变更回调接口
     * 注意这个接口的结构不能改(路径，请求方式，入参结构)，最多只能改内部实现逻辑
     * 因为这是个第三方回调接口，要跟第三方对应。
     * 如果实在要改请参考 LampEventSubscribeServiceImpl 类的订阅和第三方文档
     * <a href="https://open-icc.dahuatech.com/#/home?url=%3Fnav%3Dwiki%2Fevo-visitor%2Fevent.html&version=enterprisebase/5.0.15&blank=true">...</a>
     * （href里面的路径）
     *
     * @param param 入参
     */
    @PostMapping("/public/visitor/visitorBusinessEventCallback")
    public void visitorBusinessEventCallback(@RequestBody EventLampCallbackParam param) {
        log.info("第三方系统大华回调成功，入参：{}", param);
        lampReservationPassService.visitorBusinessEventCallback(param);
    }
}
