package com.guhecloud.cube.srv.gen.product.market.server.pricing.controller;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskDetailParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskDetailQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingTaskDetailService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingTaskDetailVO;
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
 * @Description 采价任务详情
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingTaskDetailController {
    private final GoodsPricingTaskDetailService goodsPricingTaskDetailService;

    /**
     * 功能描述： 采价任务详情添加
     */
    @PostMapping(value = "/goods-pricing-task-detail")
    public Long add(@Valid @RequestBody GoodsPricingTaskDetailParam param) {
        return goodsPricingTaskDetailService.add(param);
    }

    /**
     * 功能描述： 采价任务详情修改
     */
    @PutMapping(value = "/goods-pricing-task-detail/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody GoodsPricingTaskDetailParam param) {
        goodsPricingTaskDetailService.update(id, param);
    }

    /**
     * 功能描述： 采价任务详情删除
     */
    @DeleteMapping(value = "/goods-pricing-task-detail/{id}")
    public void delete(@PathVariable Long id) {
        goodsPricingTaskDetailService.delete(id);
    }

    /**
     * 功能描述： 采价任务详情批量删除
     */
    @DeleteMapping(value = "/goods-pricing-task-detail/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        goodsPricingTaskDetailService.batchDelete(ids);
    }

    /**
     * 功能描述： 采价任务详情分页
     */
    @GetMapping(value = "/goods-pricing-task-detail/page")
    public Pagination<GoodsPricingTaskDetailVO> getPage(GoodsPricingTaskDetailQueryParam param) {
        return goodsPricingTaskDetailService.getPage(param);
    }

    /**
     * 功能描述： 采价任务详情详情
     */
    @GetMapping(value = "/goods-pricing-task-detail/{id}")
    public GoodsPricingTaskDetailVO getById(@PathVariable Long id) {
        return goodsPricingTaskDetailService.getById(id);
    }

}