package com.guhecloud.cube.srv.gen.product.market.server.common.controller;

import com.guhecloud.cube.biz.mdata.client.GoodsCategoryClient;
import com.guhecloud.cube.biz.mdata.client.param.GoodsCategoryCreateParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketGoodsCategory;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.MarketGoodsCategoryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.MarketGoodsCategoryQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.service.MarketGoodsCategoryService;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketGoodsCategoryTreeVO;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketGoodsCategoryVO;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.job.DataETLJob;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.service.ThirdDataQueryService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Appet
 * @Description 商品品类
 * @date 2023-05-10 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketGoodsCategoryController {
    private final MarketGoodsCategoryService marketGoodsCategoryService;
    private final ThirdDataQueryService thirdDataQueryService;
    private final GoodsCategoryClient goodsCategoryClient;
    private final DataETLJob dataETLJob;

    /**
     * 功能描述： 商品品类添加
     */
    @PostMapping(value = "/market-goods-category")
    public Long add(@Valid @RequestBody MarketGoodsCategoryParam param) {
        return marketGoodsCategoryService.add(param);
    }

    /**
     * 功能描述： 商品品类修改
     */
    @PutMapping(value = "/market-goods-category/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody MarketGoodsCategoryParam param) {
        marketGoodsCategoryService.update(id, param);
    }

    /**
     * 功能描述： 商品品类删除
     */
    @DeleteMapping(value = "/market-goods-category/{id}")
    public void delete(@PathVariable Long id) {
        marketGoodsCategoryService.delete(id);
    }

    /**
     * 功能描述： 商品品类批量删除
     */
    @DeleteMapping(value = "/market-goods-category/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        marketGoodsCategoryService.batchDelete(ids);
    }

    /**
     * 功能描述： 商品品类分页
     */
    @GetMapping(value = "/market-goods-category/page")
    public Pagination<MarketGoodsCategoryVO> getPage(MarketGoodsCategoryQueryParam param) {
        return marketGoodsCategoryService.getPage(param);
    }

    /**
     * 品类树查询
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/market-goods-category/tree")
    public List<MarketGoodsCategoryTreeVO> getTree(MarketGoodsCategoryQueryParam param) {

        List<MarketGoodsCategory> categories = this.marketGoodsCategoryService.list();
        List<MarketGoodsCategoryTreeVO> goodsCategoryListVOS = ObjectUtils.copying(categories, MarketGoodsCategoryTreeVO.class);
        return TreeUtils.assembleTrees(goodsCategoryListVOS);
    }

    /**
     * 功能描述： 商品品类详情
     */
    @GetMapping(value = "/market-goods-category/{id}")
    public MarketGoodsCategoryVO getById(@PathVariable Long id) {
        return marketGoodsCategoryService.getById(id);
    }

    /**
     * 功能描述： 商品品类手动同步
     */
    @GetMapping(value = "/market-goods-category/syn")
    public void synGoodsCategoryData() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        dataETLJob.synGoodsCategory(tenant);
    }

    /**
     * 同步品类数据到mdata
     */
    @PostMapping("/market-goods-category/mdata/syn")
    public void synGoodsCategoryDataForMdata() {
        List<MarketGoodsCategory> goodsCategoryList = thirdDataQueryService.getGoodsCategory();
        //保存来货数据
        if (!CollectionUtils.isEmpty(goodsCategoryList)) {
            //先删除
            goodsCategoryClient.removeAll();
            //保存品类数据
            //先保存一级
            List<MarketGoodsCategory> oneLevels = goodsCategoryList.stream()
                    .filter(c -> c.getParentId() == null).collect(Collectors.toList());

            oneLevels.forEach(c -> {
                GoodsCategoryCreateParam copying = ObjectUtils.copying(c, GoodsCategoryCreateParam.class);
                goodsCategoryClient.save(copying);
            });

            //再保存二级
            List<MarketGoodsCategory> secondLevels = goodsCategoryList.stream()
                    .filter(c -> c.getParentId() != null)
                    .peek(c -> c.setLevel(2)).toList();
            secondLevels.forEach(c -> {
                GoodsCategoryCreateParam copying = ObjectUtils.copying(c, GoodsCategoryCreateParam.class);
                copying.setCode(c.getCode());
                goodsCategoryClient.save(copying);
            });
        }
    }

}