package com.guhecloud.cube.srv.gen.visitor.management.server.service;

import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitorStatusChangeRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitorStatusChangeRecordResult;

import java.util.List;

/**
 * 访客状态变更记录
 */
public interface VisitorStatusChangeRecordService {
    List<VisitorStatusChangeRecordResult> visitorPassTrend(VisitorStatusChangeRecordParam param);
}
