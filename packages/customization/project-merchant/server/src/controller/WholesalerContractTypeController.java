package com.guhecloud.cube.deliver.project.merchant.controller;

import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerContractTypeDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerContractTypeQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerContractTypeService;

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
 * @since 2022-01-13
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerContractTypeController {

    private final WholesalerContractTypeService service;

    @GetMapping("/wholesaler-contract-type/list")
    public List<WholesalerContractTypeDTO> getTypeList(WholesalerContractTypeQueryDTO queryDTO) {
        return service.getTypes(queryDTO);
    }

    @GetMapping("/wholesaler-contract-type")
    public PageResponse<WholesalerContractTypeDTO> getTypes(WholesalerContractTypeQueryDTO queryDTO, PageRequest pageRequest) {
        return PageResponse.doPage(service.getTypes(queryDTO), pageRequest);
    }

    @PostMapping("/wholesaler-contract-type")
    public String saveOrUpdate(@Valid @RequestBody WholesalerContractTypeDTO dto) {
        return service.saveOrUpdate(dto);
    }

    @DeleteMapping("/wholesaler-contract-type/{id}")
    public void removeById(@PathVariable("id") String id) {
        service.removeById(id);
    }

}

