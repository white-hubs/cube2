package com.guhecloud.cube.base.iot.product.model.v3.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemPageParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductBrandModifyParam;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;

/**
 * 产品品牌服务接口
 * 提供品牌管理能力,支持文件关联
 */
public interface V3ProductBrandService {

    /**
     * 分页查询品牌(包含文件信息)
     *
     * @param param 查询参数
     * @return 分页结果
     */
    Page<DictItemVO> page(DictItemPageParam param);

    /**
     * 根据ID查询品牌详情(包含文件信息)
     *
     * @param id 品牌ID
     * @return 品牌VO
     */
    DictItemVO getById(Long id);

    /**
     * 新增品牌
     *
     * @param param 新增参数
     */
    void save(ProductBrandModifyParam param);

    /**
     * 修改品牌
     *
     * @param id 品牌ID
     * @param param 修改参数
     */
    void update(Long id, ProductBrandModifyParam param);

    /**
     * 删除品牌
     *
     * @param id 品牌ID
     */
    void delete(Long id);
}
