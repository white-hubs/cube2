package com.guhecloud.cube.base.iot.product.model.v3.controller;

import com.guhecloud.cube.base.iot.product.model.v3.param.EventSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3MdProductEventService;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductEventVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 事件管理Controller v3版本
 * 路径前缀: /v3/things/product-event
 */
@Slf4j
@RestController
@RequestMapping("/v3/things/product-event")
@RequiredArgsConstructor
public class V3ProductEventController {

    private final V3MdProductEventService eventService;

    /**
     * 创建事件
     *
     * @param param 事件保存参数
     * @return 事件ID
     */
    @PostMapping
    public Long createEvent(@Validated @RequestBody EventSaveParam param) {
        log.info("创建事件接口调用, prodId={}, eventCode={}", param.getProdId(), param.getEventCode());
        return eventService.saveEvent(param);
    }

    /**
     * 更新事件
     *
     * @param id 事件ID
     * @param param 事件更新参数
     */
    @PutMapping("/{id}")
    public void updateEvent(@PathVariable Long id, @Validated @RequestBody EventSaveParam param) {
        log.info("更新事件接口调用, eventId={}", id);
        eventService.updateEvent(id, param);
    }

    /**
     * 删除事件
     *
     * @param id 事件ID
     */
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        log.info("删除事件接口调用, eventId={}", id);
        eventService.deleteEvent(id);
    }

    /**
     * 查询事件详情
     *
     * @param id 事件ID
     * @return 事件VO
     */
    @GetMapping("/{id}")
    public ProductEventVO getEvent(@PathVariable Long id) {
        log.info("查询事件详情接口调用, eventId={}", id);
        return eventService.getEventDetail(id);
    }

    /**
     * 查询事件列表(按产品ID)
     *
     * @param prodId 产品ID
     * @return 事件列表
     */
    @GetMapping("/list")
    public List<ProductEventVO> listByProduct(@RequestParam Long prodId) {
        log.info("查询产品事件列表接口调用, prodId={}", prodId);
        return eventService.listByProdId(prodId);
    }

    /**
     * 批量创建事件
     *
     * @param prodId 产品ID
     * @param params 事件保存参数列表
     */
    @PostMapping("/batch")
    public void batchCreate(@RequestParam Long prodId, @Validated @RequestBody List<EventSaveParam> params) {
        log.info("批量创建事件接口调用, prodId={}, count={}", prodId, params.size());
        eventService.batchSave(prodId, params);
    }

    /**
     * 查询事件类别列表
     * 无租户限制,返回全局事件类别
     *
     * @return 事件类别列表
     */
    @GetMapping("/categories")
    public List<DictItemVO> listEventCategories() {
        log.info("查询事件类别列表接口调用");
        return eventService.listEventCategories();
    }
}
