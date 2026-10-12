package com.guhecloud.cube.srv.gen.reportmanagement.server.rocketmq;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.Report;
import com.guhecloud.cube.srv.gen.reportmanagement.server.enumeration.ReportFileSceneCodeEnum;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.service.ReportService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import instance.event.ProcessStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 工作报告流程发起成功消息处理
 * 目前只做数据统计处理
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = ProcessStartedMessage.TOPIC + "_REPORT_START", topic = ProcessStartedMessage.TOPIC,
                         selectorExpression = "REPORT", selectorType = SelectorType.TAG
)
public class ReportProcessStartedConsumer implements RocketMQListener<ProcessStartedMessage> {

    private final ReportService reportService;
    private final FileRefClient fileRefClient;

    @Override
    public void onMessage(ProcessStartedMessage message) {
        log.info("工作报告流程发起成功消息消费开始");
        SessionContextHolder.clearSessionContext();
        try {
            SessionContextHolder.requestTenant(message.getTenant());
            ReportAddOrUpdateParam param = reportService.synFlowData(message);
            long id = IdWorker.getId();
            Report copying = ObjectUtils.copying(param, Report.class);
            copying.setId(id);
            copying.setStatus(FlowResult.ACTIVE);
            //关联附件
            if (Objects.nonNull(param.getAppendixId())) {
                FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
                fileRefCreateParam.setBusinessKey(id);
                fileRefCreateParam.setFileId(param.getAppendixId());
                fileRefCreateParam.setSceneCode(ReportFileSceneCodeEnum.WORK_REPORT.getCode());
                this.fileRefClient.refInduce(fileRefCreateParam);
            }
            reportService.save(copying);
        } catch (Exception exception) {
            log.error("工作报告流程发起成功消息消费失败，失败原因：{}", exception);
        }
    }
}
