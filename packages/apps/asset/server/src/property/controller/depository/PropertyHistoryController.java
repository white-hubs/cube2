package com.guhecloud.cube.srv.gen.property.management.server.property.controller.depository;

import com.guhecloud.cube.property.management.client.property.client.property.PropertyHistoryClient;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyHistoryPageParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyHistoryListVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyHistoryService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;

/**
 * 资产使用历史记录
 *
 * @author : lijie
 * @date : 2023-3-8
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyHistoryController implements PropertyHistoryClient {
    private final PropertyHistoryService propertyHistoryService;

    /**
     * 查询资产履历
     *
     * @param propertyHistory 筛选条件
     *
     * @return 资产履历
     */
    @GetMapping("/property/history/getPropertyHistoryList")
    public Pagination<PropertyHistoryListVO> getPropertyHistoryList(@Valid PropertyHistoryPageParam propertyHistory) {
        return propertyHistoryService.getPropertyHistoryList(propertyHistory);
    }

    /**
     * 资产履历导出
     *
     * @param propertyHistory 筛选条件
     *
     * @return 资产履历getExcel
     */
    @GetMapping("/property/history/getExcel")
    public void getPropertyHistorygetExcel(@Valid PropertyHistoryPageParam propertyHistory, HttpServletRequest request,
                                           HttpServletResponse response
                                          ) {
        propertyHistoryService.getPropertyHistorygetExcel(propertyHistory, request, response);
    }
}
