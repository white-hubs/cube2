package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesTypeParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesTypeQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencySuppliesTypeService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencySuppliesTypeVO;
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
 * @Description 应急物资类型管理
 * @date 2024-01-016 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencySuppliesTypeController {
    private final EmergencySuppliesTypeService emergencySuppliesTypeService;

    /**
     * 功能描述： 应急物资类型管理添加
     */
    @PostMapping(value = "/emergency-supplies-type")
    public Long add(@Valid @RequestBody EmergencySuppliesTypeParam param) {
        return emergencySuppliesTypeService.add(param);
    }

    /**
     * 功能描述： 应急物资类型管理修改
     */
    @PutMapping(value = "/emergency-supplies-type/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EmergencySuppliesTypeParam param) {
        emergencySuppliesTypeService.update(id, param);
    }

    /**
     * 功能描述： 应急物资类型管理删除
     */
    @DeleteMapping(value = "/emergency-supplies-type/{id}")
    public void delete(@PathVariable Long id) {
        emergencySuppliesTypeService.delete(id);
    }

    /**
     * 功能描述： 应急物资类型管理批量删除
     */
    @DeleteMapping(value = "/emergency-supplies-type/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        emergencySuppliesTypeService.batchDelete(ids);
    }

    /**
     * 功能描述： 应急物资类型管理分页
     */
    @GetMapping(value = "/emergency-supplies-type/page")
    public Pagination<EmergencySuppliesTypeVO> getPage(EmergencySuppliesTypeQueryParam param) {
        return emergencySuppliesTypeService.getPage(param);
    }

    /**
     * 功能描述： 应急物资类型管理详情
     */
    @GetMapping(value = "/emergency-supplies-type/{id}")
    public EmergencySuppliesTypeVO getById(@PathVariable Long id) {
        return emergencySuppliesTypeService.getById(id);
    }

}