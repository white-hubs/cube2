package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.po.DingTalkUserRefPO;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.valueobj.DingTalkUserRef;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.converters.DingTalkUserPOConvertsValueObj;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.converters.DingTalkUserRefConvertsPO;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.mapper.DingTalkUserRefMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
public class DingTalkUserRefRepository extends ServiceImpl<DingTalkUserRefMapper, DingTalkUserRefPO> {

    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<DingTalkUserRef> dingTalkUserRefList) {
        this.saveBatch(DingTalkUserRefConvertsPO.convertDingTalkUserRefsTOPOS(dingTalkUserRefList), 500);
    }

    public List<DingTalkUserRef> getListByDingTalkUserIds(Set<String> dingTalkUserIds) {
        if (CollectionUtils.isEmpty(dingTalkUserIds)) {
            return Collections.emptyList();
        }
        var dingTalkUserRefPOS = this.list(Wrappers.<DingTalkUserRefPO>lambdaQuery()
                                                   .in(DingTalkUserRefPO::getDingTalkUserId, dingTalkUserIds));
        return DingTalkUserPOConvertsValueObj.convertDingTalkUserPOSTOValueObjs(dingTalkUserRefPOS);
    }

    public List<DingTalkUserRef> getListBySystemUserIds(Set<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        var dingTalkUserRefPOS = this.list(Wrappers.<DingTalkUserRefPO>lambdaQuery()
                                                   .in(DingTalkUserRefPO::getSystemUserId, userIds));
        return DingTalkUserPOConvertsValueObj.convertDingTalkUserPOSTOValueObjs(dingTalkUserRefPOS);
    }
}
