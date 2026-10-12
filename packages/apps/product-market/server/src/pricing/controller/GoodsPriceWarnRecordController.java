package com.guhecloud.cube.srv.gen.product.market.server.pricing.controller;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRecordParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRecordQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnStatParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnRecordService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRecordVO;
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
 * @Description 采价预警记录
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPriceWarnRecordController {
    private final GoodsPriceWarnRecordService goodsPriceWarnRecordService;

    /**
     * 功能描述： 采价预警记录添加
     */
    @PostMapping(value = "/goods-price-warn-record")
    public Long add(@Valid @RequestBody GoodsPriceWarnRecordParam param) {
        return goodsPriceWarnRecordService.add(param);
    }

    /**
     * 功能描述： 采价预警记录修改
     */
    @PutMapping(value = "/goods-price-warn-record/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody GoodsPriceWarnRecordParam param) {
        goodsPriceWarnRecordService.update(id, param);
    }

    /**
     * 功能描述： 采价预警记录删除
     */
    @DeleteMapping(value = "/goods-price-warn-record/{id}")
    public void delete(@PathVariable Long id) {
        goodsPriceWarnRecordService.delete(id);
    }

    /**
     * 功能描述： 采价预警记录批量删除
     */
    @DeleteMapping(value = "/goods-price-warn-record/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        goodsPriceWarnRecordService.batchDelete(ids);
    }

    /**
     * 功能描述： 采价预警记录分页
     */
    @GetMapping(value = "/goods-price-warn-record/page")
    public Pagination<GoodsPriceWarnRecordVO> getPage(GoodsPriceWarnRecordQueryParam param) {
        return goodsPriceWarnRecordService.getPage(param);
    }

    /**
     * 功能描述： 采价预警记录详情
     */
    @GetMapping(value = "/goods-price-warn-record/{id}")
    public GoodsPriceWarnRecordVO getById(@PathVariable Long id) {
        return goodsPriceWarnRecordService.getById(id);
    }

    /**
     * 告警处理状态统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/goods-price-warn-record/status/stat")
    public List<CommonStatVO> goodsPriceWarnStatusStatByTime(GoodsPriceWarnStatParam param) {
        return goodsPriceWarnRecordService.goodsPriceWarnStatusStatByTime(param);
    }

}