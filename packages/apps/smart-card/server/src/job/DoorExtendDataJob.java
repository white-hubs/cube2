package com.guhecloud.cube.biz.smartcard.server.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.smartcard.server.dao.AccessEventMapper;
import com.guhecloud.cube.biz.smartcard.server.entity.AccessEvent;
import com.guhecloud.cube.biz.smartcard.server.entity.Card;
import com.guhecloud.cube.biz.smartcard.server.entity.Org;
import com.guhecloud.cube.biz.smartcard.server.entity.VisitingRecord;
import com.guhecloud.cube.biz.smartcard.server.service.AccessEventService;
import com.guhecloud.cube.biz.smartcard.server.service.DoorCardService;
import com.guhecloud.cube.biz.smartcard.server.service.OrgService;
import com.guhecloud.cube.biz.smartcard.server.service.VisitingRecordService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.guhecloud.subsystem.requester.sdk.client.AccessControlClient;
import com.guhecloud.subsystem.requester.sdk.param.accesscontrol.AccessEventParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component("smartcard_door_extend_data_job")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoorExtendDataJob {
    private final AccessControlClient accessControlClient;
    private final SubsystemRedisUtils subsystemRedisUtils;
    private final DoorCardService cardService;
    private final OrgService orgService;
    private final VisitingRecordService visitingRecordService;
    private final AccessEventMapper accessEventMapper;
    private final AccessEventService accessEventService;

    /**
     * 门禁卡采集
     *
     * @Description 门禁卡采集
     * @Author appet
     */
    @JobAnnotation(name = "DoorExtendJobForCard", cron = "0 0/5 * * * ?", desc = "门禁系统-门禁卡采集")
    public void doorExtendJobForCard(String param) {
        try {
            log.info("门禁卡采集开始：==================================================================================");
            var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.DOOR_SERVER);
            subEndpointRefreshConfigs.forEach(s -> {
                SessionContextHolder.requestTenant(s.getTenant());
                var cardList = accessControlClient.getDoorCard();
                log.info("门禁卡采集数据：{}条", cardList.size());
                if (!CollectionUtils.isEmpty(cardList)) {
                    cardList.forEach(card -> {
                        var cardPO = ObjectUtils.copying(card, Card.class);
                        cardPO.setTenant(s.getTenant());
                        cardPO.setDeleted(false);
                        EntityAuditor.populateCreateAudit(cardPO, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, card.getCreateTime());
                        EntityAuditor.populateUpdateAudit(cardPO, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, card.getUpdateTime());
                        cardService.insertOrUpdate(cardPO);
                    });
                }
            });
            log.info("门禁卡采集结束：==================================================================================");
        } catch (Exception e) {
            log.error("门禁卡采集,失败原因:", e);
        }
    }

    /**
     * 门禁系统-组织采集
     *
     * @Description 门禁系统-组织采集
     * @Author appet
     */
    @JobAnnotation(name = "DoorExtendJobForOrg", cron = "0 0/5 * * * ?", desc = "门禁系统-组织采集")
    public void doorExtendJobForOrg(String param) {
        try {
            log.info("门禁系统组织采集开始：==================================================================================");
            var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.DOOR_SERVER);
            subEndpointRefreshConfigs.forEach(s -> {
                SessionContextHolder.requestTenant(s.getTenant());
                var orgList = accessControlClient.getOrg();
                log.info("门禁卡采集数据：{}条", orgList.size());
                if (!CollectionUtils.isEmpty(orgList)) {
                    orgList.stream().forEach(data -> {
                        var po = ObjectUtils.copying(data, Org.class);
                        po.setTenant(s.getTenant());
                        po.setDeleted(false);
                        EntityAuditor.populateInnerCreateAudit(po);
                        EntityAuditor.populateInnerUpdateAudit(po);
                        orgService.insertOrUpdate(po);
                    });
                }
            });
            log.info("门禁系统组织采集结束：==================================================================================");
        } catch (Exception e) {
            log.error("门禁系统组织采集,失败原因:", e);
        }
    }

    /**
     * 门禁系统-访客记录采集
     *
     * @Description 门禁系统-访客记录采集
     * @Author appet
     */
    @JobAnnotation(name = "DoorExtendJobForVisitingRecord", cron = "0 0/5 * * * ?", desc = "门禁系统-访客记录采集")
    public void doorExtendJobForVisitor(String param) {
        try {
            log.info("门禁系统访客记录采集开始：==================================================================================");
            var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.DOOR_SERVER);
            subEndpointRefreshConfigs.forEach(s -> {
                SessionContextHolder.requestTenant(s.getTenant());
                //当前时间
                var currentTime = LocalDateTime.now();
                //开始时间
                var startTime = DateUtils.offsetMonth(currentTime, -1);
                var endTime = DateUtils.offsetMonth(currentTime, 1);
                var queryParam = AccessEventParam.builder().startTime(startTime).endTime(endTime).build();
                var dataList = accessControlClient.getVisitingRecord(queryParam);
                log.info("门禁系统访客记录采集数据：{}条", dataList.size());
                if (!CollectionUtils.isEmpty(dataList)) {
                    dataList.forEach(data -> {
                        var po = ObjectUtils.copying(data, VisitingRecord.class);
                        po.setTenant(s.getTenant());
                        po.setDeleted(false);
                        EntityAuditor.populateInnerCreateAudit(po);
                        EntityAuditor.populateInnerUpdateAudit(po);
                        visitingRecordService.insertOrUpdate(po);
                    });
                }
            });
            log.info("门禁系统访客记录采集结束：==================================================================================");
        } catch (Exception e) {
            log.error("门禁系统访客记录采集,失败原因:", e);
        }
    }

    /**
     * 门禁系统-梯控数据采集
     *
     * @Description 门禁系统-梯控数据采集
     * @Author appet
     */
    @JobAnnotation(name = "DoorExtendJobForTK", cron = "0 0/5 * * * ?", desc = "门禁系统-梯控数据采集")
    public void doorExtendJobForTK(String param) {
        try {
            log.info("门禁系统梯控记录采集开始：==================================================================================");
            var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.DOOR_SERVER);
            subEndpointRefreshConfigs.forEach(s -> {
                SessionContextHolder.requestTenant(s.getTenant());
                //当前时间
                var currentTime = LocalDateTime.now();
                //开始时间
                var accessEvent = accessEventMapper.selectOne(Wrappers.<AccessEvent>lambdaQuery()
                                                                      .eq(AccessEvent::getAccessType, "TK")
                                                                      .orderByDesc(AccessEvent::getEventTime)
                                                                      .last("limit 0,1"));
                var startTime = accessEvent != null ? accessEvent.getEventTime() : DateUtils.offsetDay(currentTime, -1);
                var queryParam = AccessEventParam.builder().startTime(startTime).endTime(currentTime).build();
                var dataList = accessControlClient.getTKRecord(queryParam);
                log.info("门禁系统访问事件采集数据：{}条", dataList.size());
                if (!CollectionUtils.isEmpty(dataList)) {
                    dataList.forEach(data -> {
                        var po = ObjectUtils.copying(data, AccessEvent.class);
                        po.setTenant(s.getTenant());
                        po.setDeleted(false);
                        EntityAuditor.populateInnerCreateAudit(po);
                        EntityAuditor.populateInnerUpdateAudit(po);
                        accessEventService.insertOrUpdate(po);
                    });
                }
            });
            log.info("门禁系统梯控记录采集结束：==================================================================================");
        } catch (Exception e) {
            log.error("门禁系统梯控记录采集,失败原因:", e);
        }
    }

    /**
     * 门禁系统-可视对讲数据采集
     *
     * @Description 门禁系统-可视对讲数据采集
     * @Author appet
     */
    @JobAnnotation(name = "DoorExtendJobForKSDJ", cron = "0 0/5 * * * ?", desc = "门禁系统-可视对讲数据采集")
    public void doorExtendJobForKSDJ(String param) {
        try {
            log.info("门禁系统可视对讲事件采集开始：==================================================================================");
            var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.DOOR_SERVER);
            subEndpointRefreshConfigs.forEach(s -> {
                SessionContextHolder.requestTenant(s.getTenant());
                //当前时间
                var currentTime = LocalDateTime.now();
                //开始时间
                var accessEvent = accessEventMapper.selectOne(Wrappers.<AccessEvent>lambdaQuery()
                                                                      .eq(AccessEvent::getAccessType, "KSDJ")
                                                                      .orderByDesc(AccessEvent::getEventTime)
                                                                      .last("limit 0,1"));
                var startTime = accessEvent != null ? accessEvent.getEventTime() : DateUtils.offsetDay(currentTime, -1);
                var queryParam = AccessEventParam.builder().startTime(startTime).endTime(currentTime).build();
                var dataList = accessControlClient.getKSDJRecord(queryParam);
                log.info("门禁系统可视对讲事件采集数据：{}条", dataList.size());
                if (!CollectionUtils.isEmpty(dataList)) {
                    dataList.forEach(data -> {
                        var po = ObjectUtils.copying(data, AccessEvent.class);
                        po.setTenant(s.getTenant());
                        po.setDeleted(false);
                        EntityAuditor.populateInnerCreateAudit(po);
                        EntityAuditor.populateInnerUpdateAudit(po);
                        accessEventService.insertOrUpdate(po);
                    });
                }
            });
            log.info("门禁系统可视对讲事件采集结束：==================================================================================");
        } catch (Exception e) {
            log.error("门禁系统可视对讲事件采集,失败原因:", e);
        }
    }
}
