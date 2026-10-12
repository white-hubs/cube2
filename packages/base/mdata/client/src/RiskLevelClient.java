package com.guhecloud.cube.biz.mdata.client;

import com.guhecloud.cube.biz.mdata.client.dto.RiskLevelDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lzy
 * @date 2021/10/19
 */
@FeignClient(name = "mdata")
public interface RiskLevelClient {
    /**
     * 根据数值查询风险级别
     *
     * @param value 风险数值
     *
     * @return 风险级别信息
     */
    @GetMapping("/risk-level/value")
    RiskLevelDTO getByValue(@RequestParam("value") Integer value);

    /**
     * 根据主键集合查询风险级别
     *
     * @param ids 风险级别主键
     *
     * @return 风险级别信息
     */
    @GetMapping("/risk-level/ids")
    List<RiskLevelDTO> getByIds(@RequestParam("ids") List<Long> ids);
}
