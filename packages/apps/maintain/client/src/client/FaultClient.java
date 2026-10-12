package com.guhecloud.cube.srv.gen.maintain.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.vo.DevcieCountVo;
import com.guhecloud.cube.srv.gen.maintain.vo.FaultCompletedMetricVO;
import com.guhecloud.cube.srv.gen.maintain.vo.PageVO;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(ServiceRoute.TAG)
public interface FaultClient {
    /**
     * 设备任务是否有效
     *
     * @param deviceId
     * @param period
     *
     * @return
     */
    @GetMapping("/isFaultValid")
    Integer isFaultValid(@RequestParam("deviceId") String deviceId, @RequestParam("period") Period period);

    /**
     * 故障趋势和案例
     *
     * @param deviceId
     *
     * @return
     */
    @GetMapping("/getFaultHealthy")
    Map<String, Object> getFaultHealthy(@RequestParam("deviceId") String deviceId);

    /**
     * 设备台账接口
     *
     * @param ids
     *
     * @return
     */
    @GetMapping("/getDeviceCount")
    List<DevcieCountVo> getDeviceCount(@RequestParam("ids") List<Long> ids);

    /**
     * 设备统计分页
     */
    @GetMapping("/getFaultCount/page")
    Page<PageVO> getFaultCountPage(@SpringQueryMap Paging query);

    /**
     * 故障保修指标
     */
    @GetMapping("/fault/completed/metric")
    FaultCompletedMetricVO faultCompletedMetic(@RequestParam("period") Period period,
                                               @RequestParam(value = "type", required = false) String type
                                              );

    /**
     * 按网格区域统计未完成故障工单数
     */
    @PostMapping("/fault/order/unfinished/count/by/areaIds")
    Map<Long, Long> countUnfinishedByAreaIds(@RequestBody List<Long> areaIds);

    /**
     * 按网格区域统计故障工单数
     */
    @PostMapping("/fault/order/count/by/areaIds")
    Map<Long, Long> countByAreaIds(@RequestBody List<Long> areaIds);
}
