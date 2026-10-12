package com.guhecloud.cube.deliver.project.merchant.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerProtocolTemplateDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerProtocolTemplateQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerProtocolTemplate;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerProtocolTemplateService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangliang
 * @since 2022-01-10
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerProtocolTemplateController {

    private final WholesalerProtocolTemplateService service;

    @GetMapping("/wholesaler-protocol-template")
    public PageResponse<WholesalerProtocolTemplateDTO> getTemplates(WholesalerProtocolTemplateQueryDTO queryDto, PageRequest pageRequest) {
        Page<WholesalerProtocolTemplate> page = service.lambdaQuery()
                .eq(WholesalerProtocolTemplate::getProjectId, queryDto.getProjectId())
                .eq(StrUtil.isNotBlank(queryDto.getType()), WholesalerProtocolTemplate::getType, queryDto.getType())
                .like(StrUtil.isNotBlank(queryDto.getSearch()), WholesalerProtocolTemplate::getName, queryDto.getSearch())
                .between(null != queryDto.getBeginTime() && null !=
                                                            queryDto.getEndTime(), WholesalerProtocolTemplate::getCreateTime, queryDto.getBeginTime(), queryDto.getEndTime())
                .orderByDesc(WholesalerProtocolTemplate::getCreateTime)
                .page(pageRequest.page());
        List<WholesalerProtocolTemplateDTO> dtos = new WholesalerProtocolTemplateDTO(page.getRecords()).list();
        return new PageResponse<>(dtos, page.getTotal(), page.getPages());
    }

    @GetMapping("/wholesaler-protocol-template/list")
    public List<WholesalerProtocolTemplate> getTemplates(String projectId) {
        return service.getTemplateList(projectId);
    }

    @GetMapping("/wholesaler-protocol-template/{id}")
    public WholesalerProtocolTemplateDTO getTemplateById(@PathVariable("id") String id) {
        return service.getTemplateById(id);
    }

    @PostMapping("/wholesaler-protocol-template")
    public String save(@Valid @RequestBody WholesalerProtocolTemplateDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/wholesaler-protocol-template")
    public String update(@Valid @RequestBody WholesalerProtocolTemplateDTO dto) {
        return service.update(dto);
    }

    @DeleteMapping("/wholesaler-protocol-template/{id}")
    public void removeById(@PathVariable("id") String id) {
        service.removeById(id);
    }

}

