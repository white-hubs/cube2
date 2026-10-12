package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterReadeCycleMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterReadeCycle;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.MeterReadingCycleEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.job.EnergyMonitorJob;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterReadeCycleParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.MeterReadeCycleQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.MeterReadeCycleService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterReadeCycleVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectIdUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.job.jobtask.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：抄表周期服务层
 *
 * @author lrj
 * 2022-07-04 15:12:36
 */
@Slf4j
@Service
@EventBusListen(order = 2)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeterReadeCycleServiceImpl extends ServiceImpl<MeterReadeCycleMapper, MeterReadeCycle> implements MeterReadeCycleService {

    private final JobService jobService;
    private static final String JOB_NAME = "ReadeCycleTask";
    private static final String CALL_BACK_METHOD = "executeReadeDevice";
    private static final String JOB_DESC = "能耗抄表周期";

    @Override
    public void addOrUpdate(MeterReadeCycleParam param) {
        List<MeterReadeCycle> all = this.list();//应该只有一条
        //删除所有的数据
        all.forEach(data -> {
            if (StringUtils.isNotEmpty(data.getTaskId())) {
                jobService.removeJob(data.getTaskId());
            }
            this.removeById(data);
        });
        //保存现有的测略
        this.add(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(MeterReadeCycleParam param) {
        MeterReadeCycle meterReadeCycle = ObjectUtils.copying(param, MeterReadeCycle.class);
        if (StringUtils.isBlank(meterReadeCycle.getTaskId())) {
            meterReadeCycle.setTaskId(getJobName(ObjectIdUtils.next()));
        }
        createJob(meterReadeCycle.getTaskId(), getCron(param));
        this.baseMapper.insert(meterReadeCycle);
        return meterReadeCycle.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, MeterReadeCycleParam param) {
        var meterReadeCycle = this.baseMapper.selectById(id);
        Assert.isNotFound(meterReadeCycle, "未查询到相关信息");
        ObjectUtils.copying(param, meterReadeCycle);
        //删除之前的job
        if (StringUtils.isNotEmpty(meterReadeCycle.getTaskId())) {
            jobService.removeJob(meterReadeCycle.getTaskId());
        }
        //创建任务
        createJob(meterReadeCycle.getTaskId(), getCron(param));
        this.baseMapper.updateById(meterReadeCycle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public MeterReadeCycleVO getById(Long id) {
        MeterReadeCycle meterReadeCycle = this.baseMapper.selectById(id);
        return ObjectUtils.copying(meterReadeCycle, MeterReadeCycleVO.class);
    }

    @Override
    public Pagination<MeterReadeCycleVO> getPage(MeterReadeCycleQueryParam param) {
        //请自行添加查询条件
        Page<MeterReadeCycle> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, MeterReadeCycleVO.class);
    }

    /**
     * 获取cron表达式
     *
     * @param param 周期对象
     *
     * @return 表达式
     */
    private String getCron(MeterReadeCycleParam param) {
        String cron = "";
        if (MeterReadingCycleEnum.EVERY_SPECIFIED_MIN.getCode().equals(param.getReadeType())) {
            if (StringUtils.isNotEmpty(param.getReadeVulue())) {
                cron = "0 0/" + param.getReadeVulue() + " * * * ?";
            } else {
                cron = "0 0/15 * * * ?";
            }
        } else if (MeterReadingCycleEnum.EVERY_SPECIFIED_DAY.getCode().equals(param.getReadeType())) {
            if (StringUtils.isNotEmpty(param.getWorkTime())) {
                String[] split = param.getWorkTime().split(":");
                cron = Integer.valueOf(split[2]) + " " + Integer.valueOf(split[1]) + " " + Integer.valueOf(split[0]) + " " + "1/" +
                       param.getReadeVulue() + " * ?";
            } else {
                cron = "0 0 0 " + "1/" + param.getReadeVulue() + " * ?";
            }
        } else if (MeterReadingCycleEnum.LAST_DAY_MONTHLY.getCode().equals(param.getReadeType())) {
            if (StringUtils.isNotEmpty(param.getWorkTime())) {
                String[] split = param.getWorkTime().split(":");
                cron = Integer.valueOf(split[2]) + " " + Integer.valueOf(split[1]) + " " + Integer.valueOf(split[0]) + " L " + "* ?";
            } else {
                cron = "0 0 0 " + " L " + "* ?";
            }
        } else if (MeterReadingCycleEnum.SPECIFIED_DATE_MONTHLY.getCode().equals(param.getReadeType())) {
            if (StringUtils.isNotEmpty(param.getWorkTime())) {
                String[] split = param.getWorkTime().split(":");
                cron = Integer.valueOf(split[2]) + " " + Integer.valueOf(split[1]) + " " + Integer.valueOf(split[0]) + " " +
                       param.getReadeVulue() + " * ?";
            } else {
                cron = "0 0 0 " + param.getReadeVulue() + " * ?";
            }
        } else if (MeterReadingCycleEnum.WEEKLY_DESIGNATED_DATE.getCode().equals(param.getReadeType())) {
            if (StringUtils.isNotEmpty(param.getWorkTime())) {

                StringBuilder weekCron = getWeekCron(param.getReadeVulue());
                String[] split = param.getWorkTime().split(":");
                cron = Integer.valueOf(split[2]) + " " + Integer.valueOf(split[1]) + " " + Integer.valueOf(split[0]) + " ? * " + weekCron;
            } else {
                StringBuilder weekCron = getWeekCron(param.getReadeVulue());
                cron = "0 0 0 ? * " + weekCron;
            }
        }
        return cron;
    }

    /**
     * 周cron处理
     *
     * @param readeValue author lrj
     */
    private StringBuilder getWeekCron(String readeValue) {
        String[] split1 = readeValue.split(",");
        StringBuilder stringBuilder = new StringBuilder();
        //国际周的换算拼接
        for (int i = 0; i < split1.length; i++) {
            int weekCron = Integer.parseInt(split1[i]);
            if (weekCron >= 7) {
                stringBuilder.append(1);
            } else {
                stringBuilder.append(weekCron + 1);
            }
            if (split1.length > 1 && i < (split1.length - 1)) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void initRegistryJob(SysReadEvent event) {
        log.info("energy init readeCycle job");
        SessionContextHolder.clearSessionContext();
        var list = this.lambdaQuery().list();
        list.forEach(meterReadeCycle -> {
            SessionContextHolder.requestTenant(meterReadeCycle.getTenant());
            var param = ObjectUtils.copying(meterReadeCycle, MeterReadeCycleParam.class);
            if (StringUtils.isBlank(meterReadeCycle.getTaskId())) {
                meterReadeCycle.setTaskId(getJobName(ObjectIdUtils.next()));
            }
            createJob(meterReadeCycle.getTaskId(), getCron(param));
            this.updateById(meterReadeCycle);
            SessionContextHolder.clearSessionContext();
        });
    }

    private void createJob(String id, String cron) {
        var tenant = SessionContextHolder.getRequester().getTenant();
        jobService.addJob(id,
                          ApplicationContextHolder.getBean(EnergyMonitorJob.class),
                          CALL_BACK_METHOD,
                          cron,
                          JOB_DESC,
                          tenant.toString(),
                          true);
    }

    private String getJobName(String id) {
        return String.format("%s_%s", JOB_NAME, id);
    }
}