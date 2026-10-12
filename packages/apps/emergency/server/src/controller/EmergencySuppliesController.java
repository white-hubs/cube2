package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencySuppliesService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencySuppliesStatVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencySuppliesVO;
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
 * @Description 应急物资管理
 * @date 2024-01-016 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencySuppliesController {
    private final EmergencySuppliesService emergencySuppliesService;

    /**
     * 功能描述： 应急物资管理添加
     */
    @PostMapping(value = "/emergency-supplies")
    public Long add(@Valid @RequestBody EmergencySuppliesParam param) {
        return emergencySuppliesService.add(param);
    }

    /**
     * 功能描述： 应急物资管理修改
     */
    @PutMapping(value = "/emergency-supplies/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EmergencySuppliesParam param) {
        emergencySuppliesService.update(id, param);
    }

    /**
     * 功能描述： 应急物资管理删除
     */
    @DeleteMapping(value = "/emergency-supplies/{id}")
    public void delete(@PathVariable Long id) {
        emergencySuppliesService.delete(id);
    }

    /**
     * 功能描述： 应急物资管理批量删除
     */
    @DeleteMapping(value = "/emergency-supplies/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        emergencySuppliesService.batchDelete(ids);
    }

    /**
     * 功能描述： 应急物资管理分页
     */
    @GetMapping(value = "/emergency-supplies/page")
    public Pagination<EmergencySuppliesVO> getPage(EmergencySuppliesQueryParam param) {
        return emergencySuppliesService.getPage(param);
    }

    /**
     * 功能描述： 应急物资管理详情
     */
    @GetMapping(value = "/emergency-supplies/{id}")
    public EmergencySuppliesVO getById(@PathVariable Long id) {
        return emergencySuppliesService.getById(id);
    }

    /**
     * 物资在库数量统计
     *
     * @return
     */
    @GetMapping(value = "/emergency-supplies/stock/stat")
    public List<EmergencySuppliesStatVO> getEmergencySuppliesStatForStock() {
        return emergencySuppliesService.getEmergencySuppliesStatForStock();
    }
}