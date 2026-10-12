package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.converters;

import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.po.DingTalkUserRefPO;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.valueobj.DingTalkUserRef;

import java.util.List;

public final class DingTalkUserRefConvertsPO {

    private DingTalkUserRefConvertsPO() {

    }

    public static DingTalkUserRefPO convertDingTalkUserRefTOPO(DingTalkUserRef dingTalkUserRef) {
        var po = new DingTalkUserRefPO();
        po.setDingTalkUserId(dingTalkUserRef.getDingTalkUserId());
        po.setSystemUserId(dingTalkUserRef.getSystemUserId());
        return po;
    }

    public static List<DingTalkUserRefPO> convertDingTalkUserRefsTOPOS(List<DingTalkUserRef> dingTalkUserRefs) {
        return dingTalkUserRefs.stream().map(DingTalkUserRefConvertsPO::convertDingTalkUserRefTOPO).toList();
    }
}
