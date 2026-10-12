package com.guhecloud.cube.biz.smartcard.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.smartcard.server.entity.Card;
import com.guhecloud.cube.biz.smartcard.server.queryparam.AccessEventQueryParam;
import com.guhecloud.cube.biz.smartcard.server.queryparam.CardQueryParam;
import com.guhecloud.cube.biz.smartcard.server.queryparam.CommonStatQueryParam;
import com.guhecloud.cube.biz.smartcard.server.vo.AccessEventVO;
import com.guhecloud.cube.biz.smartcard.server.vo.CardStatByStatusVO;
import com.guhecloud.cube.biz.smartcard.server.vo.CommonStatVO;
import com.guhecloud.cube.biz.smartcard.server.vo.DoorCardVO;
import com.guhecloud.cube.biz.smartcard.server.vo.EventRecordVo;
import com.guhecloud.cube.biz.smartcard.server.vo.VisitingRecordVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface DoorCardService extends IService<Card> {

    Pagination<DoorCardVO> getCardPage(CardQueryParam param);

    /**
     * 访客
     *
     * @param param
     *
     * @return
     */
    Pagination<VisitingRecordVO> getVisitingRecordPage(AccessEventQueryParam param);

    /**
     * 访问事件（包括：梯控、可视对讲等）
     *
     * @param param
     *
     * @return
     */
    Pagination<AccessEventVO> getAccessEventPage(AccessEventQueryParam param);

    /**
     * 门禁事件
     *
     * @param param
     *
     * @return
     */
    Pagination<EventRecordVo> getDoorEventPage(AccessEventQueryParam param);

    /**
     * 按使用状态统计数量
     *
     * @return
     */
    CardStatByStatusVO getCardStatByStatus();

    /**
     * 按时间统计访问量
     *
     * @param param
     *
     * @return
     */
    List<CommonStatVO> getVisitingRecordStatByTime(CommonStatQueryParam param);

    /**
     * 按事件名称统计
     *
     * @param param
     *
     * @return
     */
    List<CommonStatVO> getAccessEventStatByName(AccessEventQueryParam param);

    /**
     * 按门禁事件名称统计
     *
     * @param param
     *
     * @return
     */
    List<CommonStatVO> getDoorEventStatByName(AccessEventQueryParam param);

    void insertOrUpdate(Card card);
}
