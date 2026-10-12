package com.guhecloud.cube.biz.mdata.client;

import com.guhecloud.cube.biz.mdata.client.dto.MerchantDTO;
import com.guhecloud.cube.biz.mdata.client.param.MerchantQueryParam;
import com.guhecloud.cube.biz.mdata.client.param.MerchantStatQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@FeignClient(name = "mdata")
public interface MerchantClient {

    @GetMapping("/merchant/id")
    MerchantDTO getMerchantById(@RequestParam("id") Long id);

    @GetMapping("/merchant/stallId")
    MerchantDTO getByIds(@RequestParam("stallId") Long stallId);

    @GetMapping("/merchant/{userId}/list")
    List<MerchantDTO> listByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/merchant/{id}/user")
    MerchantDTO getActiveMerchantUser(@PathVariable("id") Long id);

    @GetMapping("/merchant/{merchantId}/active")
    MerchantDTO getActiveMerchant(@PathVariable("merchantId") Long merchantId);

    /**
     * 模糊查询获取商户主键集合
     *
     * @param queryParam 查询参数
     *
     * @return 商户主键ids
     */
    @GetMapping("/merchant/ids")
    public List<Long> getMerchantIds(@SpringQueryMap MerchantQueryParam queryParam);

    /**
     * 根据商户主键集合查询
     *
     * @param queryParam 查询参数
     *
     * @return 商户集合信息
     */
    @GetMapping("/merchant/list/ids")
    public List<MerchantDTO> getListByIds(@SpringQueryMap MerchantQueryParam queryParam);

    /**
     * 理货机获取指定商户
     *
     * @param midType  商户标识类型
     * @param midValue 商户标识值
     *
     * @return 商户信息
     */
    @GetMapping("/merchant/mid-type")
    public MerchantDTO getByMidType(@NotNull(message = "商户标识类型不能为空") @RequestParam("midType") String midType,
                                    @NotNull(message = "商户标识值不能为空") @RequestParam("midValue") String midValue
                                   );

    @GetMapping("/merchant/lookup")
    public List<MerchantDTO> lookupMerchants(@RequestParam("keyword") String keyword);

    @GetMapping("/merchant/lookup/page")
    public Pagination<MerchantDTO> lookupMerchantPage(@SpringQueryMap MerchantQueryParam queryParam);

    /**
     * 获取商户用户关联信息列表
     *
     * @param userIds 用户主键集合
     *
     * @return 商户用户关联信息列表
     */
    @GetMapping("/merchant/userIds/list")
    List<MerchantDTO> listMerchants(@RequestParam("userIds") Collection<Long> userIds);

    /**
     * 获取用户有效的商户
     *
     * @param userId 用户主键
     *
     * @return 商户信息
     */
    @GetMapping("/user-merchant/{userId}/active")
    public MerchantDTO getUserActiveMerchant(@PathVariable("userId") Long userId);

    @PutMapping("/merchant/update")
    public void updaterId(@RequestBody MerchantDTO merchantDTO);

    /**
     * 获取卖家买家商户
     *
     * @param queryParam 关键字
     *
     * @return 商户信息
     */
    @GetMapping("/merchant-stat/biz")
    public Pagination<MerchantDTO> getMerchantsByBiz(@SpringQueryMap MerchantStatQueryParam queryParam);
}
