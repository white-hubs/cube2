package com.guhecloud.cube.sn.meattrade.plugin.dbc.task;

import com.guhecloud.cube.sn.meattrade.plugin.common.entity.PorkArrange;
import com.guhecloud.cube.sn.meattrade.plugin.common.enumeration.PorkArrangePushDataStatusEnum;
import com.guhecloud.cube.sn.meattrade.plugin.common.service.PorkArrangePushDataService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.DbcMeatTradeApiService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.BaseResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.SetTallyDataResponse;
import com.guhecloud.cube.sn.meattrade.plugin.utils.PorkArrangeConvertUtils;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Objects;

@Getter
@Slf4j
public class TallyPushTask implements Runnable {

    private final PorkArrange tallyPushPO;

    public TallyPushTask(PorkArrange tallyPushPO) {
        this.tallyPushPO = tallyPushPO;
    }

    @Override
    public void run() {
        SessionContextHolder.requestTenant(tallyPushPO.getTenant());
        DbcMeatTradeApiService dbcMeatTradeApiService = ApplicationContextHolder.getBean(DbcMeatTradeApiService.class);
        PorkArrangePushDataService porkArrangePushDataService = ApplicationContextHolder.getBean(PorkArrangePushDataService.class);
        PorkArrangePushDataStatusEnum status = PorkArrangePushDataStatusEnum.PUSH_FAIL;
        RedissonRedLock redissonRedLock = null;
        try {
            redissonRedLock = RedisUtils.getDistributedLock()
                    .tryLock("dbcMeatTradeLock:" + tallyPushPO.getTallyId(), Expiration.seconds(10));
            if (Objects.nonNull(redissonRedLock)) {
                //通过event_type判断
                Long preEventTypePushCount = porkArrangePushDataService.getPreEventNotPushSuccessCount(tallyPushPO.getGuid(), tallyPushPO.getId(), tallyPushPO.getEventType());
                if (preEventTypePushCount > 0) {
                    log.warn("理货号为:{},guid:{},前置事件状态未推送成功,本次不进行推送,本次状态为:{}", tallyPushPO.getTallyId(),
                             tallyPushPO.getGuid(), tallyPushPO.getEventType());
                    status = PorkArrangePushDataStatusEnum.UN_PUSH;
                    return;
                }
                BaseResponse<SetTallyDataResponse> baseResponse = dbcMeatTradeApiService.setTallyData(PorkArrangeConvertUtils.convertPorkArrangeToDbcRequest(tallyPushPO));
                if (baseResponse.getSuccess() && baseResponse.getCode() == 1) {
                    status = PorkArrangePushDataStatusEnum.PUSH_SUCCESS;
                } else {
                    //失败计数
                    this.incrementRetryCount();
                }
            } else {
                log.warn("dbcMeatTradeLock on {} failed", tallyPushPO.getTallyId());
            }
        } catch (Exception e) {
            log.error("推送理货数据到大白菜失败:{}", e.getMessage(), e);
            this.incrementRetryCount();
        } finally {
            SessionContextHolder.clearSessionContext();
            if (Objects.nonNull(redissonRedLock)) {
                redissonRedLock.unlock();
            }
            porkArrangePushDataService.updatePushStatusAndFailTimes(tallyPushPO.getId(),
                                                                    tallyPushPO.getGuid(),
                                                                    status,
                                                                    tallyPushPO.getPushTimes() + 1,
                                                                    tallyPushPO.getNextRetryTime());
        }
    }

    public void incrementRetryCount() {
        var nextRetryTime = DateUtils.beginOfMinute(DateUtils.parse(System.currentTimeMillis() + calculateDelay()));
        this.tallyPushPO.setNextRetryTime(nextRetryTime);
    }

    private long calculateDelay() {
        return 60 * 1000;
    }
}
