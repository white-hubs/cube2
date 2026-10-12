package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dao.LineLossJobConfigMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossJobConfig;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.enumeration.LineLossJobSceneEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossJobConfigParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossJobConfigService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossJobConfigVO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.cron.CronExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 线损采集配置服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossJobConfigServiceImpl extends ServiceImpl<LineLossJobConfigMapper, LineLossJobConfig> implements LineLossJobConfigService {

    /**
     * 修改线损采集配置
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    public Boolean saveOrUpdateConfig(LineLossJobConfigParam param) {
        this.checkStatConfig();
        this.saveOrUpdate(param.getLineLossCollectConfig());
        return true;
    }

    /**
     * 线损采集配置详情
     *
     * @return 配置信息
     */
    @Override
    public LineLossJobConfigVO detail() {
        var po = this.getOne(Wrappers.<LineLossJobConfig>lambdaQuery()
                                     .eq(LineLossJobConfig::getSceneType, LineLossJobSceneEnum.READ_COLLECT).last("limit 1"));
        return LineLossJobConfigVO.poToVo(po);
    }

    // 检测统计配置
    private void checkStatConfig() {
        boolean exists = this.exists(Wrappers.<LineLossJobConfig>lambdaQuery()
                                             .eq(LineLossJobConfig::getSceneType, LineLossJobSceneEnum.LINE_LOSS_STAT));
        if (!exists) {
            var po = new LineLossJobConfig();
            po.setId(IdWorker.getId());
            po.setSceneType(LineLossJobSceneEnum.LINE_LOSS_STAT);
            po.setPeriodType("EVERY_DAY");
            po.setHourTime("00:00:00");
            po.setEnable(true);
            po.setCron("0 0 0 * * ?");
            try {
                po.setNextExecuteTime(new CronExpression(po.getCron()).getNextValidTimeAfter(DateUtils.now()));
            } catch (Exception e) {
                log.error("cron表达式错误{}：", po.getCron(), e);
            }
            po.setAttributeCode("dds");
            this.save(po);
        }

    }
}
