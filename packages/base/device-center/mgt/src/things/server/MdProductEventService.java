package com.guhecloud.cube.iot.mgt.things.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.mgt.things.model.MdProductEvent;
import com.guhecloud.cube.iot.mgt.things.vo.ProductEventVo;

import java.util.List;

public interface MdProductEventService extends IService<MdProductEvent> {

    void saveProdEvent(ProductEventVo productEventVo, boolean pushCreateEvent);

    ProductEventVo prodEventDetail(Long eventId);

    void updateProdEvent(ProductEventVo productEventVo);

    void deleteProdEvent(Long eventId);

    List<ProductEventVo> listProdEvent(Long prodId);
}
