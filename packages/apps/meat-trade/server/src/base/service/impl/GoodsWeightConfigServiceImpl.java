package com.guhecloud.cube.biz.meattrade.server.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.base.dao.GoodsWeightConfigMapper;
import com.guhecloud.cube.biz.meattrade.server.base.entity.GoodsWeightConfig;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.cube.biz.meattrade.server.base.service.GoodsWeightConfigService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsWeightConfigServiceImpl extends ServiceImpl<GoodsWeightConfigMapper, GoodsWeightConfig> implements GoodsWeightConfigService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(success = "新增标准重量设置", fail = "创建商品重量设置失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "GOODS_CATEGORY_WEIGHT_SETTING", subTypeName = "肉类重量设置",
               operationType = OperationType.ADD,
               extra = "{{#goodsWeightConfig}}",
               bizNo = ""
    )
    public Long saveGoodsWeightConfig(@NonNull GoodsWeightConfig goodsWeightConfig) {
        this.baseMapper.insert(goodsWeightConfig);
        return goodsWeightConfig.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(success = "编辑标准重量设置", fail = "编辑商品重量设置失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "GOODS_CATEGORY_WEIGHT_SETTING", subTypeName = "肉类重量设置",
               operationType = OperationType.UPDATE,
               bizNo = "{{#id}}"
    )
    public void update(@NonNull Long id, @NonNull GoodsWeightConfig oldConfig, @NonNull GoodsWeightConfig newConfig) {
        this.baseMapper.updateById(newConfig);
    }

    @Override
    public GoodsWeightConfig getGoodWeightConfig(@NonNull String goodsCategoryId, MeatGoodsFreshLabelEnum freshLabel) {
        List<GoodsWeightConfig> goodsWeightConfigs = this.baseMapper.selectList(Wrappers.<GoodsWeightConfig>lambdaQuery()
                                                                                        .eq(GoodsWeightConfig::getGoodsCategoryId, goodsCategoryId)
                                                                                        .eq(Objects.nonNull(freshLabel), GoodsWeightConfig::getFreshLabel, freshLabel));
        return CollectionUtils.isNotEmpty(goodsWeightConfigs) ? goodsWeightConfigs.getFirst() : null;
    }

    @Override
    public List<GoodsWeightConfig> listGoodWeightConfig() {
        return this.baseMapper.selectList(Wrappers.<GoodsWeightConfig>lambdaQuery()
                                                  .orderByDesc(GoodsWeightConfig::getCreateTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(success = "删除标准重量设置", fail = "删除商品重量设置失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "GOODS_CATEGORY_WEIGHT_SETTING", subTypeName = "肉类重量设置",
               operationType = OperationType.DELETE,
               bizNo = ""
    )
    public void remove(@NonNull GoodsWeightConfig goodsWeightConfig) {
        DatabaseContextHolder.softDelete(this, goodsWeightConfig);
    }
}
