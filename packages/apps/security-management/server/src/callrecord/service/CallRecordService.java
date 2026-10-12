package com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.entity.CallRecord;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.param.CallRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo.BedFileVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo.CallRecordVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo.ProcessingRecordsVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.Map;

public interface CallRecordService extends IService<CallRecord> {

    Map<String, Object> getCallStatistics();

    Map<String, Object> getCallTrends(CallRecordParam param);

    Pagination<CallRecordVo> getCallRecordsPage(CallRecordParam param);

    Pagination<ProcessingRecordsVo> getProcessingRecordsPage(CallRecordParam param);

    Pagination<BedFileVo> getBedFilePage(CallRecordParam param);
}
