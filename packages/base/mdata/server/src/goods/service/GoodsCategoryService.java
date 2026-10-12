package com.guhecloud.cube.biz.mdata.server.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryBatchRenewParam;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryCreateParam;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryIdsParam;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryQueryParam;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryUpdateParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface GoodsCategoryService extends IService<GoodsCategory> {

    /**
     * 商品分类新增
     *
     * @param param 商品分类新增参数
     */
    void save(GoodsCategoryCreateParam param);

    /**
     * 商品分类更新
     *
     * @param param 商品分类更新参数
     */
    void update(GoodsCategoryUpdateParam param);

    /**
     * 根据主键集合获取主键信息
     *
     * @param ids 主键集合
     *
     * @return 商品分类集合
     */
    List<GoodsCategory> getByIds(List<String> ids);

    /**
     * 商品分类列表
     *
     * @param queryParam 商品分类查询参数
     *
     * @return 商品分类列表结合
     */
    List<GoodsCategory> getList(GoodsCategoryQueryParam queryParam);

    /**
     * 小程序商品分类列表
     *
     * @param queryParam 查询参数
     *
     * @return 商品分类信息
     */
    List<GoodsCategory> getAppList(GoodsCategoryQueryParam queryParam);

    /**
     * 根据序列号获取商品分类信息
     *
     * @param sequences 序列号集合
     *
     * @return 商品分类信息
     */
    List<GoodsCategory> getBySequences(List<String> sequences);

    /**
     * 根据序列号获取三级商品分类信息
     *
     * @param sequences 序列号
     *
     * @return 商品分类信息
     */
    List<GoodsCategory> getThirdLevel(List<String> sequences);

    /**
     * 根据ids获取三级商品分类信息
     *
     * @param ids
     *
     * @return
     */
    List<GoodsCategory> getThirdLevelByIds(List<Long> ids);

    /**
     * 获取最后一级品类信息
     *
     * @param ids 品类主键集合
     *
     * @return 品类信息
     */
    List<GoodsCategory> getLastLevelByIds(Collection<? extends Serializable> ids);

    /**
     * 商品品类删除
     *
     * @param id 商品品类id
     */
    void remove(Long id);

    /**
     * 根据名称获取商品分类
     *
     * @param name 名称
     *
     * @return 商品分类信息
     */
    GoodsCategory getByName(String name);

    /**
     * 根据编号获取商品分类
     *
     * @param code 商品分类编号
     *
     * @return 商品分类信息
     */
    GoodsCategory getByCode(String code);

    /**
     * 根据父类主键获取商品分类
     *
     * @param parentId 商品分类父类主键
     *
     * @return 商品分类信息
     */
    List<GoodsCategory> getByParentId(Long parentId);

    /**
     * @Description 根据等级及id查ids
     * @Author
     * @Param
     * @Return
     * @Exception
     */
    List<String> getByIdAndLevel(GoodsCategoryIdsParam param);

    List<String> getNameByIds(List<String> ids);

    void thirdCategoryBatchRenew(GoodsCategoryBatchRenewParam param);
}
