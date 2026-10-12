package com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.job;

import com.guhecloud.cube.biz.mdata.client.GoodsCategoryClient;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.param.GoodsCategoryQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.dao.MarketGoodsCategoryMapper;
import com.guhecloud.cube.srv.gen.product.market.server.common.dao.MarketStallMapper;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketStall;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataETLFromMdataJob {
    private final StallClient stallClient;
    private final GoodsCategoryClient goodsCategoryClient;
    private final MarketStallMapper marketStallMapper;
    private final MarketGoodsCategoryMapper marketGoodsCategoryMapper;

    /**
     * 从mdata同步档位数据
     */
    public void syncStallFromMdata(String tenant) {
        SessionContextHolder.requestTenant(Integer.valueOf(tenant));
        //获取第三方的档位数据
        var stalls = stallClient.getStallList();
        //保存档位数据
        if (CollectionUtils.isNotEmpty(stalls)) {
            var dataList = ObjectUtils.copying(stalls, MarketStall.class);
            marketStallMapper.insertOrUpdateStallBatch(dataList);
        }
        SessionContextHolder.requestTenant(null);
    }

    /**
     * 从mdata同步品类数据
     */
    public void syncGoodsCategoryFromMdata(String tenant) {
        SessionContextHolder.requestTenant(Integer.valueOf(tenant));
        //获取第三方的档位数据
        var goodsCategoryList = goodsCategoryClient.getAllList(new GoodsCategoryQueryParam());
        //保存档位数据
        if (CollectionUtils.isNotEmpty(goodsCategoryList)) {
            marketGoodsCategoryMapper.insertOrUpdateBatch(goodsCategoryList);
        }
        SessionContextHolder.requestTenant(null);
    }
}
