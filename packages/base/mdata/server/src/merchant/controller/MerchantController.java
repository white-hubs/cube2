package com.guhecloud.cube.biz.mdata.server.merchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.goods.service.GoodsCategoryService;
import com.guhecloud.cube.biz.mdata.server.merchant.dto.MerchantDTO;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.Authentication;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.Merchant;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.MerchantUser;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.SellerAuthentication;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantCreateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantQueryParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantStatQueryParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantUpdateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.service.AuthenticationService;
import com.guhecloud.cube.biz.mdata.server.merchant.service.MerchantService;
import com.guhecloud.cube.biz.mdata.server.merchant.service.MerchantUserService;
import com.guhecloud.cube.biz.mdata.server.merchant.service.SellerAuthenticationService;
import com.guhecloud.cube.biz.mdata.server.merchant.vo.MerchantAppVO;
import com.guhecloud.cube.biz.mdata.server.merchant.vo.MerchantBuyerVO;
import com.guhecloud.cube.biz.mdata.server.merchant.vo.MerchantPageVO;
import com.guhecloud.cube.biz.mdata.server.merchant.vo.MerchantVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商户基本信息接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantController {

    private final MerchantService merchantService;
    private final AuthenticationService authenticationService;
    private final SellerAuthenticationService sellerAuthenticationService;
    private final GoodsCategoryService goodsCategoryService;
    private final FileClient fileClient;
    private final UserClient userClient;
    private final MerchantUserService merchantUserService;

    /**
     * 商户基本信息新增
     *
     * @param param 商户基本信息新增参数
     */
    @PostMapping("/merchant")
    public void save(@Valid @RequestBody MerchantCreateParam param) {
        this.merchantService.save(param);
    }

    /**
     * 商户基本信息更新
     *
     * @param param 商户基本信息更新参数
     */
    @PutMapping("/merchant")
    public void update(@Valid @RequestBody MerchantUpdateParam param) {
        this.merchantService.update(param);
    }

    /**
     * 商户买家卖家状态
     *
     * @return 状态映射关系
     */
    @GetMapping("/merchant/status")
    public Map<String, Object> getMerchantStatus() {
        Map<String, Object> map = new HashMap<>();
        // 判断当前商户用户是否存在
        Principal principal = SecurityContextHolder.getPrincipal();
        Merchant merchant = this.merchantService.getByUserId(principal.getId());
        if (merchant == null) {
            map.put("buyerStatus", MerchantEnum.AUTH_UNAUTHORIZED);
            map.put("sellerStatus", MerchantEnum.AUTH_UNAUTHORIZED);
            return map;
        }
        Authentication authentication = this.authenticationService.getByMerchantId(merchant.getId());
        SellerAuthentication sellerAuthentication = this.sellerAuthenticationService.getByMerchantId(merchant.getId());
        // 判断是否存在买卖家审核状态
        if (Objects.nonNull(authentication)) {
            map.put("buyerStatus", authentication.getStatus());
        } else {
            map.put("buyerStatus", MerchantEnum.AUTH_UNAUTHORIZED);
        }
        if (Objects.nonNull(sellerAuthentication)) {
            map.put("sellerStatus", sellerAuthentication.getStatus());
        } else {
            map.put("sellerStatus", MerchantEnum.AUTH_UNAUTHORIZED);
        }
        return map;
    }

    /**
     * 小程序获取当前用户市场下的商家列表
     *
     * @return 商家列表
     */
    @GetMapping("/merchant/mine")
    public List<MerchantPageVO> getMyMerchants() {
        // 获取该用户加入的商家
        Principal principal = SecurityContextHolder.getPrincipal();
        List<MerchantUser> merchantUsers = this.merchantUserService.listMerchantUsers(principal.getId());
        List<Long> merchantIds = merchantUsers.stream().map(MerchantUser::getMerchantId).collect(Collectors.toList());
        List<Merchant> merchants = this.merchantService.getByIds(merchantIds);
        Map<Long, List<Merchant>> merchantMap = merchants.stream().collect(Collectors.groupingBy(Merchant::getId));
        List<MerchantPageVO> pageVOS = ObjectUtils.copying(merchants, MerchantPageVO.class);
        // 获取所有商家下的用户
        List<Long> merchantUserIds =
                this.merchantUserService.listMerchantUsers(merchantIds).stream().map(MerchantUser::getUserId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(merchantUserIds)) {
            return Collections.emptyList();
        }

        Pagination<UserDTO> users = this.userClient.listUsers(UserQueryParam.builder().ids(merchantUserIds).pageNumber(1)
                                                                      .pageSize(merchantUserIds.size()).build());
        Map<Long, List<UserDTO>> userMap = users.getRecords().stream().collect(Collectors.groupingBy(UserDTO::getId));

        pageVOS.forEach(pageVO -> {
            if (!MapUtils.isEmpty(merchantMap) && merchantMap.containsKey(pageVO.getId())) {
                Merchant merchant = merchantMap.get(pageVO.getId()).get(0);
                List<MerchantEnum> bizTypes = new ArrayList<>();
                if (Boolean.TRUE.equals(merchant.getBuyer())) {
                    bizTypes.add(MerchantEnum.BIZ_TYPE_BUYER);
                }
                if (Boolean.TRUE.equals(merchant.getSeller())) {
                    bizTypes.add(MerchantEnum.BIZ_TYPE_SELLER);
                }
                pageVO.setBizTypes(bizTypes);
                List<FileVO> logos = this.fileClient.getByBusinessKey(merchant.getId());
                pageVO.setLogo(CollectionUtils.isEmpty(logos) ? null : logos.get(0));

                if (!MapUtils.isEmpty(userMap) && userMap.containsKey(merchant.getOwner())) {
                    UserDTO userDTO = userMap.get(merchant.getOwner()).get(0);
                    pageVO.setAdminName(userDTO == null ? "" : userDTO.getName());
                }
            }
        });
        return pageVOS;
    }

    /**
     * 获取当前用户的商户信息
     *
     * @return 商户信息
     */
    @GetMapping("/merchant/user")
    public MerchantAppVO getMerchant() {
        Principal principal = SecurityContextHolder.getPrincipal();
        // 获取当前用户信息
        Merchant merchant = this.merchantService.getByUserId(principal.getId());
        if (Objects.isNull(merchant)) {
            MerchantAppVO merchantVO = new MerchantAppVO();
            merchantVO.setBuyerStatus(MerchantEnum.AUTH_UNAUTHORIZED);
            merchantVO.setSellerStatus(MerchantEnum.AUTH_UNAUTHORIZED);
            return merchantVO;
        }
        // 获取区域集合
        List<String> areaIds = Arrays.asList(merchant.getArea().split(","));
        MerchantAppVO merchantVO = ObjectUtils.copying(merchant, MerchantAppVO.class);
        // 构建商户信息
        merchantVO.setAreaIds(areaIds);
        // 获取买家认证信息与卖家认证信息
        Authentication authentication = this.authenticationService.getByMerchantId(merchant.getId());
        SellerAuthentication sellerAuthentication = this.sellerAuthenticationService.getByMerchantId(merchant.getId());
        // 判断是否存在买家审核状态
        if (Objects.nonNull(authentication)) {
            merchantVO.setAuthenticationName(authentication.getName());
            merchantVO.setIdentityCard(authentication.getIdentityCard());
            merchantVO.setType(authentication.getType());
            merchantVO.setSubjectName(authentication.getSubjectName());
            merchantVO.setSubjectType(authentication.getSubjectType());
            merchantVO.setLicenseNo(authentication.getLicenseNo());
            merchantVO.setFileVOS(this.fileClient.getByBusinessKey(authentication.getId()));
            merchantVO.setBuyerStatus(authentication.getStatus());
            merchantVO.setRemark(authentication.getReason());
        } else {
            merchantVO.setBuyerStatus(MerchantEnum.AUTH_UNAUTHORIZED);
        }
        // 判断是否存在卖家家审核状态
        if (Objects.nonNull(sellerAuthentication)) {
            List<String> goodsCategoryIds = Arrays.asList(sellerAuthentication.getGoodsCategory().split(","));
            List<GoodsCategory> goodsCategories = this.goodsCategoryService.getByIds(goodsCategoryIds);
            merchantVO.setSellerStatus(sellerAuthentication.getStatus());
            merchantVO.setGoodsCategories(goodsCategories);
        } else {
            merchantVO.setSellerStatus(MerchantEnum.AUTH_UNAUTHORIZED);
        }
        return merchantVO;
    }

    /**
     * 获取买家商户信息
     *
     * @return 买家商户信息集合
     */
    @GetMapping("/merchant/buyer")
    public List<MerchantBuyerVO> getBuyer(@Size(message = "关键字不能大于20") String keyword, Paging paging) {
        // 获取买家商户信息
        Page<Merchant> page = this.merchantService.getBuyer(keyword, paging);
        return ObjectUtils.copying(page.getRecords(), MerchantBuyerVO.class);
    }

    /**
     * 商户基本信息ID查询
     *
     * @param id 商户基本信息ID
     *
     * @return 商户基本信息
     */
    @GetMapping("/merchant/{id}")
    public MerchantVO getById(@NotNull(message = "商户主键不能为空") @Min(value = 1, message = "商户主键不能小于1")
                              @PathVariable("id") Long id
                             ) {
        // 获取商户信息
        Merchant merchant = this.merchantService.getById(id);
        Assert.isNotFound(merchant, "不存在商户信息");
        // 获取区域集合
        List<String> areaIds = Arrays.asList(merchant.getArea().split(","));
        // 构建商户详情信息
        MerchantVO merchantVO = ObjectUtils.copying(merchant, MerchantVO.class);
        List<MerchantEnum> enums = new ArrayList<>();
        if (Boolean.TRUE.equals(merchant.getSeller())) {
            enums.add(MerchantEnum.BIZ_TYPE_SELLER);
        }
        if (Boolean.TRUE.equals(merchant.getBuyer())) {
            enums.add(MerchantEnum.BIZ_TYPE_BUYER);
        }
        merchantVO.setAreaIds(areaIds);
        merchantVO.setBizTypes(enums);
        // 获取logo图片
        List<FileVO> fileVOS = this.fileClient.getByBusinessKey(id);
        merchantVO.setLogo(CollectionUtils.isEmpty(fileVOS) ? null : fileVOS.get(0));
        // 获取用户信息
        UserDTO user = this.userClient.getUser(merchant.getOwner());
        merchantVO.setAdminName(user == null ? "" : user.getName());
        // 构建品类
        SellerAuthentication sellerAuthentication = this.sellerAuthenticationService.getByMerchantId(merchant.getId());
        if (sellerAuthentication != null) {
            List<String> goodsCategoryIds = Arrays.stream(sellerAuthentication.getGoodsCategory().split(",")).collect(Collectors.toList());
            merchantVO.setGoodsCategories(this.goodsCategoryService.getByIds(goodsCategoryIds));
        }
        return merchantVO;
    }

    /**
     * 根据用户数据获取商户列表
     *
     * @param userId 用户主键
     *
     * @return 商户信息列表
     */
    @GetMapping("/merchant/{userId}/list")
    public List<MerchantDTO> listByUserId(@PathVariable("userId") Long userId) {
        List<Merchant> merchants = this.merchantService.listByUserId(userId);
        return ObjectUtils.copying(merchants, MerchantDTO.class);
    }

    /**
     * PC端分页查询
     *
     * @param queryParam 商户基本信息查询参数
     *
     * @return 商户基本信息分页集合
     */
    @GetMapping("/merchant")
    public Pagination<MerchantPageVO> page(@Valid MerchantQueryParam queryParam) {
        // 获取商户信息
        Page<MerchantDTO> page = this.merchantService.page(queryParam);
        // 构建业务角色集合，商户存在多个业务角色
        page.getRecords().forEach(merchant -> {
            List<MerchantEnum> enums = new ArrayList<>();
            if (Boolean.TRUE.equals(merchant.getSeller())) {
                enums.add(MerchantEnum.BIZ_TYPE_SELLER);
            }
            if (Boolean.TRUE.equals(merchant.getBuyer())) {
                enums.add(MerchantEnum.BIZ_TYPE_BUYER);
            }
            merchant.setBizTypes(enums);
        });
        List<MerchantPageVO> merchantPageVOS = ObjectUtils.copying(page.getRecords(), MerchantPageVO.class);
        return PagingUtils.convert(page, merchantPageVOS);
    }

    /**
     * 模糊查询获取商户主键集合
     *
     * @param queryParam 查询参数
     *
     * @return 商户主键ids
     */
    @GetMapping("/merchant/ids")
    public List<Long> getMerchantIds(MerchantQueryParam queryParam) {
        List<Merchant> list = this.merchantService.lambdaQuery()
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(Merchant::getCode, queryParam.getKeyword())
                        .or()
                        .like(Merchant::getName, queryParam.getKeyword())
                        .or()
                        .like(Merchant::getPhone, queryParam.getKeyword())
                    )
                .list();
        return list.stream().map(Merchant::getId).collect(Collectors.toList());
    }

    /**
     * 根据商户主键集合查询
     *
     * @param queryParam 查询参数
     *
     * @return 商户集合信息
     */
    @GetMapping("/merchant/list/ids")
    public List<MerchantDTO> getListByIds(MerchantQueryParam queryParam) {
        List<Merchant> merchants = this.merchantService.listMerchantByIds(queryParam.getMerchantIds());
        return ObjectUtils.copying(merchants, MerchantDTO.class);
    }

    /**
     * 获取商户信息
     *
     * @param merchantId 商户主键
     *
     * @return 商户信息
     */
    @GetMapping("/merchant/{merchantId}/active")
    public MerchantDTO getActiveMerchant(@PathVariable("merchantId") Long merchantId) {
        Merchant merchant = this.merchantService.getActiveMerchant(merchantId);
        return ObjectUtils.copying(merchant, MerchantDTO.class);
    }

    /**
     * 获取有效的商户信息
     *
     * @param id 商户主键
     *
     * @return 商户信息
     */
    @GetMapping("/merchant/{id}/user")
    public MerchantDTO getActiveMerchantUser(@PathVariable("id") Long id) {
        Principal principal = SecurityContextHolder.getPrincipal();
        MerchantUser merchantUser = this.merchantUserService.getMerchantUser(id, principal.getId());
        Assert.isNotFound(merchantUser, "该商户未存在该用户");
        Merchant merchant = this.merchantService.getById(merchantUser.getMerchantId());
        return ObjectUtils.copying(merchant, MerchantDTO.class);
    }

    @GetMapping("/merchant/lookup")
    public List<MerchantDTO> lookupMerchants(@RequestParam("keyword") String keyword) {
        return ObjectUtils.copying(this.merchantService.lookupMerchants(keyword), MerchantDTO.class);
    }

    /**
     * 根据用户主键获取商户信息
     *
     * @param userIds 用户主键集合
     *
     * @return 商户信息列表
     */
    @GetMapping("/merchant/userIds")
    List<MerchantDTO> listUserMerchants(@RequestParam("userIds") Collection<Long> userIds) {
        return ObjectUtils.copying(this.merchantService.listUserMerchants(userIds), MerchantDTO.class);
    }

    /**
     * 获取商户用户关联信息列表
     *
     * @param userIds 用户主键集合
     *
     * @return 商户用户关联信息列表
     */
    @GetMapping("/merchant/userIds/list")
    List<MerchantDTO> listMerchants(@RequestParam("userIds") Collection<Long> userIds) {
        List<MerchantUser> merchantUsers = this.merchantUserService.listMerchantUsersByUserIds(userIds);
        List<Merchant> merchants = this.merchantService.getByIds(merchantUsers.stream().map(MerchantUser::getMerchantId)
                                                                         .collect(Collectors.toList()));
        return ObjectUtils.copying(merchants, MerchantDTO.class);
    }

    /**
     * 理货机获取指定商户
     *
     * @param midType  商户标识类型
     * @param midValue 商户标识值
     *
     * @return 商户信息
     */
    @GetMapping("/merchant/mid-type")
    public MerchantVO getByMidType(@NotNull(message = "商户标识类型不能为空") @RequestParam("midType") String midType,
                                   @NotNull(message = "商户标识值不能为空") @RequestParam("midValue") String midValue
                                  ) {

        Merchant merchant = this.merchantService.getByMidType(midType, midValue);
        return ObjectUtils.copying(merchant, MerchantVO.class);
    }

    @GetMapping("/merchant/lookup/page")
    public Pagination<MerchantDTO> lookupMerchantPage(@SpringQueryMap MerchantQueryParam queryParam) {
        return PagingUtils.convert(this.merchantService.lookupMerchantPage(queryParam), MerchantDTO.class);
    }

    /**
     * 获取用户有效的商户
     *
     * @param userId 用户主键
     *
     * @return 商户信息
     */
    @GetMapping("/user-merchant/{userId}/active")
    public MerchantDTO getUserActiveMerchant(@PathVariable("userId") Long userId) {
        return ObjectUtils.copying(this.merchantService.getUserActiveMerchant(userId), MerchantDTO.class);
    }

    @PutMapping("/merchant/update")
    public void updaterId(@RequestBody MerchantDTO merchantDTO) {
        Merchant merchant = this.merchantService.getById(merchantDTO.getId());
        ObjectUtils.copying(merchantDTO, merchant);
        this.merchantService.updateById(merchant);
    }

    @GetMapping("/merchant/id")
    public MerchantDTO getMerchantById(@RequestParam("id") Long id) {
        Merchant merchant = this.merchantService.getById(id);
        return ObjectUtils.copying(merchant, MerchantDTO.class);
    }

    /**
     * 获取卖家买家商户
     *
     * @param queryParam 关键字
     *
     * @return 商户信息
     */
    @GetMapping("/merchant-stat/biz")
    public Pagination<MerchantDTO> getMerchantsByBiz(@Valid MerchantStatQueryParam queryParam) {
        Page<Merchant> page = this.merchantService.getMerchantsByBiz(queryParam);
        List<Merchant> merchants = page.getRecords();
        return PagingUtils.convert(page, ObjectUtils.copying(merchants, MerchantDTO.class));
    }

}
