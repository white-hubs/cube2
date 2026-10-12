package com.guhecloud.cube.base.iot.product.model.v3.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductMethod;
import com.guhecloud.cube.base.iot.product.model.v3.param.MethodSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductMethodVO;

import java.util.List;

/**
 * 方法管理Service接口 v3版本(新增功能)
 */
public interface V3MdProductMethodService extends IService<MdProductMethod> {

    /**
     * 保存方法
     * 校验method_code在产品下的唯一性
     *
     * @param param 方法保存参数
     * @return 方法ID
     */
    Long saveMethod(MethodSaveParam param);

    /**
     * 更新方法
     *
     * @param methodId 方法ID
     * @param param 方法更新参数
     */
    void updateMethod(Long methodId, MethodSaveParam param);

    /**
     * 删除方法
     *
     * @param methodId 方法ID
     */
    void deleteMethod(Long methodId);

    /**
     * 根据产品ID查询方法列表
     *
     * @param prodId 产品ID
     * @return 方法VO列表
     */
    List<ProductMethodVO> listByProdId(Long prodId);

    /**
     * 查询方法详情
     *
     * @param methodId 方法ID
     * @return 方法VO
     */
    ProductMethodVO getMethodDetail(Long methodId);

    /**
     * 根据产品ID和方法编码查询方法
     *
     * @param prodId 产品ID
     * @param methodCode 方法编码
     * @return 方法实体
     */
    MdProductMethod getByProdIdAndMethodCode(Long prodId, String methodCode);

    /**
     * 批量保存方法
     *
     * @param prodId 产品ID
     * @param params 方法保存参数列表
     */
    void batchSave(Long prodId, List<MethodSaveParam> params);
}
