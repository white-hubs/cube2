package com.guhecloud.cube.biz.mdata.server.merchant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.merchant.dto.AuthenticationDTO;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.Authentication;
import com.guhecloud.cube.biz.mdata.server.merchant.param.AuthenticationCreateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.AuthenticationQueryParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.AuthenticationReviewParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.AuthenticationUpdateCreate;

import java.util.List;

public interface AuthenticationService extends IService<Authentication> {
    /**
     * 商户认证信息新增
     *
     * @param param 认证新增参数
     */
    void save(AuthenticationCreateParam param);

    /**
     * 商户认证编辑
     *
     * @param param
     */
    void update(AuthenticationUpdateCreate param);

    /**
     * 根据认证主键查询认证信息
     *
     * @param id 认证主键
     *
     * @return 认证信息
     */
    Authentication getById(Long id);

    /**
     * 根据用户主键获取认证信息
     *
     * @param userId 用户主键
     *
     * @return 认证信息
     */
    Authentication getByUserId(Long userId);

    /**
     * 根据商户主键查询商户信息
     *
     * @param merchantId 商户主键
     *
     * @return 认证信息
     */
    Authentication getByMerchantId(Long merchantId);

    /**
     * 根据商户主键集合查询认证信息
     *
     * @param merchantIds 商户主键集合
     *
     * @return 认证信息
     */
    Authentication getByMerchantIds(List<Long> merchantIds);

    /**
     * 获取认证分页信息
     *
     * @param queryParam 查询参数
     *
     * @return 认证信息集合
     */
    Page<AuthenticationDTO> page(AuthenticationQueryParam queryParam);

    /**
     * 认证审核
     *
     * @param param 认证审核参数
     */
    void review(AuthenticationReviewParam param);
}
