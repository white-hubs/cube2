package com.guhecloud.cube.biz.mdata.server.supplier.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallService;
import com.guhecloud.cube.biz.mdata.server.supplier.dto.SupplierDTO;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierUser;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierUserCreateParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierUserQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierUserUpdateParam;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierService;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierUserService;
import com.guhecloud.cube.biz.mdata.server.supplier.vo.SupplierUserPageVO;
import com.guhecloud.cube.biz.mdata.server.supplier.vo.SupplierUserVO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lzy
 * @date 2021/12/14
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplierUserController {
    private final SupplierUserService supplierUserService;
    private final SupplierService supplierService;
    private final StallService stallService;

    /**
     * 商家用户新增（添加经营者、司机）
     *
     * @param param 新增参数
     */
    @PostMapping("/supplier-user")
    public void save(@Valid @RequestBody SupplierUserCreateParam param) {
        this.supplierUserService.save(param);
    }

    /**
     * 商家用户编辑（编辑经营者、司机）
     *
     * @param param 更新参数
     */
    @PutMapping("/supplier-user")
    public void update(@Valid @RequestBody SupplierUserUpdateParam param) {
        this.supplierUserService.update(param);
    }

    /**
     * 商家用户详情
     *
     * @param id 商家用户主键
     *
     * @return 商家用户信息
     */
    @GetMapping("/supplier-user/{id}")
    public SupplierUserVO getById(@PathVariable("id") Long id) {
        SupplierUser supplierUser = this.supplierUserService.getById(id);
        SupplierUserVO supplierUserVO = ObjectUtils.copying(supplierUser, SupplierUserVO.class);
        supplierUserVO.setStallNames(Collections.singletonList(supplierUser.getSupplierStallName()));
        return supplierUserVO;
    }

    /**
     * 删除
     *
     * @param id 主键
     */
    @DeleteMapping("/supplier-user/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.supplierUserService.remove(id);
    }

    /**
     * 获取该商户的用户权限
     *
     * @param supplierId 商户主键
     *
     * @return 商户用户信息
     */
    @GetMapping("/supplier-user/{supplierId}/user")
    public List<SupplierUserVO> getBySupplierId(@PathVariable("supplierId") Long supplierId) {
        List<SupplierUser> supplierUsers = this.supplierUserService.listSupplierUser(supplierId, SecurityContextHolder.getPrincipal()
                .getId());
        Assert.isNotFound(CollectionUtils.isEmpty(supplierUsers), "用户不存在于该商户中!");
        return ObjectUtils.copying(supplierUsers, SupplierUserVO.class);
    }

    /**
     * 商家用户分页
     *
     * @param queryParam 查询参数
     *
     * @return 商家用户分页信息
     */
    @GetMapping("/supplier-user/page")
    public Pagination<SupplierUserPageVO> page(@Valid SupplierUserQueryParam queryParam) {
        Page<SupplierUser> page = this.supplierUserService.page(queryParam);
        List<SupplierUserPageVO> records = ObjectUtils.copying(page.getRecords(), SupplierUserPageVO.class);
        records = records.stream().map(record -> {
            if (StringUtils.isNotEmpty(record.getSupplierStallId())) {
                SupplierUserPageVO.Stall stall = new SupplierUserPageVO.Stall();
                stall.setId(Long.parseLong(record.getSupplierStallId()));
                stall.setName(record.getSupplierStallName());
                record.setStalls(Stream.of(stall).collect(Collectors.toList()));
            }
            return record;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, records);
    }

    /**
     * 通过供应商ID 和 用户的电话号码查询
     *
     * @param supplierId
     * @param phone
     *
     * @return
     */
    @GetMapping("/supplier-user/inner/info")
    public SupplierUserVO getBySupplierIdAndPhone(@RequestParam("supplierId") Long supplierId,
                                                  @RequestParam("phone") String phone,
                                                  @RequestParam("supplierCategory") String supplierCategory
                                                 ) {
        SupplierUser one = this.supplierUserService.getOne(Wrappers.<SupplierUser>lambdaQuery()
                                                                   .eq(SupplierUser::getSupplierId, supplierId)
                                                                   .eq(SupplierUser::getSupplierCategoryPhone, phone)
                                                                   .eq(SupplierUser::getSupplierCategory, supplierCategory));
        return ObjectUtils.copying(one, SupplierUserVO.class);
    }

    @GetMapping("/supplier-user/inner/auth-wallet-pay")
    public Boolean checkUserWithWalletPay(@RequestParam("supplierId") Long supplierId,
                                          @RequestParam("phone") String phone
                                         ) {
        List<SupplierUser> datas = this.supplierUserService.list(Wrappers.<SupplierUser>lambdaQuery()
                                                                         .eq(SupplierUser::getSupplierId, supplierId)
                                                                         .eq(SupplierUser::getSupplierCategoryPhone, phone)
                                                                         .eq(SupplierUser::getAuthWalletPay, true));
        return !CollectionUtils.isEmpty(datas);
    }

    @GetMapping("/supplier-user/{userId}/list")
    public List<SupplierDTO> getBySupplierUser(@RequestParam("userId") Long userId) {
        List<SupplierUser> supplierUserList = this.supplierUserService.list(Wrappers.<SupplierUser>lambdaQuery()
                                                                                    .eq(SupplierUser::getUserId, userId));
        if (!CollectionUtils.isEmpty(supplierUserList)) {
            List<Long> supplierIdList = supplierUserList.stream().map(s -> s.getSupplierId()).distinct().collect(Collectors.toList());
            List<Supplier> suppliers = supplierService.listByIds(supplierIdList);
            return ObjectUtils.copying(suppliers, SupplierDTO.class);
        }
        return Collections.emptyList();
    }

}
