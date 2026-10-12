package com.guhecloud.cube.srv.gen.maintain.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.dto.HiddenDangerCommandDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HiddenWorkOrderStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.vo.HiddenDangerHandleVO;
import com.guhecloud.cube.srv.gen.maintain.vo.HiddenDangerNumStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.vo.HiddendangerWordOrderStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.vo.PageVO;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("maintain")
public interface HiddenDangerClient {
    //设备任务是否有效
    @GetMapping("/isHiddenDangerValid")
    Integer isHiddenDangerValid(@RequestParam("deviceId") String deviceId, @RequestParam("period") Period period);

    /**
     * 设备隐患统计分页
     */
    @GetMapping("/getHiddenDangerCount/page")
    Page<PageVO> getHiddenDangerCount(@SpringQueryMap Paging query);

    /**
     * @Description 隐患提交
     * @Author lrj
     */
    @PostMapping("/hidden-danger")
    Long save(@Valid @RequestBody HiddenDangerCommandDTO command);

    @GetMapping("hidden/handle/report")
    HiddenDangerHandleVO hiddenDangerForSafetyReport(@RequestParam("period") Period period);

    /**
     * 隐患指标
     */
    @GetMapping("hidden/num/statistics")
    HiddenDangerNumStatisticsVO hiddenDangerNumStatistics(@RequestParam("period") Period period);

    /**
     * 隐患单工单统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/hidden/work/order/statistics/all")
    List<HiddendangerWordOrderStatisticsVO> hiddendangerWorkOrderStatisticsAll(@SpringQueryMap HiddenWorkOrderStatisticsDTO param);
}
