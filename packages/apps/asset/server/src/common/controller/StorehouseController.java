package com.guhecloud.cube.srv.gen.property.management.server.common.controller;

import com.guhecloud.cube.srv.gen.property.management.server.common.param.StorehouseParam;
import com.guhecloud.cube.srv.gen.property.management.server.common.param.StorehouseQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.StorehouseService;
import com.guhecloud.cube.srv.gen.property.management.server.common.vo.StorehouseVO;
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
 * @Description 仓库管理
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StorehouseController {
    private final StorehouseService storehouseService;

    /**
     * 功能描述： 仓库管理添加
     */
    @PostMapping(value = "/storehouse")
    public Long add(@Valid @RequestBody StorehouseParam param) {
        return storehouseService.add(param);
    }

    /**
     * 功能描述： 仓库管理修改
     */
    @PutMapping(value = "/storehouse/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody StorehouseParam param) {
        storehouseService.update(id, param);
    }

    /**
     * 功能描述： 仓库管理删除
     */
    @DeleteMapping(value = "/storehouse/{id}")
    public void delete(@PathVariable Long id) {
        storehouseService.delete(id);
    }

    /**
     * 功能描述： 仓库管理批量删除
     */
    @DeleteMapping(value = "/storehouse/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        storehouseService.batchDelete(ids);
    }

    /**
     * 功能描述： 仓库管理分页
     */
    @GetMapping(value = "/storehouse/page")
    public Pagination<StorehouseVO> getPage(StorehouseQueryParam param) {
        return storehouseService.getPage(param);
    }

    @GetMapping(value = "/storehouse/list")
    public List<StorehouseVO> getList(StorehouseQueryParam param) {
        return storehouseService.getList(param);
    }

    /**
     * 功能描述： 仓库管理详情
     */
    @GetMapping(value = "/storehouse/{id}")
    public StorehouseVO getById(@PathVariable Long id) {
        return storehouseService.getById(id);
    }

}