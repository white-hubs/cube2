package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerNewService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerHandleVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerNumStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerTypeStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 隐患统计
 *
 * @author wql
 * @describe 隐患统计
 * @date 2024/1/24 10:35
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerStatisticsController {

    private final HiddenDangerNewService hiddenDangerNewService;

    /**
     * 隐患数量统计
     *
     * @param period 时间范围
     *
     * @return 统计结果
     */
    @GetMapping("hidden/num/statistics")
    public HiddenDangerNumStatisticsVO hiddenDangerNumStatistics(Period period) {
        Assert.isNotFound(period, "统计时间范围不允许为空!");
        return hiddenDangerNewService.hiddenDangerNumStatistics(period);
    }

    /**
     * 隐患分类统计
     *
     * @param period 时间范围
     *
     * @return 统计结果
     */
    @GetMapping("hidden/type/statistics")
    public HiddenDangerTypeStatisticsVO hiddenDangerTypeStatistics(Period period) {
        Assert.isNotFound(period, "统计时间范围不允许为空!");
        return hiddenDangerNewService.hiddenDangerTypeStatistics(period);
    }

    /**
     * 隐患处理率
     *
     * @param period 时间范围
     *
     * @return 隐患处理率
     */
    @GetMapping("hidden/handle/ratio")
    public Float hiddenDangerHandleRatio(Period period) {
        return hiddenDangerNewService.hiddenDangerHandleRatio(period);
    }

    /**
     * 隐患处理统计,环比，同比
     *
     * @param period 时间范围
     *
     * @return 隐患处理率
     */
    @GetMapping("hidden/handle/report")
    public HiddenDangerHandleVO hiddenDangerForSafetyReport(@RequestParam("period") Period period) {
        return hiddenDangerNewService.hiddenDangerForSafetyReport(period);
    }

}
