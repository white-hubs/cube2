package com.guhecloud.cube.srv.gen.property.management.server.consumable.controller;

import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddRecordParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddRecordQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableAddRecordService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableAddPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableAddRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 耗材入库
 *
 * @author lijie
 * @Description 耗材入库
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableAddRecordController {
    private final ConsumableAddRecordService consumableAddRecordService;

    /**
     * 功能描述： 耗材入库添加
     */
    @PostMapping(value = "/consumable-add-record")
    public Long add(@Valid @RequestBody ConsumableAddRecordParam param) {
        return consumableAddRecordService.add(param);
    }

    /**
     * 功能描述： 耗材入库删除
     */
    @DeleteMapping(value = "/consumable-add-record/{id}")
    public Boolean delete(@PathVariable Long id) {
        return consumableAddRecordService.delete(id);
    }

    /**
     * 耗材入库分页列表
     */
    @GetMapping(value = "/consumable-add-record/page")
    public Pagination<ConsumableAddPageVO> getPage(ConsumableAddRecordQueryParam param) {
        return consumableAddRecordService.getPage(param);
    }

    /**
     * 功能描述： 耗材入库详情
     */
    @GetMapping(value = "/consumable-add-record/{id}")
    public ConsumableAddRecordVO getById(@PathVariable Long id) {
        return consumableAddRecordService.getById(id);
    }

    /**
     * 耗材入库导出
     */
    @PostMapping(value = "/consumable-add-record/getExcel")
    public void getExcel(HttpServletResponse response, @RequestBody DepositoryDetailedPageExcelParam param) {
        consumableAddRecordService.getExcel(response, param);
    }

    /**
     * 导入模板下载
     */
    @GetMapping(value = "/consumable-store-stock/download-template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        consumableAddRecordService.downloadTemplate(request, response);
    }

    /**
     * 导入
     */
    @PostMapping(value = "/consumable-store-stock/import")
    public void importExcel(MultipartFile file) {
        consumableAddRecordService.importExcel(file);
    }
}