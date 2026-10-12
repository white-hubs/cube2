package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dto.QueryDTO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.PageVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.PeriodQuery;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HazardStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.HiddenDangerQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 隐患单接口
 *
 * @author lzy
 * @date 2022/4/26
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerController {

    private final HiddenDangerQueryService hiddenDangerQueryService;

    /**
     * 设备隐患统计分页
     */
    @GetMapping("/getHiddenDangerCount/page")
    public Page<PageVO> getHiddenDangerCount(QueryDTO query) {
        return hiddenDangerQueryService.getHiddenDangerCount(query);
    }

    /**
     * 设备隐患统计分页
     */
    @GetMapping("/getHazardStatistics")
    public HazardStatisticsVO getHazardStatistics(PeriodQuery query) {
        return hiddenDangerQueryService.getHazardStatistics(query);
    }

}
