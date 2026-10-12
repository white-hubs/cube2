package com.guhecloud.cube.srv.gen.property.management.server.consumable.controller;

import com.guhecloud.cube.property.management.client.common.vo.PieChartValueVO;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceRecordParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceRecordQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableReduceRecordService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableReduceRecordVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableReduceStockStatVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockExtendVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 耗材出库记录
 *
 * @author Appet
 * @Description 耗材出库记录
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableReduceRecordController {
    private final ConsumableReduceRecordService consumableReduceRecordService;

    /**
     * 功能描述： 耗材出库记录删除
     */
    @DeleteMapping(value = "/consumable-reduce-record/{id}")
    public void delete(@PathVariable Long id) {
        consumableReduceRecordService.delete(id);
    }

    /**
     * 功能描述： 耗材出库记录分页
     */
    @GetMapping(value = "/consumable-reduce-record/page")
    public Pagination<ConsumableReduceRecordVO> getPage(ConsumableReduceRecordQueryParam param) {
        return consumableReduceRecordService.getPage(param);
    }

    /**
     * 功能描述： 耗材出库记录详情
     */
    @GetMapping(value = "/consumable-reduce-record/{id}")
    public ConsumableReduceRecordVO getById(@PathVariable Long id) {
        return consumableReduceRecordService.getById(id);
    }

    /**
     * 耗材出库统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/consumable-reduce-record/stat")
    public List<ConsumableReduceStockStatVO> getReduceStockStat(ConsumableReduceRecordParam param) {
        return consumableReduceRecordService.getReduceStockStat(param);
    }

    /**
     * 部门使用耗材占比
     *
     * @param depId  部门id
     * @param period 时间
     *
     * @return 部门资产统计
     */
    @GetMapping(value = "/consumable-reduce-record/getReduceStockStatByDep")
    public List<PieChartValueVO> getReduceStockStatByDep(String depId, String period) {
        return consumableReduceRecordService.getReduceStockStatByDep(depId, period);
    }

    /**
     * 获取出库明细
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/consumable-reduce-record/detail")
    public Pagination<ConsumableStoreStockExtendVO> getReduceDetail(ConsumableReduceRecordParam param) {
        return consumableReduceRecordService.getReduceDetail(param);
    }

    /**
     * 耗材导出
     */
    @PostMapping(value = "/consumable-check-record/getExcel")
    public void getExcel(HttpServletResponse response, @RequestBody DepositoryDetailedPageExcelParam param) {
        consumableReduceRecordService.getExcel(response, param);
    }
}