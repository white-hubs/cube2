package com.guhecloud.cube.srv.prepaid.server.job;

import com.guhecloud.cube.srv.prepaid.common.constants.Constants;
import com.guhecloud.cube.srv.prepaid.common.vo.AmmeterDTO;
import com.guhecloud.cube.srv.prepaid.server.service.ThirdGhService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 拉取福建广汇数据
 * 增量拉取
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PullGHDataTask {

    private final ThirdGhService thirdGhService;

    @Value("${gh-third.tenant}")
    private Integer tenant;

    /**
     * 拉取广汇第三方数据
     *
     * @param param
     *
     * @return
     */
    @JobAnnotation(name = "pullGhData", cron = "0 0 0 * * ?", desc = "预付费-广汇-预付费数据同步")
    public void pullGhData(String param) {
        /* 按天同步 param 格式为：yyyy-MM-dd,yyyy-MM-dd 且开始日期小于或等于结束日期，只同步某一天时开始日期等于结束日期*/
        /* 如果想只同步某一个数据，格式为：yyyy-MM-dd,yyyy-MM-dd,1 第三个参数表示单独同步哪个数据 1电表，2电表抄表记录，3电表充值记录，4电表日用能 */
        /* 如果不传参数默认同步昨天的数据，定时任务默认每天凌晨执行*/
        LocalDateTime startDate;
        LocalDateTime endDate;
        String exeType = null;
        if (StringUtils.isNotBlank(param)) {
            try {
                var params = param.split(",");
                startDate = DateUtils.parseDate(params[Constants.ZERO_INT]);
                endDate = DateUtils.parseDate(params[Constants.ONE_INT]);
                if (startDate.isAfter(endDate)) {
                    return;
                }
                if (params.length > Constants.TOW_INT) {
                    exeType = params[Constants.TOW_INT];
                }
            } catch (Exception e) {
                throw new ServiceException("data format: yyyy-MM-dd,yyyy-MM-dd");
            }
        } else {
            startDate = DateUtils.beginOfDay(DateUtils.offsetDay(LocalDateTime.now(), -Constants.ONE_INT));
            endDate = DateUtils.endOfDay(startDate);
        }
        var newTime = LocalDateTime.now();
        List<String> meterIdList = null;
        SessionContextHolder.requestTenant(tenant);
        SecurityContextHolder.setPrincipal(Principal.builder().account("1").id(1L).name("system").type(Principal.Type.CLIENT).build());

        /* 可以通过控制第三个参数来实现单个同步（如果不传入就全部同步 */
        if (exeType == null || "1".equals(exeType)) {
            // 电表信息同步（全量更新表信息
            var dtoList = thirdGhService.wholePullAmmeter(tenant, newTime, "1", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM);
            if (dtoList != null) {
                meterIdList = dtoList.stream().map(AmmeterDTO::getMeterId).filter(Objects::nonNull).collect(Collectors.toList());
            }
        }
        if (exeType == null || "2".equals(exeType)) {
            // 电表抄表记录同步(获取表读数然后入库
            thirdGhService.wholePullCollectRecords(tenant, newTime, "1", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, meterIdList);
        }
        if (exeType == null || "3".equals(exeType)) {
            // 电表充值记录同步(会先删除这段时间的记录，再同步
            thirdGhService.wholePullPowerRecharge(tenant, newTime, "1", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, startDate, endDate, meterIdList);
        }
        if (exeType == null || "4".equals(exeType)) {
            // 电表日用能同步(会先删除这段时间的记录，再同步
            thirdGhService.wholePullPowerUseDay(tenant, newTime, "1", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, startDate, endDate, meterIdList);
        }
    }
}
