package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.converters.EmergOrgConverterForEmergOrgVO;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyOrganization;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyOrganizationParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyOrganizationQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyOrganizationService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyOrganizationVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Appet
 * @Description 应急组织管理
 * @date 2024-01-22 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyOrganizationController {
    private final EmergencyOrganizationService emergencyOrganizationService;

    /**
     * 功能描述： 应急组织管理添加
     */
    @PostMapping(value = "/emergency-organization")
    public Long add(@Valid @RequestBody EmergencyOrganizationParam param) {
        return emergencyOrganizationService.add(param);
    }

    /**
     * 功能描述： 应急组织管理修改
     */
    @PutMapping(value = "/emergency-organization/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EmergencyOrganizationParam param) {
        emergencyOrganizationService.update(id, param);
    }

    /**
     * 功能描述： 应急组织管理删除
     */
    @DeleteMapping(value = "/emergency-organization/{id}")
    public void delete(@PathVariable Long id) {
        emergencyOrganizationService.delete(id);
    }

    /**
     * 功能描述： 应急组织管理批量删除
     */
    @DeleteMapping(value = "/emergency-organization/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        emergencyOrganizationService.batchDelete(ids);
    }

    /**
     * 功能描述： 应急组织管理分页
     */
    @GetMapping(value = "/emergency-organization/page")
    public Pagination<EmergencyOrganizationVO> getPage(EmergencyOrganizationQueryParam param) {
        return emergencyOrganizationService.getPage(param);
    }

    /**
     * 功能描述： 应急组织管理详情
     */
    @GetMapping(value = "/emergency-organization/{id}")
    public EmergencyOrganizationVO getById(@PathVariable Long id) {
        return emergencyOrganizationService.getById(id);
    }

    /**
     * 获取应急组织树
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/emergency-organization/tree")
    public List<EmergencyOrganizationVO> getEmergencyOrganizationTree(EmergencyOrganizationQueryParam param) {
        var dataList = this.emergencyOrganizationService.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getName()), EmergencyOrganization::getName, param.getName())
                .list();
        if (CollectionUtils.isNotEmpty(dataList)) {
            var orgIdList = dataList.stream().map(o -> Arrays.stream(o.getSequence().split("-"))
                            .map(Long::parseLong).collect(Collectors.toList()))
                    .flatMap(Collection::stream).distinct().collect(Collectors.toList());
            var finalOrgList = this.emergencyOrganizationService.lambdaQuery()
                    .in(EmergencyOrganization::getId, orgIdList).list();
            var emergencyOrgVOS = EmergOrgConverterForEmergOrgVO.toEmergencyOrgVOS(finalOrgList);
            return TreeUtils.assembleTrees(emergencyOrgVOS);
        }
        return Collections.emptyList();
    }

}