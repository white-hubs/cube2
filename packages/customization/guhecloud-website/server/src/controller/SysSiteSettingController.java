package com.guhecloud.cube.customized.guhecloudwebsite.controller;

import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteSettingParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteSettingQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysSiteSettingService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSiteSettingVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网站设置Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/website/setting")
public class SysSiteSettingController {

    private final SysSiteSettingService siteSettingService;

    /**
     * 编辑
     */
    @PutMapping
    public void update(@RequestBody SysSiteSettingParam param) {
        siteSettingService.update(param);
    }

    /**
     * 详情
     */
    @GetMapping("/detail")
    public SysSiteSettingVO detail() {
        return siteSettingService.detail();
    }

}
