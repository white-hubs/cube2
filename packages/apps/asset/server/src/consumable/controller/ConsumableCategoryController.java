package com.guhecloud.cube.srv.gen.property.management.server.consumable.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.converters.ConsumableCategoryConverterFoTreeVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCategory;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCategoryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCategoryQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableCategoryService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCategoryTreeVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCategoryVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 耗材分类
 *
 * @author Appet
 * @Description 耗材分类
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableCategoryController {
    private final ConsumableCategoryService consumableCategoryService;

    /**
     * 功能描述： 耗材分类添加
     */
    @PostMapping(value = "/consumable-category")
    public Long add(@Valid @RequestBody ConsumableCategoryParam param) {
        return consumableCategoryService.add(param);
    }

    /**
     * 功能描述： 耗材分类修改
     */
    @PutMapping(value = "/consumable-category/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody ConsumableCategoryParam param) {
        consumableCategoryService.update(id, param);
    }

    /**
     * 功能描述： 耗材分类删除
     */
    @DeleteMapping(value = "/consumable-category/{id}")
    public void delete(@PathVariable Long id) {
        consumableCategoryService.delete(id);
    }

    /**
     * 功能描述： 耗材分类分页
     */
    @GetMapping(value = "/consumable-category/page")
    public Pagination<ConsumableCategoryVO> getPage(ConsumableCategoryQueryParam param) {
        return consumableCategoryService.getPage(param);
    }

    /**
     * 功能描述：耗材树
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/consumable-category/tree")
    public List<ConsumableCategoryTreeVO> getTree(ConsumableCategoryQueryParam param) {
        var list = consumableCategoryService.list(Wrappers.<ConsumableCategory>lambdaQuery()
                                                          .like(StringUtils.isNotBlank(param.getName()), ConsumableCategory::getName, param.getName())
                                                          .eq(param.getParentId() !=
                                                              null, ConsumableCategory::getParentId, param.getParentId()));
        if (CollectionUtils.isNotEmpty(list)) {
            List<Long> ids = list.stream().flatMap(d -> Arrays.stream(d.getSequence().split("-"))).distinct().map(Long::parseLong)
                    .collect(Collectors.toList());
            var categories = consumableCategoryService.list(Wrappers.<ConsumableCategory>lambdaQuery()
                                                                    .in(ConsumableCategory::getId, ids));
            var consumableCategoryTreeVOS = ConsumableCategoryConverterFoTreeVO.toTreeVOS(categories);
            return TreeUtils.assembleTrees(consumableCategoryTreeVOS);
        }
        return Collections.emptyList();
    }

    /**
     * 功能描述： 耗材分类详情
     */
    @GetMapping(value = "/consumable-category/{id}")
    public ConsumableCategoryVO getById(@PathVariable Long id) {
        return consumableCategoryService.getById(id);
    }

}