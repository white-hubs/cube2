package com.guhecloud.cube.iot.mgt.things.controller;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.things.server.MdProductEventService;
import com.guhecloud.cube.iot.mgt.things.vo.EventConfig;
import com.guhecloud.cube.iot.mgt.things.vo.ProductEventVo;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import static com.guhecloud.cube.iot.mgt.Const.IOT_MGT_FRONTEND_SUFFIX;

@Slf4j
@RestController
@RequestMapping(IOT_MGT_FRONTEND_SUFFIX + "/things/product-event")
public class ProductEventController {

    @Resource
    private MdProductEventService mdProductEventService;

    @GetMapping("/list")
    public List<ProductEventVo> listProdEvent(@RequestParam Long prodId) {
        return mdProductEventService.listProdEvent(prodId);
    }

    @PostMapping("")
    public void saveProdEvent(@RequestBody @NonNull ProductEventVo productEventVo) throws IOException {
        //序列化麻烦,通过JSONObject转换
        EventConfig eventConfig = productEventVo.getEventConfig();
        Assert.notNull(eventConfig, "eventConfig is null");

        mdProductEventService.saveProdEvent(productEventVo, true);
    }

    @GetMapping("/{eventId}")
    public ProductEventVo prodEventDetail(@PathVariable("eventId") Long eventId) {
        return mdProductEventService.prodEventDetail(eventId);
    }

    @PutMapping("/{eventId}")
    public void updateProdEvent(@RequestBody @NonNull ProductEventVo productEventVo,
                                @PathVariable("eventId") Long eventId
                               ) throws IOException {
        //序列化麻烦,通过JSONObject转换
        EventConfig eventConfig = productEventVo.getEventConfig();
        Assert.notNull(eventConfig, "eventConfig is null");

        productEventVo.setId(eventId);
        mdProductEventService.updateProdEvent(productEventVo);
    }

    @DeleteMapping("/{eventId}")
    public void deleteProdEvent(@PathVariable("eventId") Long eventId) {
        mdProductEventService.deleteProdEvent(eventId);
    }
}
