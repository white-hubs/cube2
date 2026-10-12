package com.guhecloud.cube.sn.meattrade.plugin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.sn.meattrade.plugin.common.entity.PorkArrange;
import com.guhecloud.cube.sn.meattrade.plugin.common.enumeration.PorkArrangePushDataStatusEnum;
import com.guhecloud.cube.sn.meattrade.plugin.common.param.PorkArrangeQueryParam;
import com.guhecloud.cube.sn.meattrade.plugin.common.po.PorkArrangePushDataPO;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PorkArrangePushDataService extends IService<PorkArrangePushDataPO> {

    Long save(PorkArrange porkArrange);

    List<PorkArrange> getPushFailPorkArranges(LocalDateTime maxRetryTime, Integer max, LocalDateTime retryTime);

    List<PorkArrange> getPushPorkArranges(Integer max);

    void updatePushStatusByIds(Collection<Long> ids, PorkArrangePushDataStatusEnum status);

    void updatePushStatusByTallIds(Collection<String> ids,
                                   PorkArrangePushDataStatusEnum status,
                                   Integer pushTimes
                                  );

    Long getPreEventNotPushSuccessCount(String guid, Long arrangeReportId, Integer eventType);

    void updatePushStatusAndFailTimes(Long arrangeReportId, String guid, PorkArrangePushDataStatusEnum status, Integer pushTimes,
                                      LocalDateTime nextRetryTime
                                     );

    void resetInProcessingStatus();

    void deletedExpirePush(LocalDateTime expireTime);

    Set<String> getTallIds(PorkArrangeQueryParam param);

    Set<String> getFailPushTallIds(Collection<String> tallIds);

    Set<String> getStayPushTallIds(Collection<String> tallIds);
}
