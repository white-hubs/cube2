package com.guhecloud.cube.biz.meattrade.server.wallet.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.MerchantClient;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.meattrade.server.wallet.dao.VipCardMapper;
import com.guhecloud.cube.biz.meattrade.server.wallet.dto.SupplierVipCardDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.VipCard;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.VipCardBindParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.VipCardQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.service.VipCardService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DigestUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 会员卡业务操作实现
 *
 * @author Woody
 * @date 2021/8/22
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VipCardServiceImpl extends ServiceImpl<VipCardMapper, VipCard> implements VipCardService {
    private final MerchantClient merchantClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SupplierClient supplierClient;

    /**
     * 密文匹配模式
     */
    private static final Pattern CIPHERTEXT_PATTERN = Pattern.compile("[0-9a-fA-F]{17,}");

    /**
     * 将会员卡内容转换成16进制
     *
     * @param content 会员卡内容
     *
     * @return 会员卡内容16进制形式
     */
    private String content2hex(VipCard.Content content) {
        return StringUtils.justRight(Long.toHexString(content.getSupplierId()), 16, '0')
               + DigestUtils.md5Hex(content.getCode().getBytes());
    }

    /**
     * 将会员卡内容16进制形式转换成对象实例
     *
     * @param hex 会员卡内容16进制形式
     *
     * @return 会员卡内容对象实例
     */
    private VipCard.Content hex2content(String hex) {
        Long supplierId = Long.valueOf(hex.substring(0, 16), 16);
        String code = DigestUtils.md5Hex(hex.substring(16)).trim();
        return VipCard.Content.builder().supplierId(supplierId).code(code).build();
    }

    @Override
    public boolean isCiphertext(String text) {
        return !StringUtils.isEmpty(text) && text.length() % 2 == 0 && CIPHERTEXT_PATTERN.matcher(text).matches();
    }

    @Override
    public String encode(@NonNull VipCard.Content content) {
        String ciphertext = this.content2hex(content);
        this.redisTemplate.opsForValue().set("vip-card:" + ciphertext, content.getCode(), Duration.ofSeconds(10));
        return ciphertext;
    }

    @Override
    public VipCard.Content decode(@NonNull String ciphertext) {
        return this.hex2content(ciphertext);
    }

    @Override
    public void bind(@NonNull Long supplierId, @NonNull VipCardBindParam param) {
        // 验证商户
        Assert.isNotFound(this.supplierClient.getSupplierById(supplierId), "商户不存在");

        // 验证会员卡
        VipCard.Content content = VipCard.Content.builder().supplierId(supplierId).code(param.getCode()).build();
        Assert.isNotAcceptable(!this.content2hex(content).equals(param.getCiphertext()), "会员卡校验失败");
        Boolean deleted = this.redisTemplate.delete("vip-card:" + param.getCiphertext());
        Assert.isNotAcceptable(!deleted, "绑卡超时");

        // 保存会员卡
        VipCard card = VipCard.builder().supplierId(supplierId).code(param.getCode()).tenant(param.getTenant()).build();
        this.save(card);
    }

    @Override
    public void unbind(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public List<VipCard> getUserCards(@NonNull Collection<Long> supplierIds) {
        return this.baseMapper.getUserCards(supplierIds);
    }

    @Override
    public Page<SupplierVipCardDTO> listCards(@NonNull VipCardQueryParam param, @NonNull Collection<Long> supplierIds) {
        return this.baseMapper.listCards(PagingUtils.convert(param), param, supplierIds);
    }

    @Override
    public List<VipCard> listCards(String keyword, Integer tenant) {
        return this.lambdaQuery().eq(VipCard::getCode, keyword).eq(VipCard::getTenant, tenant).list();
    }
}
