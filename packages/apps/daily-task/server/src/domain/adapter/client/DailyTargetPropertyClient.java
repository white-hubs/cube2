package com.guhecloud.cube.biz.daily.task.domain.adapter.client;

import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.PropertyDetailedVO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.PropertyDetailedByIdListParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 资产client
 */
@FeignClient(name = "propertyManagement")
public interface DailyTargetPropertyClient {
    /**
     * 通过id批量查询
     *
     * @return 入库详情
     */
    @PostMapping("/property/detailed/batch/post")
    List<PropertyDetailedVO> queryPropertyDetailBatchPost(@RequestBody PropertyDetailedByIdListParam param);
}
