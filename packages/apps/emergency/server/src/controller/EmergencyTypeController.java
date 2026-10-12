package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.entity.EmergencyType;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyTypeModifyParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyTypeQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyTypeService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyTypeVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 预案类型管理接口
 *
 * @author lanchun
 * @date 2022/9/2 14:01
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyTypeController {

    private final EmergencyTypeService emergencyTypeService;

    /**
     * 预案类型新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/emergency-type")
    public Long save(@Valid @RequestBody EmergencyTypeModifyParam param) {
        return this.emergencyTypeService.save(param);
    }

    /**
     * 预案类型修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/emergency-type/{id}")
    public void update(@Min(value = 1, message = "预案类型主键不能小于1") @PathVariable("id") Long id,
                       @Valid @RequestBody EmergencyTypeModifyParam param
                      ) {
        this.emergencyTypeService.update(id, param);
    }

    /**
     * 预案类型详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/emergency-type/{id}")
    public EmergencyTypeVO getEmergencyType(@Min(value = 1, message = "预案类型主键不能小于1") @PathVariable("id") Long id) {
        EmergencyType emergencyType = this.emergencyTypeService.getById(id);
        Assert.isNotFound(emergencyType, "预案类型不存在");
        return ObjectUtils.copying(emergencyType, EmergencyTypeVO.class);
    }

    /**
     * 通过应用编号获取预案类型
     *
     * @param applicationCode
     *
     * @return
     */
    @GetMapping("/emergency-type/detail/by-app-code/{applicationCode}")
    public EmergencyTypeVO getEmergencyTypeByAppCode(@PathVariable("applicationCode") String applicationCode) {
        EmergencyType emergencyType = this.emergencyTypeService.getEmergencyTypeByAppCode(applicationCode);
        return ObjectUtils.copying(emergencyType, EmergencyTypeVO.class);
    }

    /**
     * 预案类型列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/emergency-types")
    public List<EmergencyTypeVO> listEmergencyTypes(@Valid EmergencyTypeQueryParam param) {
        List<EmergencyType> emergencyTypes = this.emergencyTypeService.listEmergencyTypes(param);
        if (CollectionUtils.isEmpty(emergencyTypes)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(emergencyTypes, EmergencyTypeVO.class);
    }

    /**
     * 预案类型删除
     *
     * @param id
     */
    @DeleteMapping("/emergency-type/{id}")
    public void remove(@Min(value = 1, message = "预案类型主键不能小于1") @PathVariable("id") Long id) {
        this.emergencyTypeService.remove(id);
    }
}
