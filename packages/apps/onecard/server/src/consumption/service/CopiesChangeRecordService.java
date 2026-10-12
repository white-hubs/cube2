package com.guhecloud.cube.onecard.server.consumption.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.onecard.server.consumption.entity.CopiesChangeRecord;
import com.guhecloud.cube.onecard.server.consumption.param.CopiesRecordQueryParam;
import com.guhecloud.cube.onecard.server.consumption.vo.CopiesChangeRecordVo;
import com.guhecloud.cube.onecard.server.consumption.vo.CopiesChangeVo;
import com.guhecloud.platform.base.model.Pagination;

public interface CopiesChangeRecordService extends IService<CopiesChangeRecord> {
    Pagination<CopiesChangeRecordVo> getCopiesChangeRecord(CopiesRecordQueryParam param);

    CopiesChangeVo getShareStatistics(CopiesRecordQueryParam param);
}
