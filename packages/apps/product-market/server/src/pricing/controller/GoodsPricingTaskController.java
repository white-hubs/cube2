package com.guhecloud.cube.srv.gen.product.market.server.pricing.controller;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskTransferParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingTaskService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingTaskVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.MyTaskStatVO;
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
 * @Description 采价任务
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingTaskController {
    private final GoodsPricingTaskService goodsPricingTaskService;

    /**
     * 功能描述： 采价任务添加
     */
    @PostMapping(value = "/goods-pricing-task")
    public Long add(@Valid @RequestBody GoodsPricingTaskParam param) {
        return goodsPricingTaskService.add(param);
    }

    /**
     * 功能描述： 采价任务修改
     */
    @PutMapping(value = "/goods-pricing-task/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody GoodsPricingTaskParam param) {
        goodsPricingTaskService.update(id, param);
    }

    /**
     * 功能描述： 采价任务删除
     */
    @DeleteMapping(value = "/goods-pricing-task/{id}")
    public void delete(@PathVariable Long id) {
        goodsPricingTaskService.delete(id);
    }

    /**
     * 功能描述： 采价任务批量删除
     */
    @DeleteMapping(value = "/goods-pricing-task/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        goodsPricingTaskService.batchDelete(ids);
    }

    /**
     * 功能描述： 采价任务分页
     */
    @GetMapping(value = "/goods-pricing-task/page")
    public Pagination<GoodsPricingTaskVO> getPage(GoodsPricingTaskQueryParam param) {
        return goodsPricingTaskService.getPage(param);
    }

    @GetMapping(value = "/app/goods-pricing-task/page")
    public Pagination<GoodsPricingTaskVO> getPageForApp(GoodsPricingTaskQueryParam param) {
        return goodsPricingTaskService.getPageForApp(param);
    }

    /**
     * 功能描述： 采价任务详情
     */
    @GetMapping(value = "/goods-pricing-task/{id}")
    public GoodsPricingTaskVO getById(@PathVariable Long id) {
        return goodsPricingTaskService.getById(id);
    }

    /**
     * App的任务详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping(value = "/app/goods-pricing-task/{id}")
    public GoodsPricingTaskVO getByIdForApp(@PathVariable Long id) {
        return goodsPricingTaskService.getByIdForApp(id);
    }

    /**
     * 手动完成任务
     *
     * @param id
     */
    @PutMapping(value = "/goods-pricing-task/{id}/over")
    public void overPricingTask(@PathVariable Long id, @RequestBody GoodsPricingTaskParam pricingTaskParam) {
        goodsPricingTaskService.overPricingTask(id, pricingTaskParam.getRemark());
    }

    /**
     * 结束任务前的检查
     *
     * @param id
     *
     * @return
     */
    @GetMapping(value = "/goods-pricing-task/{id}/over/check")
    public Long overPricingTaskCheck(@PathVariable Long id) {
        return goodsPricingTaskService.overPricingTaskCheck(id);
    }

    /**
     * 任务移交
     */
    @PutMapping(value = "/goods-pricing-task/{id}/transfer")
    public void transferPricingTask(@PathVariable Long id, @RequestBody GoodsPricingTaskTransferParam param) {
        goodsPricingTaskService.transferPricingTask(id, param.getFromUserId().toString(), param.getToUserIds());
    }

    /**
     * 接受任务
     *
     * @param id
     */
    @PutMapping(value = "/goods-pricing-task/{id}/receive")
    public void receivePricingTask(@PathVariable Long id) {
        goodsPricingTaskService.receivePricingTask(id);
    }

    /**
     * 统计各种状态的数量
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/goods-pricing-task/stat")
    public List<CommonStatVO> getGoodsPricingTaskStat(GoodsPricingTaskQueryParam param) {
        return goodsPricingTaskService.getGoodsPricingTaskStat(param);
    }

    /**
     * 获取个人的采价任务统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/goods-pricing-task/my/stat")
    public MyTaskStatVO getMyPricingTaskStat(GoodsPricingTaskQueryParam param) {
        return goodsPricingTaskService.getMyPricingTaskStat(param);
    }

}