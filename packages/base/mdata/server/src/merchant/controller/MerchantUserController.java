package com.guhecloud.cube.biz.mdata.server.merchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.MerchantUser;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantUserCreateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantUserQueryParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantUserUpdateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.service.MerchantUserService;
import com.guhecloud.cube.biz.mdata.server.merchant.vo.MerchantUserPageVO;
import com.guhecloud.cube.biz.mdata.server.merchant.vo.MerchantUserVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商家用户接口
 *
 * @author lzy
 * @date 2021/10/20
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantUserController {

    private final MerchantUserService merchantUserService;

    /**
     * 商家用户新增（添加经营者、司机）
     *
     * @param param 新增参数
     */
    @PostMapping("/merchant-user")
    public void save(@Valid @RequestBody MerchantUserCreateParam param) {
        this.merchantUserService.save(param);
    }

    /**
     * 商家用户编辑（编辑经营者、司机）
     *
     * @param param 更新参数
     */
    @PutMapping("/merchant-user")
    public void update(@Valid @RequestBody MerchantUserUpdateParam param) {
        this.merchantUserService.update(param);
    }

    /**
     * 商家用户详情
     *
     * @param id 商家用户主键
     *
     * @return 商家用户信息
     */
    @GetMapping("/merchant-user/{id}")
    public MerchantUserVO getById(@PathVariable("id") Long id) {
        MerchantUser merchantUser = this.merchantUserService.getById(id);
        MerchantUserVO merchantUserVO = ObjectUtils.copying(merchantUser, MerchantUserVO.class);
        merchantUserVO.setStallNames(Collections.singletonList(merchantUser.getMerchantStallName()));
        return merchantUserVO;
    }

    /**
     * 获取该商户的用户权限
     *
     * @param merchantId 商户主键
     *
     * @return 商户用户信息
     */
    @GetMapping("/merchant-user/{merchantId}/user")
    public MerchantUserVO getByMerchantId(@PathVariable("merchantId") Long merchantId) {
        MerchantUser merchantUser = this.merchantUserService.getMerchantUser(merchantId, SecurityContextHolder.getPrincipal().getId());
        Assert.isNotFound(merchantUser, "用户不存在于该商户中!");
        return ObjectUtils.copying(merchantUser, MerchantUserVO.class);
    }

    /**
     * 商家用户分页
     *
     * @param queryParam 查询参数
     *
     * @return 商家用户分页信息
     */
    @GetMapping("/merchant-user/page")
    public Pagination<MerchantUserPageVO> page(@Valid MerchantUserQueryParam queryParam) {
        Page<MerchantUser> page = this.merchantUserService.page(queryParam);
        List<MerchantUserPageVO> records = ObjectUtils.copying(page.getRecords(), MerchantUserPageVO.class);
        records = records.stream().map(record -> {
            record.setStallNames(Collections.singletonList(record.getMerchantStallName()));
            return record;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, records);
    }

}
