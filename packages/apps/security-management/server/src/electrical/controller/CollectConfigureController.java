package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.CollectConfigureParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.CollectConfigureService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.CollectConfigureVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @Description 电气安全采集配置
 * @Author lrj
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectConfigureController {
    private final CollectConfigureService collectConfigureService;

    /**
     * @Description 新增采集配置
     * @Author lrj
     */
    @PostMapping("/addCollectConfigure")
    public void addCollectConfigure(@RequestBody @Valid CollectConfigureParam param) {
        collectConfigureService.addCollectConfigure(param);
    }

    /**
     * @Description 采集配置
     * @Author lrj
     */
    @GetMapping("/getCollectConfigure/{applicationCode}")
    public CollectConfigureVo getCollectConfigure(@PathVariable String applicationCode) {
        return collectConfigureService.getCollectConfigure(applicationCode);
    }

}
