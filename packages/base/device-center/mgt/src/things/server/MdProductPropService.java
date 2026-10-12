package com.guhecloud.cube.iot.mgt.things.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.mgt.things.model.MdProductProp;
import com.guhecloud.cube.iot.mgt.things.vo.ProductPropVo;

import java.util.List;

public interface MdProductPropService extends IService<MdProductProp> {

    void saveProdProp(ProductPropVo productPropVo, boolean pushCreateEvent);

    void updateProdProp(ProductPropVo productPropVo);

    void deleteProdProp(Long propId);

    ProductPropVo prodPropDetail(Long propId);

    List<ProductPropVo> listProdProp(Long prodId);

}
