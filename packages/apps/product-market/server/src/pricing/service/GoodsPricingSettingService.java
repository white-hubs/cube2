package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingSetting;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingSettingParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingSettingVO;

/**
 * 描述：采价任务配置
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
public interface GoodsPricingSettingService extends IService<GoodsPricingSetting> {

    void addOrUpdate(GoodsPricingSettingParam param);

    GoodsPricingSettingVO getByCode(String code);

}