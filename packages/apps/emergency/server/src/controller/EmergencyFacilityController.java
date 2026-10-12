package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.param.EmergencyFacilityParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFacilityQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFacilityService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFacilityStatVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFacilityVO;
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
 * @Description 应急设施管理
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFacilityController {
    private final EmergencyFacilityService emergencyFacilityService;

    /**
     * 功能描述： 应急设施管理添加
     */
    @PostMapping(value = "/emergency-facility")
    public Long add(@Valid @RequestBody EmergencyFacilityParam param) {
        return emergencyFacilityService.add(param);
    }

    /**
     * 功能描述： 应急设施管理修改
     */
    @PutMapping(value = "/emergency-facility/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EmergencyFacilityParam param) {
        emergencyFacilityService.update(id, param);
    }

    /**
     * 功能描述： 应急设施管理删除
     */
    @DeleteMapping(value = "/emergency-facility/{id}")
    public void delete(@PathVariable Long id) {
        emergencyFacilityService.delete(id);
    }

    /**
     * 功能描述： 应急设施管理批量删除
     */
    @DeleteMapping(value = "/emergency-facility/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        emergencyFacilityService.batchDelete(ids);
    }

    /**
     * 功能描述： 应急设施管理分页
     */
    @GetMapping(value = "/emergency-facility/page")
    public Pagination<EmergencyFacilityVO> getPage(EmergencyFacilityQueryParam param) {
        return emergencyFacilityService.getPage(param);
    }

    /**
     * 功能描述： 应急设施管理详情
     */
    @GetMapping(value = "/emergency-facility/{id}")
    public EmergencyFacilityVO getById(@PathVariable Long id) {
        return emergencyFacilityService.getById(id);
    }

    /**
     * 应急设施使用统计
     *
     * @return
     */
    @GetMapping(value = "/emergency-facility/stat")
    EmergencyFacilityStatVO getEmergencyFacilityStatForUsed() {
        return emergencyFacilityService.getEmergencyFacilityStatForUsed();
    }
}