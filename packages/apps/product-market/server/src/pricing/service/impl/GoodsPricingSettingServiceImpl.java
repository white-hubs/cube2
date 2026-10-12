package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingSettingMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingSetting;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingSettingParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingSettingService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingSettingVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：商品采价计划服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingSettingServiceImpl extends ServiceImpl<GoodsPricingSettingMapper, GoodsPricingSetting> implements GoodsPricingSettingService {

    @Override
    public void addOrUpdate(GoodsPricingSettingParam param) {
        GoodsPricingSetting goodsPricingSetting = this.baseMapper.selectOne(Wrappers.<GoodsPricingSetting>lambdaQuery()
                                                                                    .eq(GoodsPricingSetting::getCode, param.getCode()));
        if (goodsPricingSetting == null) {
            GoodsPricingSetting copying = ObjectUtils.copying(param, GoodsPricingSetting.class);
            this.baseMapper.insert(copying);
        } else {
            goodsPricingSetting.setCode(param.getCode());
            goodsPricingSetting.setNoticeMinute(param.getNoticeMinute());
            this.baseMapper.updateById(goodsPricingSetting);
        }
    }

    @Override
    public GoodsPricingSettingVO getByCode(String code) {
        GoodsPricingSetting goodsPricingSetting = this.baseMapper.selectOne(Wrappers.<GoodsPricingSetting>lambdaQuery()
                                                                                    .eq(GoodsPricingSetting::getCode, code));
        return ObjectUtils.copying(goodsPricingSetting, GoodsPricingSettingVO.class);
    }
}