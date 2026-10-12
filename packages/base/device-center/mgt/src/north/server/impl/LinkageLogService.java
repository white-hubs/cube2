package com.guhecloud.cube.iot.mgt.north.server.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.CommonTriggerPart;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.RuleTriggerLog;
import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.RuleExecutionDetail;
import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.RuleTriggerSnapshot;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.LinkageEventHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.def.TimeSeriesInterface;
import com.guhecloud.cube.iot.mgt.north.param.LinkageLogPageParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
public class LinkageLogService {

    @Resource
    private TimeSeriesInterface timeSeriesInterface;

    /**
     * 联动事件历史记录
     *
     * @param param param
     *
     * @return IPage<RuleTriggerLog>
     */
    public Pagination<RuleTriggerLog> linkEventHistoryLogList(LinkageLogPageParam param) {
        LinkageEventHistoryQueryVo linkageEventHistoryQueryVo = new LinkageEventHistoryQueryVo();
        linkageEventHistoryQueryVo.setEndTs(param.getEndTs());
        linkageEventHistoryQueryVo.setStartTs(param.getStartTs());
        linkageEventHistoryQueryVo.setRuleNum(param.getRuleNum());
        linkageEventHistoryQueryVo.setTenantId(param.getTenantId());
        linkageEventHistoryQueryVo.setPageSize(param.getPageSize());
        linkageEventHistoryQueryVo.setPageNumber(param.getPageNumber());

        IPage<RuleTriggerLog> result = timeSeriesInterface.querySingleLinkagePage(linkageEventHistoryQueryVo);

        return Pagination.<RuleTriggerLog>builder()
                .total(result.getTotal())
                .pages(result.getPages())
                .records(result.getRecords())
                .build();
    }

    public RuleExecutionDetail linkEventDetails(String instanceId, String ruleNum) {

        LinkageEventHistoryQueryVo linkageEventHistoryQueryVo = new LinkageEventHistoryQueryVo();
        linkageEventHistoryQueryVo.setRuleNum(ruleNum);
        linkageEventHistoryQueryVo.setInstanceId(instanceId);
        linkageEventHistoryQueryVo.setPageSize(10);
        linkageEventHistoryQueryVo.setPageNumber(1);
        IPage<RuleTriggerLog> page = timeSeriesInterface.querySingleLinkagePage(linkageEventHistoryQueryVo);
        List<RuleTriggerLog> list = page.getRecords();
        Assert.isTrue(!list.isEmpty(), "未找到相关联动事件");

        RuleTriggerLog anyRuleTriggerLog = list.get(0);
        RuleExecutionDetail ruleExecutionDetail = new RuleExecutionDetail();

        // fill base detail info
        for (RuleTriggerLog ruleTriggerLog : list) {
            //通过触发时的日子还原规则联动信息
            if ("-1".equals(ruleTriggerLog.getNotifyStatusTs())) {
                ruleExecutionDetail.setRuleId(anyRuleTriggerLog.getRuleId());
                ruleExecutionDetail.setRuleNum(anyRuleTriggerLog.getRuleNum());
                ruleExecutionDetail.setInstanceId(anyRuleTriggerLog.getInstanceId());
                ruleExecutionDetail.setTimestamp(anyRuleTriggerLog.getTimestamp());
                ruleExecutionDetail.setNotifyStatus(anyRuleTriggerLog.getNotifyStatus());
                ruleExecutionDetail.setNotifyStatusTs(anyRuleTriggerLog.getNotifyStatusTs());
                ruleExecutionDetail.setRuleTriggerSnapshot(convert(anyRuleTriggerLog.getRuleTriggerSnapshot()));
            }
        }
        return ruleExecutionDetail;
    }

    private long convertToLong(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return -1;
        }

        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    private List<RuleTriggerSnapshot> convert(List<CommonTriggerPart> ruleTriggerSnapshot) {
        List<RuleTriggerSnapshot> snapshots = new LinkedList<>();
        for (CommonTriggerPart commonTriggerPart : ruleTriggerSnapshot) {
            RuleTriggerSnapshot rts = new RuleTriggerSnapshot();
            rts.setCondition(commonTriggerPart.getCondition());
            rts.setDeviceId(commonTriggerPart.getDeviceId());
            rts.setPropCode(commonTriggerPart.getPropCode());
            rts.setPropValue(commonTriggerPart.getPropValue());
            rts.setPropValueTs(commonTriggerPart.getPropValueTs());
            snapshots.add(rts);
        }
        return snapshots;
    }
}
