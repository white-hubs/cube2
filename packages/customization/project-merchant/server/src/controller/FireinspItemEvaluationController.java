package com.guhecloud.cube.deliver.project.merchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.FireinspItemEvaluationDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.FireinspItemEvaluation;
import com.guhecloud.cube.deliver.project.merchant.service.FireinspItemEvaluationService;

import com.guhecloud.platform.base.util.StringUtils;
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

import jakarta.validation.Valid;
import java.util.List;

/**
 * <p>
 * 巡检项评价配置 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2021-12-22
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FireinspItemEvaluationController {

    private final FireinspItemEvaluationService service;
    //    private final SysUserApi sysUserApi;

    @GetMapping("/fireinsp-item-evaluation/{id}")
    public FireinspItemEvaluationDTO getById(@PathVariable("id") String id) {
        FireinspItemEvaluation evaluation = service.getById(id);
        if (evaluation == null) {
            return null;
        }
        FireinspItemEvaluationDTO dto = new FireinspItemEvaluationDTO(evaluation);
        return dto;
    }

    @GetMapping("/fireinsp-item-evaluation/page")
    public PageResponse<FireinspItemEvaluationDTO> getPage(@RequestParam(required = false) String standardId, BaseQueryDTO queryDTO,
                                                           PageRequest pageRequest
                                                          ) {
        Page<FireinspItemEvaluation> page = service.lambdaQuery()
                .eq(FireinspItemEvaluation::getProjectId, queryDTO.getProjectId())
                .like(StringUtils.isNotBlank(queryDTO.getSearch()), FireinspItemEvaluation::getItemName, queryDTO.getSearch())
                .eq(StringUtils.isNotBlank(standardId), FireinspItemEvaluation::getStandardId, standardId)
                .between(queryDTO.getBeginTime() !=
                         null, FireinspItemEvaluation::getCreateTime, queryDTO.getBeginTime(), queryDTO.getEndTime())
                .page(pageRequest.page());

        List<FireinspItemEvaluationDTO> dtos = new FireinspItemEvaluationDTO(page.getRecords()).list();
        //        dtos.parallelStream().forEach(this::extendNickname);
        return new PageResponse<>(dtos, page.getTotal(), page.getPages());
    }

    @PostMapping("/fireinsp-item-evaluation")
    public void save(@Valid @RequestBody FireinspItemEvaluationDTO dto) {
        service.save(dto);
    }

    @PutMapping("/fireinsp-item-evaluation")
    public void update(@Valid @RequestBody FireinspItemEvaluationDTO dto) {
        service.update(dto);
    }

    @DeleteMapping("/fireinsp-item-evaluation/{id}")
    public void remove(@PathVariable("id") String id) {
        service.remove(id);
    }

    @DeleteMapping("/fireinsp-item-evaluation")
    public void remove(@RequestParam List<String> ids) {
        if (ids != null) {
            ids.forEach(service::remove);
        }
    }

    //    private void extendNickname(FireinspItemEvaluationDTO dto) {
    //        if (SysUserApi.ADMIN_USERNAME.equals(dto.getCreateBy())) {
    //            dto.setCreateByNickname("超管");
    //            return;
    //        }
    //        try {
    //            UserDTO one = sysUserApi.getOne(dto.getCreateBy(), true);
    //            if (one != null) dto.setCreateByNickname(one.getNickname());
    //        } catch (Exception e) {
    //            log.error("查询用户异常", e);
    //        }
    //
    //    }
}

