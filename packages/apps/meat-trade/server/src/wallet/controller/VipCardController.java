package com.guhecloud.cube.biz.meattrade.server.wallet.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.dto.SupplierVipCardDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.VipCard;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.VipCardBindParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.VipCardDecodeParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.VipCardEncodeParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.VipCardQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.service.VipCardService;
import com.guhecloud.cube.biz.meattrade.server.wallet.vo.SupplierVipCardVO;
import com.guhecloud.cube.biz.meattrade.server.wallet.vo.VipCardVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.annotation.Signable;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 会员卡接口
 *
 * @author Woody
 * @date 2021/8/22
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VipCardController {
    private final VipCardService vipCardService;
    private final SupplierClient supplierClient;

    /**
     * 编码会员卡信息
     *
     * @param param 编码参数
     *
     * @return 编码信息
     */
    @PostMapping("/vip-card/encode")
    public String encode(@RequestBody @Valid VipCardEncodeParam param) {
        return this.vipCardService.encode(ObjectUtils.copying(param, VipCard.Content.class));
    }

    /**
     * 解码会员卡信息
     *
     * @param param 解码参数
     *
     * @return 会员卡信息
     */
    @GetMapping("/vip-card/decode")
    public VipCard.Content decode(@Valid VipCardDecodeParam param) {
        return this.vipCardService.decode(param.getCiphertext());
    }

    /**
     * 绑定商户会员卡
     *
     * @param supplierId 商户主键
     * @param param      绑定参数
     */
    @PostMapping("/supplier/{supplierId}/vip-card")
    public void bind(@PathVariable("supplierId") @Min(value = 1, message = "商户主键不能小于1") Long supplierId,
                     @RequestBody @Valid @Signable VipCardBindParam param
                    ) {
        this.vipCardService.bind(supplierId, param);
    }

    /**
     * 解绑会员卡
     *
     * @param id 会员卡主键
     */
    @DeleteMapping("/vip-card/{id}")
    public void unbind(@PathVariable("id") @Min(value = 1, message = "会员卡主键不能小于1") Long id) {
        this.vipCardService.unbind(id);
    }

    /**
     * 获取我的会员卡列表
     *
     * @return 会员卡列表
     */
    @GetMapping("/mine/vip-cards")
    public List<VipCardVO> listMineCards() {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Set<Long> userIds = Stream.of(userId).collect(Collectors.toSet());
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(SessionContextHolder.getRequester().getTenant());
        Assert.isNotFound(supplier, "商家不存在，无法获取会员卡");
        List<SupplierDTO> supplierDTOS = this.supplierClient.listByUserIds(userIds, supplier.getTenant());
        if (CollectionUtils.isEmpty(supplierDTOS)) {
            return Collections.emptyList();
        }
        List<Long> supplierIds = supplierDTOS.stream().map(SupplierDTO::getId).collect(Collectors.toList());
        List<VipCard> cards = this.vipCardService.getUserCards(supplierIds);
        return ObjectUtils.copying(cards, VipCardVO.class);
    }

    /**
     * 采购商获取我的会员卡列表
     *
     * @return 会员卡列表
     */
    @GetMapping("/mine/purchaser/vip-cards")
    public List<VipCardVO> listMinePurchaserCards() {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Set<Long> userIds = Stream.of(userId).collect(Collectors.toSet());
        List<SupplierDTO> supplierDTOS = this.supplierClient.listByUserIds(userIds, SessionContextHolder.getRequester().getTenant());
        if (CollectionUtils.isEmpty(supplierDTOS)) {
            return Collections.emptyList();
        }
        List<Long> supplierIds = supplierDTOS.stream().map(SupplierDTO::getId).collect(Collectors.toList());
        List<VipCard> cards = this.vipCardService.getUserCards(supplierIds);
        return ObjectUtils.copying(cards, VipCardVO.class);
    }

    /**
     * 查询会员卡列表
     *
     * @param param 查询参数
     *
     * @return 会员卡列表分页
     *
     */
    @GetMapping("/vip-cards")
    public Pagination<SupplierVipCardVO> listCards(@Valid VipCardQueryParam param) {
        // 如果关键字参数满足会员卡密文模式，则对密文进行解码，然后使用会员卡号查询
        if (this.vipCardService.isCiphertext(param.getKeyword())) {
            param.setKeyword(this.vipCardService.decode(param.getKeyword()).getCode());
        }
        // 获取市场租户编号
        Integer tenant = SessionContextHolder.getRequester().getTenant();

        List<SupplierDTO> supplierDTOS = this.supplierClient.listSuppliers(param.getKeyword(), tenant);
        List<Long> supplierIds = supplierDTOS.stream().map(SupplierDTO::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(supplierIds)) {
            return Pagination.empty();
        }
        Page<SupplierVipCardDTO> page = this.vipCardService.listCards(param, supplierIds);
        List<SupplierVipCardDTO> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Pagination.empty();
        }
        // 扩展商家信息
        Map<Long, List<SupplierDTO>> supplierMap = supplierDTOS.stream().collect(Collectors.groupingBy(SupplierDTO::getId));
        List<SupplierVipCardVO> cards = records.stream().map(record -> {
            SupplierVipCardVO card = ObjectUtils.copying(record, SupplierVipCardVO.class);
            if (supplierMap.containsKey(record.getSupplierId())) {
                SupplierDTO supplierDTO = supplierMap.get(record.getSupplierId()).get(0);
                record.setName(supplierDTO.getName());
                record.setLiaison(supplierDTO.getLiaison());
                record.setPhone(supplierDTO.getPhone());
            }
            SupplierDTO supplier = ObjectUtils.copying(record, SupplierDTO.class);
            supplier.setId(record.getSupplierId());
            card.setSupplier(supplier);
            return card;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, cards);
    }
}
