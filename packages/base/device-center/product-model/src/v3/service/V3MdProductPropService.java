package com.guhecloud.cube.base.iot.product.model.v3.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductProp;
import com.guhecloud.cube.base.iot.product.model.v3.param.PropSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductPropVO;

import java.util.List;

/**
 * 属性管理Service接口 v3版本
 */
public interface V3MdProductPropService extends IService<MdProductProp> {

    /**
     * 保存属性
     * 校验prop_code在产品下的唯一性
     *
     * @param param 属性保存参数
     * @return 属性ID
     */
    Long saveProp(PropSaveParam param);

    /**
     * 更新属性
     *
     * @param propId 属性ID
     * @param param 属性更新参数
     */
    void updateProp(Long propId, PropSaveParam param);

    /**
     * 删除属性
     *
     * @param propId 属性ID
     */
    void deleteProp(Long propId);

    /**
     * 根据产品ID查询属性列表
     *
     * @param prodId 产品ID
     * @return 属性VO列表
     */
    List<ProductPropVO> listByProdId(Long prodId);

    /**
     * 查询属性详情
     *
     * @param propId 属性ID
     * @return 属性VO
     */
    ProductPropVO getPropDetail(Long propId);

    /**
     * 根据产品ID和属性编码查询属性
     *
     * @param prodId 产品ID
     * @param propCode 属性编码
     * @return 属性实体
     */
    MdProductProp getByProdIdAndPropCode(Long prodId, String propCode);

    /**
     * 批量保存属性
     *
     * @param prodId 产品ID
     * @param params 属性保存参数列表
     */
    void batchSave(Long prodId, List<PropSaveParam> params);
}
