package com.guhecloud.cube.biz.smartcard.server.controller;

import com.guhecloud.cube.biz.smartcard.server.queryparam.AccessEventQueryParam;
import com.guhecloud.cube.biz.smartcard.server.queryparam.CardQueryParam;
import com.guhecloud.cube.biz.smartcard.server.service.DoorCardService;
import com.guhecloud.cube.biz.smartcard.server.vo.AccessEventVO;
import com.guhecloud.cube.biz.smartcard.server.vo.DoorCardVO;
import com.guhecloud.cube.biz.smartcard.server.vo.EventRecordVo;
import com.guhecloud.cube.biz.smartcard.server.vo.VisitingRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author appet
 * @Description 门禁卡相关业务
 * @date 2024-01-24 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SmartCardController {
    private final DoorCardService doorCardService;

    /**
     * 门禁卡分页查询
     */
    @GetMapping("/smartcard/card")
    public Pagination<DoorCardVO> getDoorCard(CardQueryParam param) {
        return doorCardService.getCardPage(param);
    }

    /**
     * 门禁访客记录分页查询
     */
    @GetMapping("/smartcard/visiting")
    public Pagination<VisitingRecordVO> getDoorVisitingRecord(AccessEventQueryParam param) {
        return doorCardService.getVisitingRecordPage(param);
    }

    /**
     * 门禁事件分页查询
     */
    @GetMapping("/smartcard/event")
    public Pagination<EventRecordVo> getDoorEvent(AccessEventQueryParam param) {
        return doorCardService.getDoorEventPage(param);
    }

    /**
     * 门禁访问事件分页查询
     * 包括：梯控事件TK 可视对讲事件 KSDJ
     */
    @GetMapping("/smartcard/access")
    public Pagination<AccessEventVO> getDoorAccessEvent(AccessEventQueryParam param) {
        return doorCardService.getAccessEventPage(param);
    }

}
