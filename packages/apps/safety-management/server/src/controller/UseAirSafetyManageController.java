package com.guhecloud.cube.srv.gen.safety.management.controller;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.UseAirSafetyStatisticsCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.UseAirSafetyStatisticsMarketCardVO;
import com.guhecloud.cube.srv.gen.safety.management.service.UseAirSafetyManageService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用气安全管理
 *
 * @author wql
 * @describe 用气安全管理
 * @date 2023/9/8 14:34
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UseAirSafetyManageController {
    private final UseAirSafetyManageService useAirSafetyManageService;

    /**
     * 用气安全统计卡片
     *
     * @Author wql
     * @Description 用气安全统计卡片
     * @Date 19:04 2023/9/7
     **/
    @GetMapping("/useAir/statisticalCard")
    public UseAirSafetyStatisticsCardVO getStatisticalCard(String applicationCode) {
        Assert.isParameterError(StringUtils.isBlank(applicationCode), "应用code参数不能为空");
        return useAirSafetyManageService.getStatisticalCard(applicationCode);
    }

    /**
     * 用气安全市场统计卡片
     *
     * @Author wql
     * @Description 用气安全市场统计卡片
     * @Date 14:46 2023/9/8
     **/
    @GetMapping("/useAir/statisticalMarketCard")
    public UseAirSafetyStatisticsMarketCardVO getStatisticalMarketCard(BaseQuery baseQuery) {
        return useAirSafetyManageService.getStatisticalMarketCard(baseQuery);
    }

}
