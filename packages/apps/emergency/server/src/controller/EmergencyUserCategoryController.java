package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.converters.EmergUserCatConverterForEmergUserCatVO;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyUserCategory;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyOrganizationQueryParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyUserCategoryParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyUserCategoryQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyUserCategoryService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyUserCategoryVO;
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
 * @Description 应急人员分类管理
 * @date 2024-01-22 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyUserCategoryController {
    private final EmergencyUserCategoryService emergencyUserCategoryService;

    /**
     * 功能描述： 应急人员分类管理添加
     */
    @PostMapping(value = "/emergency-user-category")
    public Long add(@Valid @RequestBody EmergencyUserCategoryParam param) {
        return emergencyUserCategoryService.add(param);
    }

    /**
     * 功能描述： 应急人员分类管理修改
     */
    @PutMapping(value = "/emergency-user-category/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EmergencyUserCategoryParam param) {
        emergencyUserCategoryService.update(id, param);
    }

    /**
     * 功能描述： 应急人员分类管理删除
     */
    @DeleteMapping(value = "/emergency-user-category/{id}")
    public void delete(@PathVariable Long id) {
        emergencyUserCategoryService.delete(id);
    }

    /**
     * 功能描述： 应急人员分类管理批量删除
     */
    @DeleteMapping(value = "/emergency-user-category/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        emergencyUserCategoryService.batchDelete(ids);
    }

    /**
     * 功能描述： 应急人员分类管理分页
     */
    @GetMapping(value = "/emergency-user-category/page")
    public Pagination<EmergencyUserCategoryVO> getPage(EmergencyUserCategoryQueryParam param) {
        return emergencyUserCategoryService.getPage(param);
    }

    /**
     * 功能描述： 应急人员分类管理详情
     */
    @GetMapping(value = "/emergency-user-category/{id}")
    public EmergencyUserCategoryVO getById(@PathVariable Long id) {
        return emergencyUserCategoryService.getById(id);
    }

    /**
     * 获取应急组织树
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/emergency-user-category/tree")
    public List<EmergencyUserCategoryVO> getEmergencyOrganizationTree(EmergencyOrganizationQueryParam param) {
        var dataList = this.emergencyUserCategoryService.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getName()), EmergencyUserCategory::getName, param.getName())
                .list();
        if (CollectionUtils.isNotEmpty(dataList)) {
            var orgIdList = dataList.stream().map(o -> Arrays.stream(o.getSequence().split("-"))
                            .map(Long::parseLong).collect(Collectors.toList()))
                    .flatMap(Collection::stream).distinct().collect(Collectors.toList());
            var finalOrgList = this.emergencyUserCategoryService.lambdaQuery()
                    .in(EmergencyUserCategory::getId, orgIdList).list();
            var emergencyUserCatVOS = EmergUserCatConverterForEmergUserCatVO.toEmergencyUserCatVOS(finalOrgList);
            return TreeUtils.assembleTrees(emergencyUserCatVOS);
        }
        return Collections.emptyList();
    }

}