package com.guhecloud.cube.biz.mdata.server.merchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.goods.service.GoodsCategoryService;
import com.guhecloud.cube.biz.mdata.server.merchant.dto.AuthenticationDTO;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.Authentication;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.Merchant;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.SellerAuthentication;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import com.guhecloud.cube.biz.mdata.server.merchant.param.AuthenticationCreateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.AuthenticationQueryParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.AuthenticationReviewParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.AuthenticationUpdateCreate;
import com.guhecloud.cube.biz.mdata.server.merchant.service.AuthenticationService;
import com.guhecloud.cube.biz.mdata.server.merchant.service.MerchantService;
import com.guhecloud.cube.biz.mdata.server.merchant.service.SellerAuthenticationService;
import com.guhecloud.cube.biz.mdata.server.merchant.vo.AuthenticationPageVO;
import com.guhecloud.cube.biz.mdata.server.merchant.vo.AuthenticationVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商户认证接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final MerchantService merchantService;
    private final SellerAuthenticationService sellerAuthenticationService;
    private final GoodsCategoryService goodsCategoryService;

    private final FileClient fileClient;

    /**
     * 商户认证新增
     *
     * @param param 商户认证新增参数
     */
    @PostMapping("/authentication")
    public void save(@Valid @RequestBody AuthenticationCreateParam param) {
        this.authenticationService.save(param);
    }

    /**
     * 商户认证编辑
     *
     * @param param 商户认证编辑参数
     */
    @PutMapping("/authentication")
    public void update(@Valid @RequestBody AuthenticationUpdateCreate param) {
        this.authenticationService.update(param);
    }

    /**
     * PC端商户认证查询
     *
     * @param id 商户认证ID
     *
     * @return 商户认证信息
     */
    @GetMapping("/authentication/{id}")
    public AuthenticationVO getById(@NotNull(message = "认证主键不能为空") @Min(value = 1, message = "认证主键不能小于1")
                                    @PathVariable("id") Long id
                                   ) {
        // 获取认证详情
        Authentication authentication = this.authenticationService.getById(id);
        AuthenticationVO authenticationVO = ObjectUtils.copying(authentication, AuthenticationVO.class);
        // 获取商户信息
        Merchant merchant = this.merchantService.getById(authentication.getMerchantId());
        // 构建认证信息
        authenticationVO.setMerchantName(merchant.getName());
        authenticationVO.setLiaison(merchant.getLiaison());
        authenticationVO.setPhone(merchant.getPhone());

        // 构建认证图片信息
        authenticationVO.setFileVOS(this.fileClient.getByBusinessKey(authentication.getId()));

        // 获取卖家认证详情
        SellerAuthentication sellerAuthentication = this.sellerAuthenticationService.getByMerchantId(merchant.getId());
        if (Objects.isNull(sellerAuthentication)) {
            return authenticationVO;
        }
        // 构建卖家认证资料
        List<String> goodsCategoryIds = Arrays.asList(sellerAuthentication.getGoodsCategory().split(","));
        List<GoodsCategory> goodsCategories = this.goodsCategoryService.getByIds(goodsCategoryIds);
        authenticationVO.setGoodsCategories(goodsCategories);
        authenticationVO.setSellerSubmitTime(sellerAuthentication.getSubmitTime());
        authenticationVO.setSellerStatus(sellerAuthentication.getStatus());
        authenticationVO.setSellerReason(sellerAuthentication.getReason());
        return authenticationVO;
    }

    /**
     * PC端商户认证分页查询
     *
     * @param queryParam 商户认证查询参数
     *
     * @return 商户认证分页集合
     */
    @GetMapping("/authentication")
    public Pagination<AuthenticationPageVO> page(@Valid AuthenticationQueryParam queryParam) {
        // 获取商户认证信息
        Page<AuthenticationDTO> page = this.authenticationService.page(queryParam);
        List<AuthenticationPageVO> authentications = ObjectUtils.copying(page.getRecords(), AuthenticationPageVO.class);
        if (CollectionUtils.isEmpty(authentications)) {
            return PagingUtils.convert(page, authentications);
        }
        // 根据商户主键获取商户信息
        List<Long> merchantIds =
                page.getRecords().stream().map(AuthenticationDTO::getMerchantId).collect(Collectors.toList());
        List<Merchant> merchants = this.merchantService.listMerchantByIds(merchantIds);
        Map<Long, List<Merchant>> merchantMap = merchants.stream().collect(Collectors.groupingBy(Merchant::getId));

        // 获取卖家认证信息
        List<SellerAuthentication> sellerAuthentications = this.sellerAuthenticationService.getByMerchantIds(merchantIds);
        Map<Long, List<SellerAuthentication>> sellerAuthenticationMap = sellerAuthentications.stream()
                .collect(Collectors.groupingBy(SellerAuthentication::getMerchantId));

        // 构建商户信息（包括商户名称、联系人、联系手机号）
        for (AuthenticationPageVO authentication : authentications) {
            if (!MapUtils.isEmpty(sellerAuthenticationMap)) {
                authentication.setSellerStatus(!sellerAuthenticationMap.containsKey(authentication.getMerchantId()) ?
                                               MerchantEnum.AUTH_UNAUTHORIZED : sellerAuthenticationMap.get(authentication.getMerchantId())
                                                       .get(0).getStatus());
                authentication.setSubmitTime(!sellerAuthenticationMap.containsKey(authentication.getMerchantId())
                                             ? authentication.getSubmitTime()
                                             : sellerAuthenticationMap.get(authentication.getMerchantId()).get(0).getSubmitTime());
            }
            if (!MapUtils.isEmpty(merchantMap) && merchantMap.containsKey(authentication.getMerchantId())) {
                Merchant merchant = merchantMap.get(authentication.getMerchantId()).get(0);
                authentication.setLiaison(merchant.getLiaison());
                authentication.setPhone(merchant.getPhone());
                authentication.setMerchantName(merchant.getName());
            }
        }
        return PagingUtils.convert(page, authentications);
    }

    /**
     * 商户认证审核结果
     *
     * @return 商户认证结果
     */
    @GetMapping("/authentication/review/result")
    public AuthenticationVO getReviewResult() {
        // 获取当前用户信息
        Principal principal = SecurityContextHolder.getPrincipal();
        // 根据用户主键获取认证信息
        Authentication authentication = this.authenticationService.getByUserId(principal.getId());
        Assert.isNotFound(authentication, "不存在认证信息");
        // 判断认证状态
        AuthenticationVO authenticationVO = ObjectUtils.copying(authentication, AuthenticationVO.class);
        // 获取图片信息
        authenticationVO.setFileVOS(this.fileClient.getByBusinessKey(authentication.getId()));
        return authenticationVO;
    }

    /**
     * 商户认证审核
     *
     * @param param 商户认证审核参数
     */
    @PutMapping("/authentication/review")
    public void review(@Valid @RequestBody AuthenticationReviewParam param) {
        this.authenticationService.review(param);
    }

}
