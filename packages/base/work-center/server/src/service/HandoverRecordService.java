package com.guhecloud.cube.biz.work.center.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.work.center.server.entity.HandoverRecord;
import com.guhecloud.cube.biz.work.center.server.param.HandTakeoverParam;
import com.guhecloud.cube.biz.work.center.server.param.HandoverRecordParam;
import com.guhecloud.cube.biz.work.center.server.param.HandoverRecordQueryParam;
import com.guhecloud.cube.biz.work.center.server.vo.HandoverRecordDetailVO;
import com.guhecloud.cube.biz.work.center.server.vo.HandoverRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/29 16:00
 * @Description:
 */
public interface HandoverRecordService extends IService<HandoverRecord> {

    /**
     * 新增交接记录
     *
     * @param param
     */
    Boolean addHandoverRecord(HandoverRecordParam param);

    /**
     * 接班
     *
     * @param param
     *
     * @return
     */
    void takeOver(HandTakeoverParam param);

    /**
     * 交接记录列表
     *
     * @param param
     *
     * @return
     */
    Pagination<HandoverRecordVO> getHandoverRecord(HandoverRecordQueryParam param);

    /**
     * 交接记录详情
     *
     * @param id
     *
     * @return
     */
    HandoverRecordDetailVO getHandoverRecordDetail(Long id);

    /**
     * 撤销交接
     *
     * @param id
     *
     * @return
     */
    boolean cancel(Long id);

    List<Integer> getTaskTenantList();

    List<HandoverRecordVO> getOverdueTask(Period period, Integer value);
}
