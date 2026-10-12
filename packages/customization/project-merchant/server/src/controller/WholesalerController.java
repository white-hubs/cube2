package com.guhecloud.cube.deliver.project.merchant.controller;

import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.util.ExcelUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyUserDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholeSpaceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerListDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerShortDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerUserExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerUserQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import java.util.Comparator;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/04
 * @description 批发商基本新（包含有人员列表的相关API）
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerController {

    private final WholesalerService service;

    @GetMapping("/wholesaler/company/{stallId}")
    public WholesalerListDTO getWholesalerByStallId(String projectId, @PathVariable("stallId") String stallId) {
        return service.getWholesalerByStallId(projectId, stallId);
    }

    @GetMapping("/wholesaler/page")
    public PageResponse<WholesalerListDTO> getWholesalerPage(WholesalerQueryDTO queryDTO, PageRequest pageRequest) {
        return PageResponse.doPage(service.getWholesalerPage(queryDTO), pageRequest);
    }

    @GetMapping("/wholesaler/company")
    public List<WholesalerShortDTO> getWholesalerList(String projectId) {
        return service.getWholesalerList(projectId);
    }

    @GetMapping("/wholesaler/stall/list")
    public List<WholeSpaceDTO> getWholesalerStalls(String projectId) {
        return service.getWholesalerStalls(projectId);
    }

    @GetMapping("/wholesaler/stall/owner/page")
    public PageResponse<WholeSpaceDTO> getWholesalerOwnerStalls(String projectId, PageRequest pageRequest) {
        List<WholeSpaceDTO> dtos = service.getWholesalerStalls(projectId);
        return PageResponse.doPage(dtos, pageRequest);
    }

    @GetMapping("/wholesaler/stall/page")
    public PageResponse<WholeSpaceDTO> getWholesalerStalls(String projectId, PageRequest pageRequest) {
        return PageResponse.doPage(service.getWholesalerStalls(projectId), pageRequest);
    }

    @GetMapping("/wholesaler/{id}")
    public WholesalerDTO getWholesaler(@PathVariable("id") String id) {
        return service.getWholesaler(id);
    }

    @GetMapping("/wholesaler/user/page")
    public PageResponse<CompanyUserDTO> getWholesalerUsers(WholesalerUserQueryDTO queryDto, PageRequest pageRequest) {
        List<CompanyUserDTO> wholesalerUsers = service.getWholesalerUsers(queryDto);
        wholesalerUsers.sort(Comparator.comparing(CompanyUserDTO::getCreateTime, Comparator.reverseOrder()));
        return PageResponse.doPage(wholesalerUsers, pageRequest);
    }

    @PostMapping("/wholesaler/user")
    public String save(@Valid @RequestBody CompanyUserDTO dto, LoginUser user) {
        return service.save(dto, user);
    }

    @PutMapping("/wholesaler/user")
    public void update(@RequestBody CompanyUserDTO dto) {
        service.update(dto);
    }

    @DeleteMapping("/wholesaler/user/{id}")
    public void remove(@PathVariable("id") String id) {
        service.remove(id);
    }

    @PostMapping("/wholesaler")
    public String saveWholesaler(@Valid @RequestBody CompanyDTO dto) {
        return service.saveWholesaler(dto);
    }

    /**
     * 导出人员测试
     */

    @GetMapping("/wholesaler/data-export")
    public void exportData(WholesalerUserQueryDTO queryDto, HttpServletRequest request, HttpServletResponse response) {
        List<CompanyUserDTO> companyUserDtoList = service.getWholesalerUsers(queryDto);
        List<WholesalerUserExcelDTO> dtos = new WholesalerUserExcelDTO(companyUserDtoList).list();
        ExcelUtils<WholesalerUserExcelDTO> utils = new ExcelUtils<>(WholesalerUserExcelDTO.class);
        utils.createWorkbook();
        try {
            utils.createSheet("人员信息", dtos);
        } catch (Exception e) {
            log.error("导出数据异常", e);
            throw new ServiceException("导出数据失败", e);
        }
        utils.downLoadExcel("人员信息", request, response);
    }

}
