package com.guhecloud.cube.deliver.project.merchant.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.BatchDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerAuthDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerAuthInfoDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerAuthQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerAuth;
import com.guhecloud.cube.deliver.project.merchant.service.SpaceService;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerAuthService;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2022-01-08
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerAuthController {

    private final WholesalerAuthService service;
    private final SpaceService spaceService;

    @GetMapping("/wholesaler-auth")
    public WholesalerAuthDTO getAuth(String projectId, String projectName, String userId) {
        return service.getAuth(projectId, projectName, userId);
    }

    @GetMapping("/wholesaler-auth/page")
    public PageResponse<WholesalerAuthDTO> getAuthDtoList(WholesalerAuthQueryDTO queryDto, PageRequest pageRequest) {
        Page<WholesalerAuth> page = service.lambdaQuery()
                .eq(WholesalerAuth::getProjectId, queryDto.getProjectId())
                .and(StringUtils.isNotEmpty(queryDto.getSearch()), q -> q
                        .like(WholesalerAuth::getRealName, queryDto.getSearch())
                        .or()
                        .like(WholesalerAuth::getCreateBy, queryDto.getSearch())
                        .or()
                        .like(WholesalerAuth::getCompanyName, queryDto.getSearch())
                        .or()
                        .like(WholesalerAuth::getEnterpriseName, queryDto.getSearch())
                    )
                .eq(null != queryDto.getStatus(), WholesalerAuth::getStatus, queryDto.getStatus())
                .between(null != queryDto.getBeginTime() &&
                         null != queryDto.getEndTime(), WholesalerAuth::getCreateTime, queryDto.getBeginTime(), queryDto.getEndTime())
                .eq(StrUtil.isNotBlank(queryDto.getCompanyId()), WholesalerAuth::getCompanyId, queryDto.getCompanyId())
                .like(StrUtil.isNotBlank(queryDto.getStallId()), WholesalerAuth::getStallIds, queryDto.getStallId())
                .page(pageRequest.page());

        List<WholesalerAuthDTO> dtos = new WholesalerAuthDTO(page.getRecords()).list();
        // 扩展档位的名称
        service.extendStallNames(dtos);
        return new PageResponse<>(dtos, page.getTotal(), page.getPages());
    }

    @GetMapping("/wholesaler-auth/{id}")
    public WholesalerAuthDTO getAuthDto(@PathVariable("id") String id) {
        WholesalerAuth auth = service.lambdaQuery().eq(WholesalerAuth::getId, id).one();
        if (auth == null) {
            throw new ServiceException("没有找到对应的实名认证详情");
        }
        return service.getAuthDto(id);
    }

    @GetMapping("/wholesaler-auth/status/page")
    public PageResponse<WholesalerAuthDTO> getAuthDtoListByStatus(WholesalerAuthQueryDTO queryDto, PageRequest pageRequest) {
        //筛选条件  时间，人，状态
        List<WholesalerAuth> list = service.lambdaQuery()
                .eq(WholesalerAuth::getStatus, queryDto.getStatus())
                .like(StrUtil.isNotBlank(queryDto.getCreateBy()), WholesalerAuth::getCreateBy, queryDto.getCreateBy())
                .between(queryDto.getBeginTime() != null && queryDto.getEndTime() != null,
                         WholesalerAuth::getCreateTime, queryDto.getBeginTime(), queryDto.getEndTime())
                .list();
        List<WholesalerAuthDTO> dtos = new WholesalerAuthDTO(list).list();
        return PageResponse.doPage(dtos, pageRequest);
    }

    @GetMapping("/wholesaler/verifyCode")
    public String sendVerifyCode(String projectId, String userId, String mobile) {
        return service.sendVerifyCode(projectId, userId, mobile);
    }

    @PostMapping("/wholesaler-auth")
    public String saveOrUpdate(@Valid @RequestBody WholesalerAuthDTO dto) {
        return service.saveOrUpdate(dto);
    }

    @PutMapping("/wholesaler/status/ids")
    public void updateStatusByIds(@RequestBody BatchDTO dto, LoginUser user) {
        service.batchPass(dto, user);
    }

    @PutMapping("/wholesaler/status/{id}")
    public void updateStatusById(@PathVariable("id") String id, LoginUser user) {
        service.pass(id, user);
    }

    @PutMapping("/wholesaler/reject/{id}")
    public void updateRejectReasonById(@PathVariable("id") String id, String rejectReason, LoginUser user) {
        service.doNotPass(id, rejectReason, user);
    }

    @GetMapping("/wholesaler-auth/company")
    public WholesalerAuthInfoDTO getAuthByStallId(String projectId, String stallId, Long userId) {
        return service.getAuthByStallId(projectId, stallId, userId);
    }
}

