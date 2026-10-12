package com.guhecloud.cube.biz.mdata.server.merchant.controller;

import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.goods.service.GoodsCategoryService;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.Merchant;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.SellerAuthentication;
import com.guhecloud.cube.biz.mdata.server.merchant.param.SellerAuthenticationCreateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.service.MerchantService;
import com.guhecloud.cube.biz.mdata.server.merchant.service.SellerAuthenticationService;
import com.guhecloud.cube.biz.mdata.server.merchant.vo.SellerAuthenticationVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 卖家认证接口
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SellerAuthenticationController {

    private final SellerAuthenticationService sellerAuthenticationService;
    private final MerchantService merchantService;
    private final GoodsCategoryService goodsCategoryService;

    /**
     * 卖家认证新增
     *
     * @param param 卖家新增信息
     */
    @PostMapping("/seller-authentication")
    public void save(@RequestBody SellerAuthenticationCreateParam param) {
        this.sellerAuthenticationService.save(param);
    }

    /**
     * 卖家重新认证
     */
    @GetMapping("/seller-authentication/reset")
    public void sellerReset() {
        this.sellerAuthenticationService.sellerReset();
    }

    /**
     * 卖家认证结果
     *
     * @return 卖家认证结果信息
     */
    @GetMapping("/seller-authentication/review/result")
    public SellerAuthenticationVO getReviewResult() {
        Principal principal = SecurityContextHolder.getPrincipal();
        Merchant merchant = this.merchantService.getByUserId(principal.getId());
        Assert.isNotFound(merchant, "不存在商户资料信息");
        SellerAuthentication sellerAuthentication = this.sellerAuthenticationService.getByMerchantId(merchant.getId());
        SellerAuthenticationVO sellerAuthenticationVO = ObjectUtils.copying(sellerAuthentication,
                                                                            SellerAuthenticationVO.class);
        // 扩展品类
        List<String> goodsCategoryIds = Arrays.asList(sellerAuthentication.getGoodsCategory().split(","));
        List<GoodsCategory> goodsCategories = this.goodsCategoryService.getByIds(goodsCategoryIds);
        sellerAuthenticationVO.setGoodsCategories(goodsCategories);
        return sellerAuthenticationVO;
    }
}