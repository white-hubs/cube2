package com.guhecloud.cube.deliver.project.merchant.controller.store;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallEvaluationTagQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationTag;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallEvaluationTagService;

import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * <p>
 * 档位评价标签 前端控制器
 * </p>
 *
 * @author bq
 * @since 2022-06-17
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallEvaluationTagController {

    private final StallEvaluationTagService tagService;

    /**
     * 列表查询
     * @param queryDTO
     */
    @GetMapping("/stall-evaluation-tag/list")
    public List<StallEvaluationTag> getList(StallEvaluationTagQueryDTO queryDTO, PageRequest pageRequest) {
        return tagService.lambdaQuery().eq(StallEvaluationTag::getProjectId, queryDTO.getProjectId())
                .like(StringUtils.isNotEmpty(queryDTO.getSearch()), StallEvaluationTag::getTag, queryDTO.getSearch())
                .eq(StringUtils.isNotEmpty(queryDTO.getEvaluation()), StallEvaluationTag::getEvaluation, queryDTO.getEvaluation())
                .eq(StringUtils.isNotEmpty(queryDTO.getCategory()), StallEvaluationTag::getCategory, queryDTO.getCategory())
                .list();
    }

    /**
     * 分页查询
     * @param queryDTO
     */
    @GetMapping("/stall-evaluation-tag/page")
    public PageResponse<StallEvaluationTag> getPage(StallEvaluationTagQueryDTO queryDTO, PageRequest pageRequest) {
        Page<StallEvaluationTag> page = tagService.lambdaQuery().eq(StallEvaluationTag::getProjectId, queryDTO.getProjectId())
                .like(StringUtils.isNotEmpty(queryDTO.getSearch()), StallEvaluationTag::getTag, queryDTO.getSearch())
                .eq(StringUtils.isNotEmpty(queryDTO.getEvaluation()), StallEvaluationTag::getEvaluation, queryDTO.getEvaluation())
                .eq(StringUtils.isNotEmpty(queryDTO.getCategory()), StallEvaluationTag::getCategory, queryDTO.getCategory())
                .page(pageRequest.page());
        return new PageResponse<>(page.getRecords(), page.getTotal(), page.getPages());
    }

    /**
     * 新增或修改
     * @param tag
     */
    @PostMapping("/stall-evaluation-tag")
    public void saveOrUpdate(@Valid @RequestBody StallEvaluationTag tag) {
        tagService.insertOrUpdate(tag);
    }

    /**
     * 删除
     * @param id
     */
    @DeleteMapping("/stall-evaluation-tag/{id}")
    public void remove(@PathVariable("id") String id) {
        tagService.removeById(id);
    }

}

