package com.guhecloud.cube.srv.gen.property.management.server.consumable.controller;

import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 耗材档案
 *
 * @author Appet
 * @Description 耗材档案
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableController {
    private final ConsumableService consumableService;

    /**
     * 新增耗材档案
     */
    @PostMapping(value = "/consumable")
    public Long add(@Valid @RequestBody ConsumableParam param) {
        return consumableService.add(param);
    }

    /**
     * 功能描述： 耗材档案修改
     */
    @PutMapping(value = "/consumable/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody ConsumableParam param) {
        consumableService.update(id, param);
    }

    /**
     * 功能描述： 耗材档案删除
     */
    @DeleteMapping(value = "/consumable/{id}")
    public void delete(@PathVariable Long id) {
        consumableService.delete(id);
    }

    /**
     * 功能描述： 耗材档案分页
     */
    @GetMapping(value = "/consumable/page")
    public Pagination<ConsumableVO> getPage(ConsumableQueryParam param) {
        return consumableService.getPage(param);
    }

    @GetMapping(value = "/consumable/page-permission")
    @DataPermissionSwitch(dataModel = "property:consumableList")
    public Pagination<ConsumableVO> getPagePermission(ConsumableQueryParam param) {
        return consumableService.getPage(param);
    }

    /**
     * 功能描述： 耗材档案详情
     */
    @GetMapping(value = "/consumable/{id}")
    public ConsumableVO getById(@PathVariable Long id) {
        return consumableService.getById(id);
    }

    /**
     * 导入耗材档案
     */
    @PostMapping("/consumable/import")
    public void importTemplate(@RequestParam("file") MultipartFile file) {
        consumableService.importTemplate(file);
    }

    /**
     * 下载耗材档案导入模板
     */
    @GetMapping("/consumable/downloadExcelTemplate")
    public void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response) {
        consumableService.downloadExcelTemplate(request, response);
    }
}