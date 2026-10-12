package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.param.RuleTimingQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.service.RuleTimingService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.vo.RuleTimingPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleTimingController {

    private final RuleTimingService ruleTimingService;

    /**
     * 定时规则分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/rule-timings")
    public Pagination<RuleTimingPageVO> page(@Valid RuleTimingQueryParam param) {
        Page<RuleTimingPageVO> page = this.ruleTimingService.page(param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        return PagingUtils.convert(page);
    }
}
