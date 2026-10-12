package com.guhecloud.kaihe.scada.server.controller;

import com.guhecloud.kaihe.scada.server.infrastructure.web.response.Result;
import com.guhecloud.kaihe.scada.server.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: gh-scada
 * @description: 项目配置
 * @author: hyq
 * @create: 2021-01-14 11:53
 **/
@Slf4j
@RequestMapping("/sysConfig")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysConfigController {

    private final SysConfigService sysConfigService;

    @GetMapping("/GetConfigValue")
    public Result getConfigValue(@RequestParam("configKey") String configKey) {
        String configValue = sysConfigService.getConfigValueByConfigKey(configKey);
        // result
        Result result = Result.builder().success(true).build();
        result.prop("configValue", configValue);
        return result;
    }

}
