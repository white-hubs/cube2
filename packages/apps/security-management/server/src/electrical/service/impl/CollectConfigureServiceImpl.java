package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.dao.CollectConfigureMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.CollectConfigure;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.TransformerDistribution;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.job.ElectricalAcquisition;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.CollectConfigureParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.CollectConfigureService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.TransformerDistributionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.CollectConfigureVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.TransformerDistributionVo;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectIdUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.job.jobtask.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@EventBusListen(order = 2)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectConfigureServiceImpl extends ServiceImpl<CollectConfigureMapper, CollectConfigure> implements CollectConfigureService {

    private final TransformerDistributionService transformerDistributionService;
    private final JobService jobService;
    private static final String POWER_JOB_NAME = "collectConfigure";
    private static final String POWER_CALL_BACK_METHOD = "addDistribution";
    private static final String POWER_JOB_DESC = "配电采集";

    private static final String CONSUMPTION_JOB_NAME = "collectConsumption";
    private static final String CONSUMPTION_CALL_BACK_METHOD = "addElectricity";
    private static final String CONSUMPTION_JOB_DESC = "用电采集";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCollectConfigure(CollectConfigureParam param) {
        CollectConfigure one = this.getOne(Wrappers.<CollectConfigure>lambdaQuery()
                                                   .eq(CollectConfigure::getApplicationCode, param.getApplicationCode()));
        if (Objects.nonNull(one)) {
            if (StringUtils.isBlank(one.getDistributionTaskId())) {
                one.setDistributionTaskId(ObjectIdUtils.next());
            }
            String cron1 = "0 0 0/" + param.getTimeCycle() + " * * ?";
            updateJob(one.getDistributionTaskId(), POWER_JOB_NAME, cron1, POWER_JOB_DESC);
            if (StringUtils.isBlank(one.getConsumptionTaskId())) {
                one.setConsumptionTaskId(ObjectIdUtils.next());
            }
            String[] split = param.getReadeTime().split(":");
            String cron2 = split[2] + " " + split[1] + " " + split[0] + " * * ?";
            updateJob(one.getConsumptionTaskId(), CONSUMPTION_JOB_NAME, cron2, CONSUMPTION_JOB_DESC);
            one.setReadeTime(param.getReadeTime());
            one.setTimeCycle(param.getTimeCycle());
            this.saveOrUpdate(one);
        } else {
            CollectConfigure copying = ObjectUtils.copying(param, CollectConfigure.class);
            copying.setDistributionTaskId(ObjectIdUtils.next());
            copying.setConsumptionTaskId(ObjectIdUtils.next());
            String cron1 = "0 0 0/" + param.getTimeCycle() + " * * ?";
            makeJob(copying.getDistributionTaskId(), POWER_JOB_NAME, cron1, POWER_CALL_BACK_METHOD, POWER_JOB_DESC);
            String[] split = param.getReadeTime().split(":");
            String cron2 = split[2] + " " + split[1] + " " + split[0] + " * * ?";
            makeJob(copying.getConsumptionTaskId(), CONSUMPTION_JOB_NAME, cron2, CONSUMPTION_CALL_BACK_METHOD, CONSUMPTION_JOB_DESC);
            this.save(copying);
        }

        if (CollectionUtils.isNotEmpty(param.getTransformers())) {
            transformerDistributionService.remove(null);
            List<TransformerDistribution> copying = ObjectUtils.copying(param.getTransformers(), TransformerDistribution.class);
            transformerDistributionService.saveBatch(copying);
        }

    }

    @Override
    public CollectConfigureVo getCollectConfigure(String applicationCode) {
        CollectConfigure one = this.getOne(Wrappers.<CollectConfigure>lambdaQuery()
                                                   .eq(CollectConfigure::getApplicationCode, applicationCode));
        if (Objects.nonNull(one)) {
            CollectConfigureVo copying = ObjectUtils.copying(one, CollectConfigureVo.class);
            List<TransformerDistribution> list = transformerDistributionService.list();
            List<TransformerDistributionVo> transformerDistributionVos = ObjectUtils.copying(list, TransformerDistributionVo.class);
            if (CollectionUtils.isNotEmpty(transformerDistributionVos)) {
                copying.setTransformerDistributionVos(transformerDistributionVos);
            }
            return copying;
        } else {
            return null;
        }

    }

    //创建任务抄表任务
    private void makeJob(String id, String name, String cron, String jobMethod, String desc) {
        jobService.addJob(String.format("%s_%s", name, id),
                          ApplicationContextHolder.getBean((Class<?>) ElectricalAcquisition.class),
                          jobMethod,
                          cron,
                          desc,
                          SessionContextHolder.getRequester().getTenant().toString(),
                          true);
    }

    private void updateJob(String id, String name, String cron, String desc) {
        String jobName = String.format("%s_%s", name, id);
        boolean updateJobTask = jobService.updateJob(jobName, cron, desc, SessionContextHolder.getRequester()
                .getTenant()
                .toString());
        if (log.isDebugEnabled()) {
            log.debug("collect configure update job result:{}", updateJobTask);
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void initRegistryJob(SysReadEvent event) {
        log.info("collect configure plan init job");
        SessionContextHolder.clearSessionContext();
        var list = this.lambdaQuery().list();
        list.forEach(item -> {
            SessionContextHolder.requestTenant(item.getTenant());
            if (StringUtils.isBlank(item.getDistributionTaskId())) {
                item.setDistributionTaskId(ObjectIdUtils.next());
            }
            if (StringUtils.isBlank(item.getConsumptionTaskId())) {
                item.setConsumptionTaskId(ObjectIdUtils.next());
            }
            var cron1 = "0 0 0/" + item.getTimeCycle() + " * * ?";
            makeJob(item.getDistributionTaskId(), POWER_JOB_NAME, cron1, POWER_CALL_BACK_METHOD, POWER_JOB_DESC);
            var split = item.getReadeTime().split(":");
            var cron2 = split[2] + " " + split[1] + " " + split[0] + " * * ?";
            makeJob(item.getConsumptionTaskId(), CONSUMPTION_JOB_NAME, cron2, CONSUMPTION_CALL_BACK_METHOD, CONSUMPTION_JOB_DESC);
            this.updateById(item);
            SessionContextHolder.clearSessionContext();
        });
    }
}
