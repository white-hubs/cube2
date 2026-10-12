package com.guhecloud.cube.property.management.client.property.client.property;

import com.guhecloud.cube.property.management.client.property.param.property.PropertyHistoryPageParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyHistoryListVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 资产使用历史记录
 *
 * @author : lijie
 * @date : 2023-3-8
 */
@FeignClient(name = "propertyManagement")
public interface PropertyHistoryClient {

    /**
     * 查询资产履历
     *
     * @param propertyHistory 筛选条件
     *
     * @return 资产履历
     */
    @GetMapping("/property/history/getPropertyHistoryList")
    Pagination<PropertyHistoryListVO> getPropertyHistoryList(@SpringQueryMap @Valid PropertyHistoryPageParam propertyHistory);
}
