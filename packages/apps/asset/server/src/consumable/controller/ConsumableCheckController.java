package com.guhecloud.cube.srv.gen.property.management.server.consumable.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCheck;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableCheckService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckDetailStatVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckWithStatVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockExtendVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.validation.Valid;
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
 * 耗材盘点
 *
 * @author lijie
 * @Description 耗材盘点
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableCheckController {
    private final ConsumableCheckService consumableCheckService;

    /**
     * 功能描述： 耗材盘点添加
     */
    @PostMapping(value = "/consumable-check")
    public Long add(@Valid @RequestBody ConsumableCheckParam param) {
        return consumableCheckService.add(param);
    }

    /**
     * 功能描述： 耗材盘点修改
     */
    @PutMapping(value = "/consumable-check/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody ConsumableCheckParam param) {
        consumableCheckService.update(id, param);
    }

    /**
     * 将盘点单改成完成状态
     *
     * @param id
     */
    @GetMapping(value = "/consumable-check/{id}/submit")
    public Boolean updateForSubmit(@PathVariable Long id, @RequestParam(defaultValue = "false") Boolean updateStock) {
        return consumableCheckService.updateForSubmit(id, updateStock);
    }

    /**
     * 删除耗材盘点任务
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping(value = "/consumable-check/{id}")
    public void delete(@PathVariable Long id) {
        consumableCheckService.delete(id);
    }

    /**
     * 功能描述： 耗材盘点单分页
     */
    @GetMapping(value = "/consumable-check/page")
    public Pagination<ConsumableCheckVO> getPage(ConsumableCheckQueryParam param) {
        return consumableCheckService.getPage(null, param);
    }

    /**
     * 功能描述： App端 耗材盘点单分页
     */
    @GetMapping(value = "/app/consumable-check/page")
    public Pagination<ConsumableCheckWithStatVO> getPageForApp(ConsumableCheckQueryParam param) {
        return consumableCheckService.getPageForApp(param);
    }

    /**
     * 盘点明细查询
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/consumable-check/detail/page")
    public Pagination<ConsumableStoreStockExtendVO> getDetailPage(@Valid ConsumableCheckDetailParam param) {
        return consumableCheckService.getCheckDetailPage(param);
    }

    /**
     * 通过盘点单ID 统计盘点情况
     *
     * @param checkId
     *
     * @return
     */
    @GetMapping(value = "/consumable-check/{id}/stat")
    public ConsumableCheckDetailStatVO getCheckStatById(@PathVariable("id") Long checkId) {
        return consumableCheckService.getCheckStatById(checkId);
    }

    /**
     * 功能描述： 耗材盘点单详情
     */
    @GetMapping(value = "/consumable-check/{id}")
    public ConsumableCheckVO getById(@PathVariable Long id) {
        return consumableCheckService.getById(id);
    }

    /**
     * 校验查库是否有未完成的盘点
     *
     * @param storeId
     *
     * @return
     */
    @GetMapping(value = "/consumable-check/{storeId}/undone")
    public Boolean validStoreCheck(@PathVariable("storeId") Long storeId) {
        List<ConsumableCheck> list = consumableCheckService.list(Wrappers.<ConsumableCheck>lambdaQuery()
                                                                         .eq(ConsumableCheck::getStoreId, storeId)
                                                                         .eq(ConsumableCheck::getStatus, TaskStatusEnum.UNDONE));
        Assert.isNotAcceptable(CollectionUtils.isNotEmpty(list), "该仓库有未完成的盘点，不能出入库");
        return true;
    }

    /**
     * 盘点是否有盘赢耗材
     *
     * @param taskId 盘点任务id
     *
     * @return 是否有盘赢耗材
     */
    @GetMapping(value = "/consumable-check/submitWin/{taskId}")
    public Boolean submitWin(@PathVariable("taskId") Long taskId) {
        return consumableCheckService.submitWin(taskId);
    }

    /**
     * 盘赢同步库存
     *
     * @param taskId 盘点任务id
     *
     * @return 是否有盘赢耗材
     */
    @GetMapping(value = "/consumable-check/addWin/{taskId}")
    public Boolean addWin(@PathVariable("taskId") Long taskId) {
        return consumableCheckService.addWin(taskId);
    }
}