package com.guhecloud.cube.srv.gen.property.management.server.consumable.controller;

import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableStockDetailQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableStockDetailService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStockDetailExtendVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStockDetailVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 出入库记录明细
 *
 * @author Appet
 * @Description 出入库记录明细
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableStockDetailController {
    private final ConsumableStockDetailService consumableStockDetailService;

    /**
     * 功能描述： 出入库记录明细分页
     */
    @GetMapping(value = "/consumable-stock-detail/page")
    public Pagination<ConsumableStockDetailVO> getPage(ConsumableStockDetailQueryParam param) {
        return consumableStockDetailService.getPage(param);
    }

    /**
     * 功能描述： 出入库记录明细详情
     */
    @GetMapping(value = "/consumable-stock-detail/{id}")
    public ConsumableStockDetailVO getById(@PathVariable Long id) {
        return consumableStockDetailService.getById(id);
    }

    /**
     * 出入库明细记录
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/consumable-stock-detail/record/page")
    public Pagination<ConsumableStockDetailExtendVO> getStockRecordDetail(ConsumableStockDetailQueryParam param) {
        return consumableStockDetailService.getStockRecordDetail(param);
    }

    @GetMapping(value = "/export/consumable-stock-detail/record")
    public void exportStockRecordDetail(ConsumableStockDetailQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        consumableStockDetailService.exportStockRecordDetail(param, request, response);
    }

}