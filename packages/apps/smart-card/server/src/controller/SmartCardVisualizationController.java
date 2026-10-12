package com.guhecloud.cube.biz.smartcard.server.controller;

import com.guhecloud.cube.biz.smartcard.server.queryparam.AccessEventQueryParam;
import com.guhecloud.cube.biz.smartcard.server.queryparam.CommonStatQueryParam;
import com.guhecloud.cube.biz.smartcard.server.service.DoorCardService;
import com.guhecloud.cube.biz.smartcard.server.vo.CardStatByStatusVO;
import com.guhecloud.cube.biz.smartcard.server.vo.CommonStatVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 门禁管理可视化
 *
 * @author : lijie
 * @date : 2023-3-21
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SmartCardVisualizationController {
    private final DoorCardService doorCardService;

    /**
     * 按使用状态统计卡的数量
     *
     * @return
     */
    @GetMapping("/smartcard/card/useStatus/stat")
    public CardStatByStatusVO getCardStatByStatus() {
        return doorCardService.getCardStatByStatus();
    }

    /**
     * 访客趋势统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/smartcard/visiting/trend/stat")
    public List<CommonStatVO> getVisitingRecordStatByTime(CommonStatQueryParam param) {
        return doorCardService.getVisitingRecordStatByTime(param);
    }

    /**
     * 门禁事件按名称统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/smartcard/event/name/stat")
    public List<CommonStatVO> getDoorEventStatByName(AccessEventQueryParam param) {
        return doorCardService.getDoorEventStatByName(param);
    }

    /**
     * 访问事件按名称统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/smartcard/access/name/stat")
    public List<CommonStatVO> getAccessEventStatByName(AccessEventQueryParam param) {
        return doorCardService.getAccessEventStatByName(param);
    }

}
