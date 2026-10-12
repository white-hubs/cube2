package com.guhecloud.cube.biz.meattrade.server.base.controller;

import com.guhecloud.cube.biz.mdata.client.GoodsCategoryClient;
import com.guhecloud.cube.biz.meattrade.server.base.entity.GoodsWeightConfig;
import com.guhecloud.cube.biz.meattrade.server.base.param.GoodWeightConfigModifyParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.GoodsWeightConfigService;
import com.guhecloud.cube.biz.meattrade.server.base.vo.GoodsWeightConfigVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 肉类商品品类重量配置
 *
 * @author lanchun
 * @date 2023/7/28 9:59
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsWeightConfigController {

    private final GoodsWeightConfigService goodsWeightConfigService;

    private final GoodsCategoryClient goodsCategoryClient;

    /**
     * 肉类商品品类重量新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/meattrade-goods-weight-config")
    public Long save(@Valid @RequestBody GoodWeightConfigModifyParam param) {
        GoodsWeightConfig goodsWeightConfig = ObjectUtils.copying(param, GoodsWeightConfig.class);
        this.setCategoryInfo(param, goodsWeightConfig);
        return this.goodsWeightConfigService.saveGoodsWeightConfig(goodsWeightConfig);
    }

    /**
     * 肉类商品品类重量修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/meattrade-goods-weight-config/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody GoodWeightConfigModifyParam param) {
        GoodsWeightConfig oldParam = this.goodsWeightConfigService.getById(id);
        Assert.isNotFound(oldParam, "数据不存在");
        GoodsWeightConfig newParam = ObjectUtils.copying(param, GoodsWeightConfig.class);
        newParam.setId(id);
        newParam.setDeleted(oldParam.getDeleted());
        EntityAuditor.populateCreateAudit(newParam, oldParam.getCreateBy(), oldParam.getCreatorId(),
                                          oldParam.getCreatorType(), oldParam.getCreateTime());
        this.setCategoryInfo(param, newParam);
        this.goodsWeightConfigService.update(id, oldParam, newParam);
    }

    /**
     * 肉类重量设置列表
     *
     * @return
     */
    @GetMapping("/meattrade-goods-weight-configs")
    public List<GoodsWeightConfigVO> getGoodWeightConfigs() {
        List<GoodsWeightConfig> goodsWeightConfigs = this.goodsWeightConfigService.listGoodWeightConfig();
        if (CollectionUtils.isEmpty(goodsWeightConfigs)) {
            return Collections.emptyList();
        }
        List<GoodsWeightConfigVO> goodsWeightConfigVOS = ObjectUtils.copying(goodsWeightConfigs, GoodsWeightConfigVO.class);
        goodsWeightConfigVOS.stream().forEach(goodsWeightConfigVO -> {
            if (StringUtils.isNotEmpty(goodsWeightConfigVO.getGoodsCategoryId())) {
                goodsWeightConfigVO.setGoodsCategoryIds(Arrays.stream(goodsWeightConfigVO.getGoodsCategoryId().split("-"))
                                                                .collect(Collectors.toSet()));
            }
        });
        return goodsWeightConfigVOS;
    }

    /**
     * 肉类重量设置删除
     *
     * @param id
     */
    @DeleteMapping("/meattrade-goods-weight-config/{id}")
    public void remove(@PathVariable("id") Long id) {
        GoodsWeightConfig goodsWeightConfig = this.goodsWeightConfigService.getById(id);
        Assert.isNotFound(goodsWeightConfig, "数据不存在");
        this.goodsWeightConfigService.remove(goodsWeightConfig);
    }

    private void setCategoryInfo(@NonNull GoodWeightConfigModifyParam param, GoodsWeightConfig goodsWeightConfig) {
        goodsWeightConfig.setGoodsCategoryId(param.getGoodsCategoryIds().stream().collect(Collectors.joining("-")));
        String categoryId = param.getGoodsCategoryIds().get(param.getGoodsCategoryIds().size() - 1);
        List<String> categoryNames = this.goodsCategoryClient.getNameByIds(Collections.singletonList(categoryId));
        if (!CollectionUtils.isEmpty(categoryNames)) {
            goodsWeightConfig.setGoodsCategoryName(categoryNames.get(0));
        }
    }
}
