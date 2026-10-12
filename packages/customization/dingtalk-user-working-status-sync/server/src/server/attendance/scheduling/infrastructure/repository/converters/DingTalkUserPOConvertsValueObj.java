package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.converters;

import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.po.DingTalkUserRefPO;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.valueobj.DingTalkUserRef;

import java.util.List;
import java.util.stream.Collectors;

public final class DingTalkUserPOConvertsValueObj {

    private DingTalkUserPOConvertsValueObj() {

    }

    public static DingTalkUserRef convertDingTalkUserPOSToValueObj(DingTalkUserRefPO po) {
        var valueObj = new DingTalkUserRef();
        valueObj.setDingTalkUserId(po.getDingTalkUserId());
        valueObj.setSystemUserId(po.getSystemUserId());
        return valueObj;
    }

    public static List<DingTalkUserRef> convertDingTalkUserPOSTOValueObjs(List<DingTalkUserRefPO> pos) {
        return pos.stream().map(DingTalkUserPOConvertsValueObj::convertDingTalkUserPOSToValueObj).collect(Collectors.toList());
    }
}
