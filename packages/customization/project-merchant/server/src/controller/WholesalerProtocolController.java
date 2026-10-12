package com.guhecloud.cube.deliver.project.merchant.controller;

import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerProQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerProtocolAppDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerProtocolDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerProtocolVerCodeDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerProtocol;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerProtocolService;

import com.guhecloud.platform.base.util.StringUtils;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
public class WholesalerProtocolController {

    private final WholesalerProtocolService service;

    @GetMapping("/wholesaler-protocol/page")
    public PageResponse<WholesalerProtocolDTO> getProtocolsOnPc(
            WholesalerProQueryDTO queryDTO, PageRequest pageRequest) {
        List<WholesalerProtocolDTO> dtos = service.getProtocolsOnPc(queryDTO);
        dtos.sort(Comparator.comparing(WholesalerProtocolDTO::getCreateTime).reversed());
        return PageResponse.doPage(dtos, pageRequest);
    }

    @GetMapping("/wholesaler-protocol/company/page")
    public PageResponse<WholesalerProtocolDTO> getProtocolsByCompanyId(String projectId, String stallId, String search,
                                                                       String companyId, PageRequest pageRequest
                                                                      ) {
        List<WholesalerProtocolDTO> dtos = service.getProtocolsByCompanyIdOrStallId(projectId, companyId, stallId);
        if (StringUtils.isNotEmpty(search)) {
            dtos = dtos.stream().filter(dto -> dto.getName().contains(search)).collect(Collectors.toList());
        }
        dtos.sort(Comparator.comparing(WholesalerProtocolDTO::getCreateTime).reversed());
        return PageResponse.doPage(dtos, pageRequest);
    }

    @GetMapping("/wholesaler-protocol/notice/{id}")
    public void sendNotice(String projectId, @PathVariable("id") String id) {
        service.sendNotice(projectId, id);
    }

    @PostMapping("/wholesaler-protocol/notices")
    public void sendNotices(String projectId) {
        List<WholesalerProtocol> protocols = service.lambdaQuery()
                .eq(WholesalerProtocol::getProjectId, projectId).list();
        protocols.forEach(protocol -> service.sendNotice(projectId, protocol.getId()));
    }

    @GetMapping("/wholesaler-protocol/app/stall/page")
    public PageResponse<WholesalerProtocolAppDTO> getProtocolsOnApp(String stallId, PageRequest pageRequest) {
        return PageResponse.doPage(service.getProtocolsOnApp(stallId), pageRequest);
    }

    @GetMapping("/wholesaler-protocol/{stallId}/{protocolId}")
    public WholesalerProtocolAppDTO getProtocolOnAppById(@PathVariable("stallId") String stallId,
                                                         @PathVariable("protocolId") String protocolId
                                                        ) {
        return service.getProtocolOnAppById(stallId, protocolId);
    }

    @GetMapping("/wholesaler-prtocol/{id}")
    public WholesalerProtocolDTO getProtocolById(String projectId, @PathVariable("id") String id) {
        return service.getProtocolById(projectId, id);
    }

    @GetMapping("/wholesaler-protocol/verify-code")
    public String sendVerifyCode(String projectId, String mobile, LoginUser user) {
        return service.sendVerifyCode(projectId, mobile, user);
    }

    @PostMapping("/wholesaler-protocol")
    public String saveOrUpdate(@Valid @RequestBody WholesalerProtocolDTO dto) {
        return service.saveOrUpdate(dto);
    }

    @PutMapping("/wholesaler-protocol/sign")
    public void signProtocol(@Valid @RequestBody WholesalerProtocolVerCodeDTO dto, LoginUser user) {
        service.signProtocol(dto, user);
    }

    @DeleteMapping("/wholesaler-protocol/{id}")
    public void remove(@PathVariable("id") String id) {
        service.remove(id);
    }
}

