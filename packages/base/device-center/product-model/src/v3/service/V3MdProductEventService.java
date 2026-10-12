package com.guhecloud.cube.base.iot.product.model.v3.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductEvent;
import com.guhecloud.cube.base.iot.product.model.v3.param.EventSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductEventVO;

import java.util.List;

/**
 * 事件管理Service接口 v3版本
 */
public interface V3MdProductEventService extends IService<MdProductEvent> {

    /**
     * 保存事件
     * 校验event_code在产品下的唯一性
     *
     * @param param 事件保存参数
     * @return 事件ID
     */
    Long saveEvent(EventSaveParam param);

    /**
     * 更新事件
     *
     * @param eventId 事件ID
     * @param param 事件更新参数
     */
    void updateEvent(Long eventId, EventSaveParam param);

    /**
     * 删除事件
     *
     * @param eventId 事件ID
     */
    void deleteEvent(Long eventId);

    /**
     * 根据产品ID查询事件列表
     *
     * @param prodId 产品ID
     * @return 事件VO列表
     */
    List<ProductEventVO> listByProdId(Long prodId);

    /**
     * 查询事件详情
     *
     * @param eventId 事件ID
     * @return 事件VO
     */
    ProductEventVO getEventDetail(Long eventId);

    /**
     * 批量保存事件
     *
     * @param prodId 产品ID
     * @param params 事件保存参数列表
     */
    void batchSave(Long prodId, List<EventSaveParam> params);

    /**
     * 查询事件类别列表
     * 无租户查询,返回全局事件类别
     *
     * @return 事件类别列表
     */
    List<DictItemVO> listEventCategories();
}
