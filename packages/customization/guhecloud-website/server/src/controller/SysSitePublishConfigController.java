package com.guhecloud.cube.customized.guhecloudwebsite.controller;

import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSitePublishConfigParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysSitePublishConfigService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSitePublishConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 网站发布配置控制器
 */
@RestController
@RequestMapping("/website/publish-config")
public class SysSitePublishConfigController {

    @Autowired
    private SysSitePublishConfigService publishConfigService;

    /**
     * 查询发布配置详情
     * 不传任何参数，查询第一条记录
     */
    @GetMapping
    public SysSitePublishConfigVO detail() {
        return publishConfigService.getDetail();
    }

    /**
     * 保存或更新发布配置
     * 有一则更新，没有则新增
     * @param body 请求体，包含 provider 和 providerConfig 字段
     * @return 更新后的配置详情
     */
    @PutMapping
    public void saveOrUpdate(@RequestBody SysSitePublishConfigParam body) {
        publishConfigService.saveOrUpdate(body);
    }

}
