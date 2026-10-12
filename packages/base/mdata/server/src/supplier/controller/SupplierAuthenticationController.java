package com.guhecloud.cube.biz.mdata.server.supplier.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.AuthenticationSourceEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierAuthentication;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierAuthenticatedEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierAuthenticationModifyParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierAuthenticationQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierAuthenticationService;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierService;
import com.guhecloud.cube.biz.mdata.server.supplier.vo.SupplierAuthenticationPageVO;
import com.guhecloud.cube.biz.mdata.server.supplier.vo.SupplierAuthenticationVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 批发商认证接口
 *
 * @author lzy
 * @date 2021/12/4
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplierAuthenticationController {

    private final SupplierService supplierService;
    private final SupplierAuthenticationService supplierAuthenticationService;

    private final FileClient fileClient;

    /**
     * 批发商认证信息新增
     * 用于小程序端
     *
     * @param param 新增参数
     */
    @PostMapping("/supplier-authentication")
    public void addSupplierAuthentication(@Valid @RequestBody SupplierAuthenticationModifyParam param) {
        param.setSource(AuthenticationSourceEnum.MP);
        param.setStatus(SupplierAuthenticatedEnum.AUDITED);
        this.supplierAuthenticationService.addSupplierAuthentication(param);
    }

    /**
     * 批发商认证信息新增
     * 用于pc端
     *
     * @param param 新增参数
     */
    @PostMapping("/pc/supplier-authentication")
    public void addSupplierAuthenticationForPC(@Valid @RequestBody SupplierAuthenticationModifyParam param) {
        param.setSource(AuthenticationSourceEnum.PC);
        param.setStatus(SupplierAuthenticatedEnum.CERTIFIED);
        this.supplierAuthenticationService.addSupplierAuthentication(param);
    }

    /**
     * 批发商认证信息审核
     *
     * @param id     认证主键
     * @param status 认证状态
     * @param remark 备注
     */
    @PutMapping("/supplier-authentication/{id}/audit")
    public void auditSupplierAuthentication(@PathVariable("id") Long id,
                                            @Valid @NotNull(message = "认证状态不能为空")
                                            @RequestParam(value = "status", defaultValue = "FAILED") SupplierAuthenticatedEnum status,
                                            @RequestParam(value = "remark", defaultValue = "") String remark
                                           ) {
        this.supplierAuthenticationService.auditSupplierAuthentication(id, status, remark);
    }

    /**
     * 批发商重新认证
     */
    @PutMapping("/supplier-authentication/reset")
    public void resetSupplierAuthentication() {
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        Supplier supplier = this.supplierService.getBySupplierTenant(supplierTenant);
        Assert.isNotFound(supplier, "不存在批发商信息!");

        // 修改认证信息
        SupplierAuthentication supplierAuthentication = this.supplierAuthenticationService.getActiveBySupplierId(supplier.getId(), supplier.getTenant());
        if (Objects.nonNull(supplierAuthentication)) {
            this.supplierAuthenticationService.resetSupplierAuthentication(supplierAuthentication);
        }
    }

    /**
     * 批发商认证信息详情
     *
     * @param id 认证主键
     *
     * @return 认证信息详情
     */
    @GetMapping("/supplier-authentication/{id}")
    public SupplierAuthenticationVO getById(@NotNull(message = "认证主键不能为空") @PathVariable("id") Long id) {
        SupplierAuthentication supplierAuthentication = this.supplierAuthenticationService.getById(id);
        Assert.isNotFound(supplierAuthentication, "不存在认证信息!");
        SupplierAuthenticationVO supplierAuthenticationVO = ObjectUtils.copying(supplierAuthentication, SupplierAuthenticationVO.class);

        // 构建主体资料（批发商信息）
        Supplier supplier = this.supplierService.getById(supplierAuthentication.getSupplierId());
        Assert.isNotFound(supplier, "不存在批发商信息!");
        supplierAuthenticationVO.setSupplierName(supplier.getName());
        supplierAuthenticationVO.setLiaison(supplier.getLiaison());
        supplierAuthenticationVO.setPhone(supplier.getPhone());

        // 构建认证资料
        // 获取文件信息
        this.setFileInfo(supplierAuthentication, supplierAuthenticationVO);
        return supplierAuthenticationVO;
    }

    /**
     * 获取批发商认证结果
     *
     * @return 认证信息
     */
    @GetMapping("/supplier-authentication/result")
    public SupplierAuthenticationVO getSupplierAuthenticationResult() {
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();

        Supplier supplier = this.supplierService.getBySupplierTenant(supplierTenant);
        Assert.isNotFound(supplier, "不存在批发商信息!");

        // 获取认证信息
        SupplierAuthentication supplierauthentication =
                this.supplierAuthenticationService.getActiveBySupplierId(supplier.getId(), supplier.getTenant());

        // 构建信息
        if (Objects.isNull(supplierauthentication)) {
            // 返回最近一次认证的信息

            List<SupplierAuthentication> lastSupplierAuthentications = this.supplierAuthenticationService.getLastBySupplierId(supplier.getId(), supplier.getTenant());
            if (!CollectionUtils.isEmpty(lastSupplierAuthentications)) {
                supplierauthentication = lastSupplierAuthentications.get(0);
            } else {
                supplierauthentication = new SupplierAuthentication();
                supplierauthentication.setStatus(SupplierAuthenticatedEnum.UNAUTHORIZED);
            }
        }
        SupplierAuthenticationVO authentication = ObjectUtils.copying(supplierauthentication, SupplierAuthenticationVO.class);
        authentication.setSupplierName(supplier.getName());
        authentication.setLiaison(supplier.getLiaison());
        authentication.setPhone(supplier.getPhone());
        this.setFileInfo(supplierauthentication, authentication);
        return authentication;
    }

    /**
     * 获取当前批发商下的认证信息状态
     *
     * @return 批发商信息
     */
    @GetMapping("/supplier-authentication/status")
    public SupplierAuthenticationVO getSupplierAuthenticationStatus() {
        // 获取当前批发商的租户编号
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Supplier supplier = this.supplierService.getBySupplierTenant(tenant);
        Assert.isNotFound(supplier, "不存在批发商信息!");

        // 获取对应批发商认证信息
        SupplierAuthentication supplierAuthentication = this.supplierAuthenticationService.getActiveBySupplierId(supplier.getId(), supplier.getTenant());
        if (Objects.isNull(supplierAuthentication)) {
            supplierAuthentication = new SupplierAuthentication();
            supplierAuthentication.setStatus(SupplierAuthenticatedEnum.UNAUTHORIZED);
        }

        // 构建信息
        // 获取文件信息
        SupplierAuthenticationVO supplierAuthenticationVO = ObjectUtils.copying(supplierAuthentication, SupplierAuthenticationVO.class);
        this.setFileInfo(supplierAuthentication, supplierAuthenticationVO);
        return supplierAuthenticationVO;
    }

    /**
     * 认证审核分页查询
     *
     * @param queryParam 查询参数
     *
     * @return 认证信息
     */
    @GetMapping("/supplier-authentication/page")
    public Pagination<SupplierAuthenticationPageVO> pageSupplierAuthentication(@Valid SupplierAuthenticationQueryParam queryParam) {
        // 先获取所有非失效认证信息
        queryParam.setTenant(SessionContextHolder.getRequester().getTenant());
        Page<SupplierAuthentication> page = this.supplierAuthenticationService.pageSupplierAuthentication(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        // 获取所有批发商信息
        Set<Long> supplierIds = page.getRecords().stream().map(SupplierAuthentication::getSupplierId).collect(Collectors.toSet());
        List<Supplier> suppliers = this.supplierService.listByIds(supplierIds);
        Map<Long, List<Supplier>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(Supplier::getId));

        // 构建信息
        List<SupplierAuthenticationPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), SupplierAuthenticationPageVO.class);
        pageVOS = pageVOS.stream().map(result -> {
            if (supplierMap.containsKey(result.getSupplierId())) {
                Supplier supplier = supplierMap.get(result.getSupplierId()).get(0);
                result.setSupplierName(supplier.getName());
                result.setLiaison(supplier.getLiaison());
                result.setPhone(supplier.getPhone());
            }
            return result;
        }).collect(Collectors.toList());

        return PagingUtils.convert(page, pageVOS);
    }

    private void setFileInfo(SupplierAuthentication supplierAuthentication, SupplierAuthenticationVO supplierAuthenticationVO) {
        List<Long> fileIds = Stream
                .of(supplierAuthentication.getLicenseId(), supplierAuthentication.getFrontIdentityCardId(), supplierAuthentication.getBackIdentityCardId())
                .collect(Collectors.toList());
        List<FileVO> files = this.fileClient.getByIds(fileIds);
        Map<Long, List<FileVO>> fileMap = files.stream().collect(Collectors.groupingBy(FileVO::getId));
        fileIds.forEach(fileId -> {
            if (fileMap.containsKey(fileId)) {
                FileVO file = fileMap.get(fileId).get(0);
                if (Objects.equals(fileId, supplierAuthentication.getLicenseId())) {
                    supplierAuthenticationVO.setLicenseFile(file);
                }
                if (Objects.equals(fileId, supplierAuthentication.getFrontIdentityCardId())) {
                    supplierAuthenticationVO.setFrontIdentityCardFile(file);
                }
                if (Objects.equals(fileId, supplierAuthentication.getBackIdentityCardId())) {
                    supplierAuthenticationVO.setBackIdentityCardFile(file);
                }
            }
        });
    }
}
