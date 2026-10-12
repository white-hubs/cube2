package com.guhecloud.cube.biz.mdata.server.bizlog.service;

import com.guhecloud.cube.biz.mdata.server.bizlog.param.BizLogRecordParam;
import com.guhecloud.cube.biz.mdata.server.bizlog.vo.BizLogRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.cloud.logger.message.BizLogRecordDTO;

import java.util.List;

public interface BizLogRecordService {
    /**
     * 添加日志记录
     *
     * @param bizLogRecord
     */
    void addBizLog(BizLogRecordDTO bizLogRecord);

    Pagination<BizLogRecordVO> queryBizLogRecord(BizLogRecordParam param);

    Pagination<BizLogRecordVO> queryBizLogRecordForLogin(BizLogRecordParam param);

    List<BizLogRecordVO> getBizLogRecordList(BizLogRecordParam param);

    List<BizLogRecordVO> getBizLogRecordListForLogin(BizLogRecordParam param);

    void clearBizLogInRedis();
}
