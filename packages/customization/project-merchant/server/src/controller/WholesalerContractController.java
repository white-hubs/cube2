package com.guhecloud.cube.deliver.project.merchant.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.ExcelUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerContractDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerContractExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerContractQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerContractSignDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerIdBatchDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerSecondPartDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerContract;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerContractService;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangliang
 * @since 2022-01-14
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerContractController {

    private final WholesalerContractService service;

    @GetMapping("/wholesaler-contract/list")
    public List<WholesalerContract> getContractList(String projectId) {
        return service.getContractList(projectId);
    }

    @GetMapping("/wholesaler-contract/notice/{id}")
    public void sendNotice(String projectId, @PathVariable("id") String id) {
        service.sendNotice(projectId, id);
    }

    @GetMapping("/wholesaler-contract/notice/ids")
    public void sendNotices(String projectId, @RequestBody WholesalerIdBatchDTO dto) {
        dto.getIds().forEach(id -> service.sendNotice(projectId, id));
    }

    @GetMapping("/wholesaler-contract/page")
    public PageResponse<WholesalerContractDTO> getContracts(WholesalerContractQueryDTO queryDTO, PageRequest pageRequest) {
        return PageResponse.doPage(service.getContracts(queryDTO), pageRequest);
    }

    @GetMapping("/wholesaler-contract/mine/page")
    public PageResponse<WholesalerContractDTO> getMyContractDTOS(WholesalerContractQueryDTO queryDTO, PageRequest pageRequest) {
        return service.getMyContractDTOS(queryDTO, pageRequest);
    }

    @GetMapping("/wholesaler-contract/second-part/verify-code")
    public String sendVerifyCode(String projectId, String contractId, String mobile, LoginUser user) {
        return service.sendVerifyCode(projectId, contractId, mobile, user);
    }

    @GetMapping("/wholesaler-contract/{id}")
    public WholesalerContractDTO getContractById(@PathVariable("id") String id) {
        return service.getContractById(id);
    }

    @PostMapping("/wholesaler-contract")
    public Long saveOrUpdate(@Valid @RequestBody WholesalerContractDTO dto) {
        return service.saveOrUpdate(dto);
    }

    @PostMapping("/wholesaler-contract/second-part")
    public String signBySecondPart(@Valid @RequestBody WholesalerSecondPartDTO dto) {
        return service.signBySecondPart(dto);
    }

    @PutMapping("/wholesaler-contract/second-part/confirm")
    public void signConfirmSecondPart(@Valid @RequestBody WholesalerContractSignDTO dto, LoginUser user) {
        service.signConfirm(dto, user);
    }

    @GetMapping("/wholesaler-contract/first-part/confirm")
    public void signConfirmFirstPart(String signatoryName, String signatoryId, String contractId) {
        service.signConfirmFirstPart(signatoryName, signatoryId, contractId);
    }

    @PutMapping("/wholesaler-contract/commit/{id}")
    public void commit(@PathVariable("id") String id) {
        service.commit(id);
    }

    @GetMapping("/wholesaler-contract/data-export")
    public void exportData(WholesalerContractQueryDTO queryDto, HttpServletRequest request, HttpServletResponse response) {
        List<WholesalerContractDTO> contractDTOList = service.getContracts(queryDto);
        List<WholesalerContractExcelDTO> dtos = new ArrayList<>();
        contractDTOList.forEach(dto -> {
            WholesalerContractExcelDTO wholesaler = Convert.convert(WholesalerContractExcelDTO.class, dto);
            wholesaler.setContractType(dto.getContractTypeDto().getName());
            wholesaler.setStallName(dto.getStallDtos().stream().map(p -> p.getName()).collect(Collectors.joining(",")));
            wholesaler.setCreateTime(DateUtils.format(dto.getCreateTime()));
            dtos.add(wholesaler);
        });

        ExcelUtils<WholesalerContractExcelDTO> utils = new ExcelUtils<>(WholesalerContractExcelDTO.class);
        utils.createWorkbook();
        try {
            utils.createSheet("合同信息", dtos);
        } catch (Exception e) {
            log.error("导出数据异常", e);
            throw new ServiceException("导出数据失败", e);
        }
        utils.downLoadExcel("合同信息", request, response);
    }
}

