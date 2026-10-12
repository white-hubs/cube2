package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesInoutRecordParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesInoutRecordQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencySuppliesInoutRecordService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencySuppliesInoutRecordVO;
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
 * @Description 应急物资出入库记录
 * @date 2024-01-016 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencySuppliesInoutRecordController {
    private final EmergencySuppliesInoutRecordService emergencySuppliesInoutRecordService;

    /**
     * 功能描述： 应急物资出入库记录添加
     */
    @PostMapping(value = "/emergency-supplies-inout-record")
    public Long add(@Valid @RequestBody EmergencySuppliesInoutRecordParam param) {
        return emergencySuppliesInoutRecordService.add(param);
    }

    /**
     * 功能描述： 应急物资出入库记录修改
     */
    @PutMapping(value = "/emergency-supplies-inout-record/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EmergencySuppliesInoutRecordParam param) {
        emergencySuppliesInoutRecordService.update(id, param);
    }

    /**
     * 功能描述： 应急物资出入库记录删除
     */
    @DeleteMapping(value = "/emergency-supplies-inout-record/{id}")
    public void delete(@PathVariable Long id) {
        emergencySuppliesInoutRecordService.delete(id);
    }

    /**
     * 功能描述： 应急物资出入库记录批量删除
     */
    @DeleteMapping(value = "/emergency-supplies-inout-record/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        emergencySuppliesInoutRecordService.batchDelete(ids);
    }

    /**
     * 功能描述： 应急物资出入库记录分页
     */
    @GetMapping(value = "/emergency-supplies-inout-record/page")
    public Pagination<EmergencySuppliesInoutRecordVO> getPage(EmergencySuppliesInoutRecordQueryParam param) {
        return emergencySuppliesInoutRecordService.getPage(param);
    }

    /**
     * 功能描述： 应急物资出入库记录详情
     */
    @GetMapping(value = "/emergency-supplies-inout-record/{id}")
    public EmergencySuppliesInoutRecordVO getById(@PathVariable Long id) {
        return emergencySuppliesInoutRecordService.getById(id);
    }

}