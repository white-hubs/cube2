package com.guhecloud.cube.base.iot.product.model.v3.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProduct;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductPageParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductVO;

/**
 * 产品管理Service接口 v3版本
 */
public interface V3MdProductService extends IService<MdProduct> {

    /**
     * 保存产品
     * 自动生成prod_number,填充拼音和级联名称,生成版本快照
     *
     * @param param 产品保存参数
     * @return 产品ID
     */
    Long saveProduct(ProductSaveParam param);

    /**
     * 更新产品
     * 级联更新相关数据,生成版本快照
     *
     * @param prodId 产品ID
     * @param param 产品更新参数
     */
    void updateProduct(Long prodId, ProductSaveParam param);

    /**
     * 删除产品
     * 逻辑删除,检查设备依赖
     *
     * @param prodId 产品ID
     */
    void deleteProduct(Long prodId);

    /**
     * 查询产品详情
     * 关联查询属性、事件、方法
     *
     * @param prodId 产品ID
     * @return 产品VO(包含完整信息)
     */
    ProductVO getProductDetail(Long prodId);

    /**
     * 分页查询产品
     * 支持多条件筛选
     *
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<ProductVO> queryProductPage(ProductPageParam param);

    /**
     * 根据产品编号查询产品
     *
     * @param prodNumber 产品编号
     * @return 产品实体
     */
    MdProduct getByProdNumber(String prodNumber);
}
