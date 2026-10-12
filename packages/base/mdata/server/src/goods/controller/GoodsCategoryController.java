package com.guhecloud.cube.biz.mdata.server.goods.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.mdata.server.goods.converters.GoodsCategoryConverterFoVO;
import com.guhecloud.cube.biz.mdata.server.goods.dto.GoodsCategoryDTO;
import com.guhecloud.cube.biz.mdata.server.goods.dto.GoodsCategoryListDTO;
import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryBatchRenewParam;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryCreateParam;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryIdsParam;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryQueryParam;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryUpdateParam;
import com.guhecloud.cube.biz.mdata.server.goods.service.GoodsCategoryService;
import com.guhecloud.cube.biz.mdata.server.goods.vo.GoodsCategoryListVO;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTreeEntity;
import com.guhecloud.platform.database.util.EntityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品分类接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsCategoryController {

    private final GoodsCategoryService goodsCategoryService;
    private final SupplierService supplierService;

    /**
     * 商品分类新增
     *
     * @param param 商品分类新增参数
     */
    @PostMapping("/goods-category")
    public void save(@Valid @RequestBody GoodsCategoryCreateParam param) {
        this.goodsCategoryService.save(param);
    }

    /**
     * 商品分类更新
     *
     * @param param 商品分类更新参数
     */
    @PutMapping("/goods-category")
    public void update(@Valid @RequestBody GoodsCategoryUpdateParam param) {
        this.goodsCategoryService.update(param);
    }

    /**
     * 商品品类删除
     *
     * @param id 商品品类id
     */
    @DeleteMapping("/goods-category/{id}")
    public void remove(@NotNull(message = "主键不能为空") @Min(value = 1, message = "主键不能小于1")
                       @PathVariable("id") Long id
                      ) {
        this.goodsCategoryService.remove(id);
    }

    /**
     * 删除租户下的所有品类
     */
    @DeleteMapping("/goods-category/all")
    public void removeAll() {
        this.goodsCategoryService.remove(Wrappers.emptyWrapper());
    }

    /**
     * 商品分类列表
     *
     * @param queryParam 商品分类查询参数
     *
     * @return 商品分类列表结合
     */
    @GetMapping("/goods-category/list")
    public List<GoodsCategoryListVO> list(GoodsCategoryQueryParam queryParam) {
        List<GoodsCategory> list = this.goodsCategoryService.getList(queryParam);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        //  获取序列号
        List<String> sequences = EntityUtils.getAllSequences(list);
        List<GoodsCategory> categories = this.goodsCategoryService.getBySequences(sequences);
        List<GoodsCategoryListVO> goodsCategoryListVOS = ObjectUtils.copying(categories, GoodsCategoryListVO.class);
        return TreeUtils.assembleTrees(goodsCategoryListVOS);
    }

    @GetMapping("/goods-category/all/list")
    public List<GoodsCategory> getAllList(GoodsCategoryQueryParam queryParam) {
        List<GoodsCategory> list = this.goodsCategoryService.getList(queryParam);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    /**
     * 小程序商品品类信息
     *
     * @param queryParam 查询参数
     *
     * @return 商品品类信息
     */
    @GetMapping("/goods-category/app/list")
    public List<GoodsCategoryListVO> appList(GoodsCategoryQueryParam queryParam) {
        List<GoodsCategory> list = this.goodsCategoryService.getAppList(queryParam);
        if (CollectionUtils.isEmpty(list) || StringUtils.isEmpty(queryParam.getKeyword())) {
            return Collections.emptyList();
        }
        // 获取满足商品的id集合
        List<Long> ids = list.stream().map(GoodsCategory::getId).collect(Collectors.toList());
        List<GoodsCategory> goodsCategories = this.goodsCategoryService.getThirdLevelByIds(ids);
        return ObjectUtils.copying(goodsCategories, GoodsCategoryListVO.class);
    }

    /**
     * 小程序商品品类信息
     *
     * @param queryParam 查询参数
     *
     * @return 商品品类信息
     */
    @GetMapping("/goods-category/mp/list")
    public List<GoodsCategoryListVO> listMpGoodsCategory(GoodsCategoryQueryParam queryParam) {
        SessionContextHolder.requestTenant(queryParam.getTenant());
        List<GoodsCategory> goodsCategories = this.goodsCategoryService.getAppList(queryParam);
        if (CollectionUtils.isEmpty(goodsCategories)) {
            return Collections.emptyList();
        }
        Set<Long> goodsCategoryIds = goodsCategories.stream().map(GoodsCategory::getId).collect(Collectors.toSet());
        List<GoodsCategoryListVO> categoryListVOS = GoodsCategoryConverterFoVO.toVOS(this.goodsCategoryService.getLastLevelByIds(goodsCategoryIds));
        return TreeUtils.assembleTrees(categoryListVOS);
    }

    @GetMapping("/goods-category/ids")
    public List<GoodsCategoryDTO> getByIds(List<String> ids) {
        List<GoodsCategory> byIds = goodsCategoryService.getByIds(ids);
        return ObjectUtils.copying(byIds, GoodsCategoryDTO.class);
    }

    @GetMapping("/goods-category/{code}/sequence")
    public String getSequenceByCode(@PathVariable("code") String code) {
        GoodsCategory gc = goodsCategoryService.getOne(Wrappers.<GoodsCategory>lambdaQuery()
                                                               .eq(GoodsCategory::getCode, code));
        return gc != null ? gc.getSequence() : "";
    }

    /**
     * 商城商品分类列表
     *
     * @param queryParam 商品分类查询参数
     *
     * @return 商城商品分类列表
     */
    @PostMapping("/mall-goods-category/list")
    public List<GoodsCategoryListDTO> mallCategoryList(@RequestBody GoodsCategoryQueryParam queryParam) {
        //转换tenant 将小程序端的tenant转换成市场端的tenant
        Integer tenant = queryParam.getTenant();
        Supplier bySupplierTenant = supplierService.getBySupplierTenant(tenant);
        SessionContextHolder.requestTenant(bySupplierTenant.getTenant());
        List<GoodsCategory> list = this.goodsCategoryService.getList(queryParam);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        //获取序列号
        List<String> sequences = EntityUtils.getAllSequences(list);
        List<GoodsCategory> categories = this.goodsCategoryService.getBySequences(sequences);

        List<GoodsCategoryListDTO> categoriesDTO = ObjectUtils.copying(categories, GoodsCategoryListDTO.class);
        List<GoodsCategoryListDTO> goodsCategoryListDTOS = TreeUtils.assembleTrees(categoriesDTO);

        return goodsCategoryListDTOS;
    }

    /**
     * 公共商城商品分类列表
     *
     * @param queryParam 商品分类查询参数
     *
     * @return 公共商城商品分类列表
     */
    @GetMapping("/public/mall-goods-category/list")
    public List<GoodsCategoryListDTO> pubilcMallCategoryList(GoodsCategoryQueryParam queryParam) {

        List<GoodsCategory> list = this.goodsCategoryService.getList(queryParam);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        //  获取序列号
        List<String> sequences = EntityUtils.getAllSequences(list);
        List<GoodsCategory> categories = this.goodsCategoryService.getBySequences(sequences);

        List<GoodsCategoryListDTO> categoriesDTO = ObjectUtils.copying(categories, GoodsCategoryListDTO.class);
        List<GoodsCategoryListDTO> goodsCategoryListDTOS = TreeUtils.assembleTrees(categoriesDTO);

        return goodsCategoryListDTOS;
    }

    /**
     * 商城商品分类ids
     *
     * @return ids
     */
    @PostMapping("/mall-goods-category/ids")
    public List<String> getByIdAndLevel(@RequestBody GoodsCategoryIdsParam param) {
        return this.goodsCategoryService.getByIdAndLevel(param);
    }

    /**
     * 商城商品分类app，id名称转换
     *
     * @return ids
     */
    @PostMapping("/get-name-by-ids/ids")
    public List<String> getNameByIds(@RequestBody List<String> ids) {
        return this.goodsCategoryService.getNameByIds(ids);
    }

    /**
     * 模糊查询获取序列号
     *
     * @param queryParam 查询参数
     *
     * @return
     */
    @GetMapping("/goods-category/sequence/list")
    public List<String> getSequences(GoodsCategoryQueryParam queryParam) {
        List<GoodsCategory> goodsCategories = this.goodsCategoryService.getList(queryParam);
        if (CollectionUtils.isEmpty(goodsCategories)) {
            return Collections.emptyList();
        }
        return goodsCategories.stream().map(BasicEnhancedTreeEntity::getSequence).collect(Collectors.toList());
    }

    /**
     * 商品品类批量更新（第三方品类数据同步使用）
     * 根据编码 相同就更新，不同就新增
     *
     * @param param 品类
     */
    @PostMapping("/third/goods-category/batchRenew")
    public void thirdCategoryBatchRenew(@RequestBody GoodsCategoryBatchRenewParam param) {
        this.goodsCategoryService.thirdCategoryBatchRenew(param);
    }

}
