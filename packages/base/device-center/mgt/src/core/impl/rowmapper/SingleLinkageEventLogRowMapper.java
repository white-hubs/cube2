package com.guhecloud.cube.iot.mgt.core.impl.rowmapper;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.iot.mgt.core.data.lang.BizType;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.CommonTriggerPart;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyStatus;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.PushBizLog;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.RuleTriggerLog;
import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.SingleLinkConditionResult;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.LinkageMetaEventLog;
import com.guhecloud.platform.base.util.DateUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SingleLinkageEventLogRowMapper implements RowMapper<RuleTriggerLog> {

    @Override
    public RuleTriggerLog mapRow(ResultSet rs, int i) throws SQLException {
        RuleTriggerLog linkageEventLog = new RuleTriggerLog();
        linkageEventLog.setRuleNum(rs.getString("ruleId"));
        linkageEventLog.setInstanceId(rs.getString("instanceId"));
        linkageEventLog.setTimestamp(String.valueOf(rs.getTimestamp("eventTime").getTime()));

        String type = rs.getString("bizType");
        String msg = rs.getString("msg");

        if (BizType.touch.name().equals(type)) {
            LinkageMetaEventLog linkageMetaEventLog = JSON.parseObject(msg, LinkageMetaEventLog.class);

            linkageEventLog.setNotifyStatus(NotifyStatus.initial.name());
            linkageEventLog.setNotifyStatusTs("-1");
            linkageEventLog.setRuleTriggerSnapshot(toCommonTriggerPart(linkageMetaEventLog.getMatchedConditionResults()));
        } else if (BizType.notify.name().equals(type)) {
            PushBizLog pushBizLog = JSON.parseObject(msg, PushBizLog.class);

            linkageEventLog.setNotifyStatus(pushBizLog.getNotifyStatus());
            linkageEventLog.setNotifyStatusTs(DateUtils.format(DateUtils.parse(pushBizLog.getNotifyTs())));
            linkageEventLog.setRuleTriggerSnapshot(toCommonTriggerPart(pushBizLog.getEventMessageList()));
        }

        return linkageEventLog;
    }

    private List<CommonTriggerPart> toCommonTriggerPart(Collection<SingleLinkConditionResult> linkageExpresses) {

        if (Objects.isNull(linkageExpresses) || linkageExpresses.isEmpty()) {
            return Collections.emptyList();
        }

        List<CommonTriggerPart> ruleTriggerSnapshot = new LinkedList<>();
        for (SingleLinkConditionResult linkageExpress : linkageExpresses) {
            CommonTriggerPart commonTriggerPart = new CommonTriggerPart();
            commonTriggerPart.setDeviceId(String.valueOf(linkageExpress.getDeviceId()));
            commonTriggerPart.setPropCode(linkageExpress.getPropertyCode());
            commonTriggerPart.setPropValue(String.valueOf(linkageExpress.getValue()));
            commonTriggerPart.setPropValueTs(String.valueOf(linkageExpress.getEventTime().getTime()));

            ruleTriggerSnapshot.add(commonTriggerPart);
        }

        return ruleTriggerSnapshot;
    }
}
