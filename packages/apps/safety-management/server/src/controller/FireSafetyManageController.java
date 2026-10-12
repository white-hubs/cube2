package com.guhecloud.cube.srv.gen.safety.management.controller;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.FireSafetyStatisticsCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.FireSafetyStatisticsMarketCardVO;
import com.guhecloud.cube.srv.gen.safety.management.service.FireSafetyManageService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消防安全管理
 *
 * @author wql
 * @describe 消防安全管理
 * @date 2023/9/8 14:49
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FireSafetyManageController {
    private final FireSafetyManageService fireSafetyManageService;

    /**
     * 消防安全统计卡片
     *
     * @Author wql
     * @Description 消防安全统计卡片
     * @Date 15:06 2023/9/8
     **/
    @GetMapping("/fireSafety/statisticalCard")
    public FireSafetyStatisticsCardVO getStatisticalCard(String applicationCode) {
        Assert.isParameterError(StringUtils.isBlank(applicationCode), "应用code参数不能为空");
        return fireSafetyManageService.getStatisticalCard(applicationCode);
    }

    /**
     * 消防设备建档数和到期数
     *
     * @Author wql
     * @Description 消防安全市场统计卡片
     * @Date 15:07 2023/9/8
     **/
    @GetMapping("/fireSafety/statisticalMarketCard")
    public FireSafetyStatisticsMarketCardVO getStatisticalMarketCard(BaseQuery query) {
        return fireSafetyManageService.getStatisticalMarketCard(query);
    }

}
