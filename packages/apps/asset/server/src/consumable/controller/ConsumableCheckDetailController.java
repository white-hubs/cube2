package com.guhecloud.cube.srv.gen.property.management.server.consumable.controller;

import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.AppConsumablePageParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckDetailAddParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckDetailQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableCheckDetailService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.AppConsumablePageVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckDetailVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 耗材盘点明细
 *
 * @author Appet
 * @Description 耗材盘点明细
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableCheckDetailController {
    private final ConsumableCheckDetailService consumableCheckDetailService;

    /**
     * 新增耗材盘点记录
     */
    @PostMapping(value = "/consumable-check-detail")
    public Long add(@Valid @RequestBody ConsumableCheckDetailAddParam param) {
        return consumableCheckDetailService.add(param);
    }

    /**
     * 功能描述： 耗材盘点明细分页
     */
    @GetMapping(value = "/consumable-check-detail/page")
    public Pagination<ConsumableCheckDetailVO> getPage(ConsumableCheckDetailQueryParam param) {
        return consumableCheckDetailService.getPage(param);
    }

    /**
     * 功能描述： 耗材盘点明细详情
     */
    @GetMapping(value = "/consumable-check-detail/{id}")
    public ConsumableCheckDetailVO getById(@PathVariable Long id) {
        return consumableCheckDetailService.getById(id);
    }

    /**
     * 耗材盘点资产信息
     */
    @GetMapping(value = "/app/consumable-check-detail/page")
    public Pagination<AppConsumablePageVO> getAppPage(@Valid AppConsumablePageParam param) {
        return consumableCheckDetailService.getAppPage(param);
    }

    /**
     * 耗材盘点单导出
     */
    @PostMapping(value = "/consumable-check-detail/getExcel")
    public void getExcel(HttpServletResponse response, @RequestBody DepositoryDetailedPageExcelParam param) {
        consumableCheckDetailService.getExcel(response, param);
    }
}