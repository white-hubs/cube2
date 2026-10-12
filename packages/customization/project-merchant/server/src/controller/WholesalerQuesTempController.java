package com.guhecloud.cube.deliver.project.merchant.controller;

import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerQuesTempDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerQuesTempQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuesTemp;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerQuesTempService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
 * @since 2022-01-11
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerQuesTempController {

    private final WholesalerQuesTempService service;

    @GetMapping("/wholesaler-ques-temp/page")
    public PageResponse<WholesalerQuesTempDTO> getTempDtoList(WholesalerQuesTempQueryDTO queryDto, PageRequest pageRequest) {
        List<WholesalerQuesTempDTO> dtos = service.getTempDtoList(queryDto);
        return PageResponse.doPage(dtos, pageRequest);
    }

    @GetMapping("/wholesaler-ques-temp/list")
    public List<WholesalerQuesTemp> getTempList(String projectId) {
        return service.lambdaQuery().eq(WholesalerQuesTemp::getProjectId, projectId).list();
    }

    @GetMapping("/wholesaler-ques-temp/{id}")
    public WholesalerQuesTempDTO getTempDto(@PathVariable("id") String id) {
        return service.getTempDto(id);
    }

    @PostMapping("/wholesaler-ques-temp")
    public String saveOrUpdate(@Valid @RequestBody WholesalerQuesTempDTO dto) {
        return service.saveOrUpdate(dto);
    }

    @DeleteMapping("/wholesaler-ques-temp/{id}")
    public void remove(@PathVariable("id") String id) {
        service.remove(id);
    }

}

