package com.guhecloud.cube.srv.gen.visitor.management.server.vo.param;

import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitorStatusChangeRecordPO;
import lombok.Data;

import java.util.List;

/**
 * 访客状态变更回调入参
 */
@Data
public class VisitorStatusCallbackParam {
    private List<VisitorStatusChangeRecordPO> visitors;
}
