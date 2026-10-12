package com.guhecloud.cube.biz.mdata.server.purchaser.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.purchaser.entity.Purchaser;
import com.guhecloud.cube.biz.mdata.server.purchaser.entity.PurchaserAuthentication;
import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.PurchaserAuthenticationEnum;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserAuthenticationAuditParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserAuthenticationModifyParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserAuthenticationQueryParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.service.PurchaserAuthenticationService;
import com.guhecloud.cube.biz.mdata.server.purchaser.service.PurchaserService;
import com.guhecloud.cube.biz.mdata.server.purchaser.vo.PurchaserAuthenticationVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lzy
 * @date 2021/12/24
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PurchaserAuthenticationController {

    private final PurchaserAuthenticationService purchaserAuthenticationService;
    private final PurchaserService purchaserService;

    private final FileClient fileClient;
    private final UserClient userClient;

    /**
     * 采购商认证新增
     * 用于小程序
     *
     * @param param 新增参数
     */
    @PostMapping("/purchaser-authentication")
    public void addPurchaserAuthentication(@Valid @RequestBody PurchaserAuthenticationModifyParam param) {
        this.purchaserAuthenticationService.addPurchaserAuthentication(param);
    }

    /**
     * 采购商认证新增
     * 用于PC端
     *
     * @param purchaserId
     * @param param
     */
    @PostMapping("/purchaser-authentication/{purchaserId}")
    public void addPurchaserAuthentication(@PathVariable("purchaserId") Long purchaserId,
                                           @Valid @RequestBody PurchaserAuthenticationModifyParam param
                                          ) {
        this.purchaserAuthenticationService.addPurchaserAuthenticationForPC(purchaserId, param);
    }

    /**
     * 采购商重新认证
     *
     * @param id 认证主键
     */
    @PutMapping("/purchaser-authentication/{id}/reset")
    public void resetPurchaserAuthentication(@PathVariable("id") Long id) {
        this.purchaserAuthenticationService.resetPurchaserAuthentication(id);
    }

    @GetMapping("/purchaser-authentication/{id}")
    public PurchaserAuthenticationVO getById(@PathVariable("id") Long id) {
        // 构建信息
        PurchaserAuthenticationVO authentication = new PurchaserAuthenticationVO();
        Purchaser purchaser = this.purchaserService.getById(id);
        Assert.isNotFound(purchaser, "不存在采购商信息!");

        UserDTO user = this.userClient.getUser(purchaser.getUserId());
        authentication.setLiaison(Objects.isNull(user) ? "" : user.getName());
        authentication.setPhone(Objects.isNull(user) ? "" : user.getMobile());

        //查询认证信息
        PurchaserAuthentication byPurchaser = this.purchaserAuthenticationService.getByPurchaser(id, purchaser.getTenant());
        if (byPurchaser != null) {
            ObjectUtils.copying(byPurchaser, authentication);
            this.setFileInfo(byPurchaser, authentication);
        }
        authentication.setId(id);//采购商ID
        authentication.setPurchaserId(id);
        return authentication;
    }

    /**
     * 采购商认证审核
     *
     * @param id 认证主键
     */
    @PutMapping("/purchaser-authentication/{id}/audit")
    public void auditPurchaserAuthentication(@PathVariable("id") Long id,
                                             @RequestBody PurchaserAuthenticationAuditParam param
                                            ) {
        this.purchaserAuthenticationService.auditPurchaserAuthentication(id, param.getStatus(), param.getRemark());
    }

    /**
     * 采购商获取认证状态
     *
     * @return 认证状态信息
     */
    @GetMapping("/purchaser-authentication/status")
    public PurchaserAuthenticationVO getAuthenticationStatus() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Purchaser purchaser = this.purchaserService.getPurchaser(userId, tenant);
        Assert.isNotFound(purchaser, "不存在采购商信息!");

        PurchaserAuthentication purchaserAuthentication =
                this.purchaserAuthenticationService.getByPurchaser(purchaser.getId(), tenant);
        PurchaserAuthenticationVO record = ObjectUtils.copying(purchaserAuthentication, PurchaserAuthenticationVO.class);
        if (Objects.isNull(record)) {
            record = new PurchaserAuthenticationVO();
            record.setPurchaserId(purchaser.getId());
            record.setStatus(PurchaserAuthenticationEnum.UNAUTHORIZED);
        }

        return record;
    }

    /**
     * 采购商认证查询
     *
     * @param queryParam 查询参数
     *
     * @return 认证信息
     */
    @GetMapping("/purchaser-authentication/page")
    public Pagination<PurchaserAuthenticationVO> pageAuthentication(@Valid PurchaserAuthenticationQueryParam queryParam) {
        queryParam.setTenant(SessionContextHolder.getRequester().getTenant());
        Page<PurchaserAuthenticationVO> page = this.purchaserService.pageForPurchaser(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        this.buildAuthentication(page.getRecords());
        return PagingUtils.convert(page);
    }

    /**
     * 采购商认证查询
     * 排除未认证的采购商信息
     *
     * @param queryParam 查询参数
     *
     * @return 认证信息
     */
    @GetMapping("/purchaser-authentication/verify/page")
    public Pagination<PurchaserAuthenticationVO> pageAuthenticationForVerify(@Valid PurchaserAuthenticationQueryParam queryParam) {
        queryParam.setTenant(SessionContextHolder.getRequester().getTenant());
        Page<PurchaserAuthenticationVO> page = this.purchaserService.pageForPurchaserVerify(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        this.buildAuthentication(page.getRecords());
        return PagingUtils.convert(page);
    }

    private void buildAuthentication(List<PurchaserAuthenticationVO> datas) {
        Set<Long> userIds = datas.stream().map(PurchaserAuthenticationVO::getUserId).collect(Collectors.toSet());
        List<UserDTO> users = this.userClient.listUserByIds(userIds);
        Map<Long, UserDTO> userMap = users.stream().collect(Collectors.toMap(UserDTO::getId, u -> u));
        datas.stream().forEach(authentication -> {
            if (userMap.containsKey(authentication.getUserId())) {
                UserDTO user = userMap.get(authentication.getUserId());
                authentication.setLiaison(user.getName());
                authentication.setPhone(user.getMobile());
                authentication.setDelegateName(
                        StringUtils.isEmpty(authentication.getDelegateName()) ? user.getName() : authentication.getDelegateName());
                authentication.setStatus(
                        authentication.getStatus() == null ? PurchaserAuthenticationEnum.UNAUTHORIZED : authentication.getStatus());
                authentication.setId(authentication.getPurchaserId());
            }
        });
    }

    /**
     * 采购商认证结果
     *
     * @return
     */
    @GetMapping("/purchaser-authentication/result")
    public PurchaserAuthenticationVO getAuthenticationResult() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Purchaser purchaser = this.purchaserService.getPurchaser(userId, tenant);
        Assert.isNotFound(purchaser, "不存在采购商信息!");

        // 获取认证信息
        PurchaserAuthentication purchaserAuthentication =
                this.purchaserAuthenticationService.getByPurchaser(purchaser.getId(), tenant);
        if (Objects.isNull(purchaserAuthentication)) {
            // 返回最后一次认证信息
            List<PurchaserAuthentication> lastPurchaserAuthentication =
                    this.purchaserAuthenticationService.getLastByPurchaser(purchaser.getId(), tenant);
            if (!CollectionUtils.isEmpty(lastPurchaserAuthentication)) {
                purchaserAuthentication = lastPurchaserAuthentication.get(0);
            } else {
                purchaserAuthentication = new PurchaserAuthentication();
                purchaserAuthentication.setStatus(PurchaserAuthenticationEnum.UNAUTHORIZED);
            }
        }
        PurchaserAuthenticationVO authentication =
                ObjectUtils.copying(purchaserAuthentication, PurchaserAuthenticationVO.class);
        this.setFileInfo(purchaserAuthentication, authentication);
        return authentication;
    }

    private void setFileInfo(PurchaserAuthentication purchaserAuthentication, PurchaserAuthenticationVO purchaserAuthenticationVO) {
        List<Long> fileIds = Stream
                .of(purchaserAuthentication.getLicenseId(), purchaserAuthentication.getFrontIdentityCardId(), purchaserAuthentication.getBackIdentityCardId())
                .collect(Collectors.toList());
        List<FileVO> files = this.fileClient.getByIds(fileIds);
        Map<Long, List<FileVO>> fileMap = files.stream().collect(Collectors.groupingBy(FileVO::getId));
        fileIds.forEach(fileId -> {
            if (fileMap.containsKey(fileId)) {
                FileVO file = fileMap.get(fileId).get(0);
                if (Objects.equals(fileId, purchaserAuthentication.getLicenseId())) {
                    purchaserAuthenticationVO.setLicenseFile(file);
                }
                if (Objects.equals(fileId, purchaserAuthentication.getFrontIdentityCardId())) {
                    purchaserAuthenticationVO.setFrontIdentityCardFile(file);
                }
                if (Objects.equals(fileId, purchaserAuthentication.getBackIdentityCardId())) {
                    purchaserAuthenticationVO.setBackIdentityCardFile(file);
                }
            }
        });
    }

}
