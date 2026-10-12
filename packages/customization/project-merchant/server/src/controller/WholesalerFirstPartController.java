package com.guhecloud.cube.deliver.project.merchant.controller;

import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerFirstPartDTO;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerFirstPartService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangliang
 * @since 2022-01-13
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerFirstPartController {

    private final WholesalerFirstPartService service;

    @GetMapping("/wholesaler-first-part/{projectId}")
    public WholesalerFirstPartDTO getFirstPart(@PathVariable("projectId") String projectId) {
        return service.getFirstPart(projectId);
    }

    @PostMapping("/wholesaler-first-part")
    public Long saveOrUpdate(@Valid @RequestBody WholesalerFirstPartDTO dto) {
        return service.saveOrUpdate(dto);
    }
}

