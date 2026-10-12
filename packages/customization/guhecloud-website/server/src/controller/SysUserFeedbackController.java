package com.guhecloud.cube.customized.guhecloudwebsite.controller;

import com.guhecloud.cube.customized.guhecloudwebsite.param.SysUserFeedbackParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysUserFeedbackQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysUserFeedbackService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysUserFeedbackVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户留言Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/website/feedback")
@Validated
public class SysUserFeedbackController {

    private final SysUserFeedbackService sysUserFeedbackService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Pagination<SysUserFeedbackVO> page(SysUserFeedbackQueryParam param) {
        return sysUserFeedbackService.page(param);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    public SysUserFeedbackVO detail(@PathVariable Long id) {
        return sysUserFeedbackService.detail(id);
    }

    /**
     * 新增
     */
    @PostMapping
    public void add(@Validated @RequestBody SysUserFeedbackParam param) {
        sysUserFeedbackService.add(param);
    }
    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sysUserFeedbackService.delete(id);
    }

}
