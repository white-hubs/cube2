package com.guhecloud.cube.iot.mgt.things.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.mgt.things.model.MdProductMethod;
import com.guhecloud.cube.iot.mgt.things.vo.ProductMethodBatchVo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductMethodMgtVo;

import java.util.List;

public interface MdProductMethodService extends IService<MdProductMethod> {

    void saveProdMethod(ProductMethodMgtVo productMethodMgtVo);

    void updateProdMethod(ProductMethodMgtVo productMethodMgtVo);

    void deleteProdMethod(Long methodId);

    ProductMethodMgtVo prodMethodDetail(Long methodId);

    List<ProductMethodMgtVo> listProdMethod(Long prodId);

    /**
     * 批量保存或更新产品方法（覆盖模式）
     * 前端传递的方法列表将覆盖数据库中的现有方法
     * @param batchVo 包含产品ID和方法列表的对象
     */
    void saveOrUpdateProdMethods(ProductMethodBatchVo batchVo);
}
