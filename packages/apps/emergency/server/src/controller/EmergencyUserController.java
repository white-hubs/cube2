package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.param.EmergencyUserParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyUserQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyUserService;
import com.guhecloud.cube.biz.emergency.server.vo.CommonStatVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyUserVO;
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
 * @Description 应急人员管理
 * @date 2024-01-22 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyUserController {
    private final EmergencyUserService emergencyUserService;

    /**
     * 功能描述： 应急人员管理添加
     */
    @PostMapping(value = "/emergency-user")
    public Long add(@Valid @RequestBody EmergencyUserParam param) {
        return emergencyUserService.add(param);
    }

    /**
     * 功能描述： 应急人员管理修改
     */
    @PutMapping(value = "/emergency-user/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EmergencyUserParam param) {
        emergencyUserService.update(id, param);
    }

    /**
     * 功能描述： 应急人员管理删除
     */
    @DeleteMapping(value = "/emergency-user/{id}")
    public void delete(@PathVariable Long id) {
        emergencyUserService.delete(id);
    }

    /**
     * 功能描述： 应急人员管理批量删除
     */
    @DeleteMapping(value = "/emergency-user/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        emergencyUserService.batchDelete(ids);
    }

    /**
     * 功能描述： 应急人员管理分页
     */
    @GetMapping(value = "/emergency-user/page")
    public Pagination<EmergencyUserVO> getPage(EmergencyUserQueryParam param) {
        return emergencyUserService.getPage(param);
    }

    /**
     * 功能描述： 应急人员管理详情
     */
    @GetMapping(value = "/emergency-user/{id}")
    public EmergencyUserVO getById(@PathVariable Long id) {
        return emergencyUserService.getById(id);
    }

    /**
     * 人员类型统计
     *
     * @return
     */
    @GetMapping(value = "/emergency-user/stat")
    public List<CommonStatVO> getEmergencyUserStat() {
        return emergencyUserService.getEmergencyUserStat();
    }

}