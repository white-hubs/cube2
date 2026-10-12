package com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.IncomingQuantity;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketGoodsCategory;
import com.guhecloud.cube.srv.gen.product.market.server.common.service.IncomingQuantityService;
import com.guhecloud.cube.srv.gen.product.market.server.common.service.MarketGoodsCategoryService;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.service.ThirdDataQueryService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataETLJob {
    private final ThirdDataQueryService thirdDataQueryService;
    private final IncomingQuantityService incomingQuantityService;
    private final MarketGoodsCategoryService goodsCategoryService;
    private final int innerTenant = 9999;

    /**
     * 每6个小时更新一次来货量
     */
    @JobAnnotation(name = "quantityETL", cron = "0 0 2/6 * * ?", desc = "商品采价-定时更新来货量")
    public void quantityETL() {
        //获取当日的来货数据
        var yyyyMMdd = DateUtils.format(LocalDateTime.now(), "yyyyMMdd");
        var quantities = thirdDataQueryService.getIncomingQuantity(yyyyMMdd);
        //保存来货数据
        if (CollectionUtils.isNotEmpty(quantities)) {
            SessionContextHolder.requestTenant(innerTenant);
            //删除来货数据
            incomingQuantityService.remove(Wrappers.<IncomingQuantity>lambdaQuery()
                                                   .eq(IncomingQuantity::getDateNo, yyyyMMdd));
            //保存来货数据
            incomingQuantityService.saveBatch(quantities);
            SessionContextHolder.requestTenant(null);
        }
    }

    public void synGoodsCategory(Integer tenantParam) {
        List<MarketGoodsCategory> goodsCategoryList = thirdDataQueryService.getGoodsCategory();
        //保存来货数据
        if (CollectionUtils.isNotEmpty(goodsCategoryList)) {

            SessionContextHolder.requestTenant(tenantParam);
            //先删除
            goodsCategoryService.remove(Wrappers.<MarketGoodsCategory>lambdaQuery()
                                                .eq(MarketGoodsCategory::getTenant, tenantParam));
            //保存品类数据
            //先保存一级
            List<MarketGoodsCategory> oneLevels = goodsCategoryList.stream()
                    .filter(c -> c.getParentId() == null).toList();

            oneLevels.forEach(goodsCategoryService::save);

            //再保存二级
            List<MarketGoodsCategory> secondLevels = goodsCategoryList.stream()
                    .filter(c -> c.getParentId() != null)
                    .peek(c -> c.setLevel(2)).toList();
            secondLevels.forEach(goodsCategoryService::save);
            SessionContextHolder.requestTenant(null);
        }
    }
}
