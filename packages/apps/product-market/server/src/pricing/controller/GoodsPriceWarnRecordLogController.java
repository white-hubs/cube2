package com.guhecloud.cube.srv.gen.product.market.server.pricing.controller;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRecordLogParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRecordLogQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnRecordLogService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRecordLogVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Appet
 * @Description 采价预警-价格修改日志
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPriceWarnRecordLogController {
    private final GoodsPriceWarnRecordLogService goodsPriceWarnRecordLogService;

    /**
     * 功能描述： 采价预警-价格修改日志添加
     */
    @PostMapping(value = "/goods-price-warn-record-log")
    public Long add(@Valid @RequestBody GoodsPriceWarnRecordLogParam param) {
        return goodsPriceWarnRecordLogService.add(param);
    }

    /**
     * 功能描述： 采价预警-价格修改日志修改
     */
    @PutMapping(value = "/goods-price-warn-record-log/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody GoodsPriceWarnRecordLogParam param) {
        goodsPriceWarnRecordLogService.update(id, param);
    }

    /**
     * 功能描述： 采价预警-价格修改日志删除
     */
    @DeleteMapping(value = "/goods-price-warn-record-log/{id}")
    public void delete(@PathVariable Long id) {
        goodsPriceWarnRecordLogService.delete(id);
    }

    /**
     * 功能描述： 采价预警-价格修改日志批量删除
     */
    @DeleteMapping(value = "/goods-price-warn-record-log/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        goodsPriceWarnRecordLogService.batchDelete(ids);
    }

    /**
     * 功能描述： 采价预警-价格修改日志分页
     */
    @GetMapping(value = "/goods-price-warn-record-log/page")
    public Pagination<GoodsPriceWarnRecordLogVO> getPage(GoodsPriceWarnRecordLogQueryParam param) {
        return goodsPriceWarnRecordLogService.getPage(param);
    }

    /**
     * 功能描述： 采价预警-价格修改日志详情
     */
    @GetMapping(value = "/goods-price-warn-record-log/{id}")
    public GoodsPriceWarnRecordLogVO getById(@PathVariable Long id) {
        return goodsPriceWarnRecordLogService.getById(id);
    }

}