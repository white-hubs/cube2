package com.guhecloud.cube.srv.gen.official.management.server.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkMapper;
import com.guhecloud.cube.srv.gen.official.management.server.enumeration.FocusWorkStatus;
import com.guhecloud.cube.srv.gen.official.management.server.message.event.FocusWorkDelayEvent;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FocusWorkMonitor {
    private final FocusWorkMapper focusWorkMapper;
    private final AssemblyNoticeClient assemblyNoticeClient;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 每分钟监测 督办完成
     * 超时：将【进行中】的督办事项变成【未完成】
     */
    @JobAnnotation(name = "focusWorkStatusMonitor", cron = "0 0/1 * * * ?", desc = "人事-督办超时（未完成）监控")
    public void focusWorkStatusMonitor(String param) {
        try {
            log.info("督办任务已超时提醒...");
            //查询出所有没有完成的任务
            var focusWorkPOS = this.focusWorkMapper.selectList(Wrappers.<FocusWorkPO>lambdaQuery()
                                                                       .eq(FocusWorkPO::getStatus, FocusWorkStatus.PROCESSING));
            log.info("督办任务已超时,检查数据：{}", focusWorkPOS.size());
            if (CollectionUtils.isNotEmpty(focusWorkPOS)) {
                //获取当天0点时间
                var currentTime = LocalDateTime.now();
                focusWorkPOS.forEach(f -> {
                    if ((f.getUpdateCompleteTime() != null && f.getUpdateCompleteTime().isBefore(currentTime))
                        ||
                        (f.getUpdateCompleteTime() == null && f.getCompleteTime() != null && f.getCompleteTime().isBefore(currentTime))) {
                        SessionContextHolder.requestTenant(f.getTenant());
                        f.setStatus(FocusWorkStatus.UNDONE.name());
                        this.focusWorkMapper.updateById(f);
                        //推送消息 督办事项已经超期
                        applicationEventPublisher.publishEvent(new FocusWorkDelayEvent(this, f, 0));
                    }
                });
            }
            SessionContextHolder.requestTenant(null);
        } catch (Exception e) {
            log.error("督办任务已超时提醒,异常：", e);
        }
    }

    /**
     * 每分钟 检查督办任务超时情况
     */
    @JobAnnotation(name = "focusWorkOverTimeMonitor", cron = "0 0/1 * * * ?", desc = "人事-督办任务即将超时监控")
    public void focusWorkOverTimeMonitor(String param) {
        try {
            log.info("督办任务即将超时提醒....");
            //查询出所有没有完成的任务
            var focusWorkPOS = this.focusWorkMapper.selectList(Wrappers.<FocusWorkPO>lambdaQuery()
                                                                       .eq(FocusWorkPO::getStatus, FocusWorkStatus.PROCESSING));
            log.info("督办任务即将超时,检查数据：" + focusWorkPOS.size());
            if (CollectionUtils.isNotEmpty(focusWorkPOS)) {
                //获取当前时间
                var currentTime = LocalDateTime.now();
                focusWorkPOS.forEach(f -> {
                    SessionContextHolder.requestTenant(f.getTenant());
                    //获取即将超时的配置
                    var willOverTimeNoticeMinute = this.getWillOverTimeNoticeMinute();
                    long towDateMinute;
                    if (f.getUpdateCompleteTime() != null) {
                        towDateMinute = DateUtils.between(currentTime, f.getUpdateCompleteTime(), ChronoUnit.MINUTES);
                    } else {
                        towDateMinute = DateUtils.between(currentTime, f.getCompleteTime(), ChronoUnit.MINUTES);
                    }
                    log.info("督办任务即将超时:" + f.getId() + " 即将超时时间：" + willOverTimeNoticeMinute + " 当前相差：" + towDateMinute);
                    //与即将超时时间比较
                    if (willOverTimeNoticeMinute != null &&
                        towDateMinute >= willOverTimeNoticeMinute &&
                        towDateMinute < willOverTimeNoticeMinute + 1) {
                        //推送即将到期的消息
                        applicationEventPublisher.publishEvent(new FocusWorkDelayEvent(this, f, willOverTimeNoticeMinute));
                    }
                });
            }
            SessionContextHolder.requestTenant(null);
        } catch (Exception e) {
            log.error("督办任务即将超时提醒：", e);
        }
    }

    //获取将要超时的时间配置
    private Integer getWillOverTimeNoticeMinute() {
        MessageSendSceneQueryPram queryPram = new MessageSendSceneQueryPram();
        queryPram.setBusinessType("OFFICIAL_MANAGEMENT");
        queryPram.setSceneCodes(Arrays.asList("FOCUS_WORK_WILL_OVERTIME_PUBLISHED_NOTIFY"));
        List<MessageSendConfig> messageSendConfigs = assemblyNoticeClient.getSendMsgConfig(queryPram);
        if (CollectionUtils.isNotEmpty(messageSendConfigs)) {
            return messageSendConfigs.get(0).getValue();
        }
        return null;
    }

}
