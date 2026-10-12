package com.guhecloud.cube.onecard.server.consumption.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.guhecloud.cube.onecard.server.consumption.entity.Account;
import com.guhecloud.cube.onecard.server.consumption.entity.AmountChangeRecord;
import com.guhecloud.cube.onecard.server.consumption.entity.CopiesChangeRecord;
import com.guhecloud.cube.onecard.server.consumption.enumeration.TransactionTypeEnum;
import com.guhecloud.cube.onecard.server.consumption.param.OneCardJobParam;
import com.guhecloud.cube.onecard.server.consumption.service.AccountDetailsService;
import com.guhecloud.cube.onecard.server.consumption.service.AccountService;
import com.guhecloud.cube.onecard.server.consumption.service.AmountChangeRecordService;
import com.guhecloud.cube.onecard.server.consumption.service.CanteenOrderService;
import com.guhecloud.cube.onecard.server.consumption.service.ConsumeRecordOutService;
import com.guhecloud.cube.onecard.server.consumption.service.CopiesChangeRecordService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.guhecloud.subsystem.requester.sdk.client.OneCardClient;
import com.guhecloud.subsystem.requester.sdk.param.onecard.AmountChangeParamQuery;
import com.guhecloud.subsystem.requester.sdk.vo.onecard.CopiesChangeRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AccountDataAcquisition {

    @Autowired
    private OneCardClient oneCardClient;

    @Autowired
    private AmountChangeRecordService amountChangeRecordService;

    @Autowired
    private CopiesChangeRecordService copiesChangeRecordService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ConsumeRecordOutService consumeRecordOutService;

    @Autowired
    private AccountDetailsService accountDetailsService;

    @Autowired
    private CanteenOrderService canteenOrderService;

    @Autowired
    private SubsystemRedisUtils subsystemRedisUtils;

    /**
     * 账户数据采集
     *
     * @return
     */
    @JobAnnotation(name = "collectAccount", cron = "0 0/1 * * * ?", desc = "一卡通-消费管理-账户数据采集")
    public void addAccount(String param) {
        SessionContextHolder.clearSessionContext();
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.ONECARD_SERVER);
        subEndpointRefreshConfigs.forEach(config -> {
            SessionContextHolder.requestTenant(config.getTenant());
            var amountChangeParamQuery = new AmountChangeParamQuery();
            var accountVo = oneCardClient.getAccountStatusInfo(amountChangeParamQuery);
            var userCardList = accountService.list().stream().map(Account::getUserCard).collect(Collectors.toSet());
            log.info("账户采集开始=========第三方入库的数据：{}", JsonUtils.serialize(accountVo));

            if (accountVo != null) {
                var account = new Account();
                if (StringUtils.isNotBlank(accountVo.getAccountStatus())) {
                    account.setAccountStatus(accountVo.getAccountStatus());
                }
                if (StringUtils.isNotBlank(accountVo.getUserName())) {
                    account.setUserName(accountVo.getUserName());
                }
                if (StringUtils.isNotBlank(accountVo.getUserNo())) {
                    account.setUserNo(accountVo.getUserNo());
                }
                if (StringUtils.isNotBlank(accountVo.getUserSerial())) {
                    account.setUserSerial(Long.valueOf(accountVo.getUserSerial()));
                }
                if (StringUtils.isNotBlank(accountVo.getUserCard())) {
                    account.setUserCard(accountVo.getUserCard());
                    if (!userCardList.contains(accountVo.getUserCard())) {
                        log.info("将插入的账户数据是：{}", JsonUtils.serialize(account));
                        accountService.save(account);
                    } else {
                        log.info("被修改的数据是：{}", JsonUtils.serialize(account));
                        accountService.update(account, new UpdateWrapper<Account>().eq("user_card", accountVo.getUserCard()));
                    }
                }
            }
            SessionContextHolder.clearSessionContext();
        });
    }

    /**
     * 金额变更数据采集
     *
     * @return
     */
    @JobAnnotation(name = "collectAmountRecord", cron = "0 * * * * ?", desc = "一卡通-消费管理-金额变更数据采集")
    public void collectAmountRecord(String param) {
        SessionContextHolder.clearSessionContext();
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.ONECARD_SERVER);
        subEndpointRefreshConfigs.forEach(config -> {
            SessionContextHolder.requestTenant(config.getTenant());
            var amountChangeParamQuery = new AmountChangeParamQuery();
            var now = LocalDateTime.now();
            amountChangeParamQuery.setEndDate(DateUtils.format(now));
            amountChangeParamQuery.setBeginDate(DateUtils.format(now.minusDays(1)));
            var amountChange = oneCardClient.getAmountChange(amountChangeParamQuery);
            var collect = amountChangeRecordService.list().stream().map(AmountChangeRecord::getOrderId).collect(Collectors.toSet());
            if (amountChange != null && !amountChange.getRecords().isEmpty()) {
                for (var record : amountChange.getRecords()) {
                    if (!collect.contains(record.getOrderId())) {
                        var amountChangeRecord = new AmountChangeRecord();
                        if (StringUtils.isNotBlank(record.getTransactionMoney())) {
                            amountChangeRecord.setTransactionMoney(new BigDecimal(record.getTransactionMoney()));
                        }
                        if (Objects.nonNull(record.getTransactionChannel())) {
                            amountChangeRecord.setTransactionChannel(record.getTransactionChannel());
                        }
                        if (StringUtils.isNotBlank(record.getBalance())) {
                            amountChangeRecord.setBalance(new BigDecimal(record.getBalance()));
                        }
                        if (StringUtils.isNotBlank(record.getDepName())) {
                            amountChangeRecord.setDepName(record.getDepName());
                        }
                        if (Objects.nonNull(record.getTransactionStatus())) {
                            amountChangeRecord.setTransactionStatus(record.getTransactionStatus());
                        }
                        if (StringUtils.isNotBlank(record.getOrderId())) {
                            amountChangeRecord.setOrderId(record.getOrderId());
                        }
                        if (Objects.nonNull(record.getTransactionType())) {
                            amountChangeRecord.setTransactionType(TransactionTypeEnum.AmountJobtransfer(record.getTransactionType()));
                        }
                        if (StringUtils.isNotBlank(record.getUserNo()) && StringUtils.isNotBlank(record.getUserSerial())) {
                            var list = accountService.list(new LambdaQueryWrapper<Account>()
                                                                   .eq(Account::getUserNo, record.getUserNo())
                                                                   .eq(Account::getUserSerial, record.getUserSerial()));
                            if (!list.isEmpty()) {
                                amountChangeRecord.setUserCard(list.getFirst().getUserCard());
                            }
                        }
                        amountChangeRecord.setTransactionTime(DateUtils.parse(record.getTransactionTime()));
                        var list = amountChangeRecordService.list(new LambdaQueryWrapper<AmountChangeRecord>()
                                                                          .eq(AmountChangeRecord::getTransactionType, amountChangeRecord.getTransactionType())
                                                                          .eq(AmountChangeRecord::getTransactionTime, amountChangeRecord.getTransactionTime())
                                                                          .eq(AmountChangeRecord::getOrderId, amountChangeRecord.getOrderId()));
                        if (list.isEmpty()) {
                            amountChangeRecordService.save(amountChangeRecord);
                        }
                    }
                }

            }
        });
    }

    /**
     * 份变更数据采集
     *
     * @return
     */
    @JobAnnotation(name = "collectCopiesRecord", cron = "0 * * * * ?", desc = "一卡通-消费管理-份变更数据采集")
    public void collectCopiesRecord(String param) {
        SessionContextHolder.clearSessionContext();
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.ONECARD_SERVER);
        subEndpointRefreshConfigs.forEach(config -> {
            SessionContextHolder.requestTenant(config.getTenant());
            var amountChangeParamQuery = new AmountChangeParamQuery();
            var now = LocalDateTime.now();
            amountChangeParamQuery.setEndDate(DateUtils.format(now));
            amountChangeParamQuery.setBeginDate(DateUtils.format(now.minusDays(1)));
            var copiesChange = oneCardClient.getCopiesChange(amountChangeParamQuery);
            if (copiesChange != null && !copiesChange.getRecords().isEmpty()) {
                for (CopiesChangeRecordVo record : copiesChange.getRecords()) {
                    var copiesChangeRecord = new CopiesChangeRecord();
                    if (StringUtils.isNotBlank(record.getDepName())) {
                        copiesChangeRecord.setDepName(record.getDepName());
                    }
                    if (StringUtils.isNotBlank(record.getNewMoney())) {
                        copiesChangeRecord.setTransactionCount(Integer.valueOf(record.getNewMoney()));
                    }
                    if (Objects.nonNull(record.getTransactionType())) {
                        copiesChangeRecord.setTransactionType(TransactionTypeEnum.CopiesJobtransfer(record.getTransactionType()));
                    }
                    if (StringUtils.isNotBlank(record.getUserNo()) || StringUtils.isNotBlank(record.getUserSerial())) {
                        var list = accountService.list(new LambdaQueryWrapper<Account>()
                                                               .eq(Account::getUserNo, record.getUserNo())
                                                               .eq(Account::getUserSerial, record.getUserSerial()));
                        if (!list.isEmpty()) {
                            copiesChangeRecord.setUserCard(list.getFirst().getUserCard());
                        }
                    }
                    copiesChangeRecord.setTransactionTime(DateUtils.parse(record.getTransactionTime()));
                    var copiesChangeRecordList = copiesChangeRecordService.list(new LambdaQueryWrapper<CopiesChangeRecord>()
                                                                                        .eq(CopiesChangeRecord::getTransactionType, copiesChangeRecord.getTransactionType())
                                                                                        .eq(CopiesChangeRecord::getTransactionTime, copiesChangeRecord.getTransactionTime()));
                    if (copiesChangeRecordList.isEmpty()) {
                        copiesChangeRecordService.save(copiesChangeRecord);
                    }
                }
            }
        });
    }

    /**
     * 消费记录数据采集
     */
    @JobAnnotation(name = "collectConsumptionRecord", cron = "0 * * * * ?", desc = "一卡通-消费管理-消费记录数据采集")
    public void collectConsumptionRecord(String param) {
        log.info("消费记录数据采集");
        SessionContextHolder.clearSessionContext();
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.ONECARD_SERVER);
        subEndpointRefreshConfigs.forEach(config -> {
            SessionContextHolder.requestTenant(config.getTenant());
            var oneCardJobParam = new OneCardJobParam();
            oneCardJobParam.setTenant(config.getTenant());
            consumeRecordOutService.syncConsumptionRecord(oneCardJobParam);
            SessionContextHolder.clearSessionContext();
        });
    }

    /**
     * 账户明细数据采集
     */
    @JobAnnotation(name = "collectAccountDetails", cron = "0 * * * * ?", desc = "一卡通-消费管理-账户明细数据采集")
    public void collectAccountDetails(String param) {
        log.info("账户明细数据采集");
        SessionContextHolder.clearSessionContext();
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.ONECARD_SERVER);
        subEndpointRefreshConfigs.forEach(config -> {
            SessionContextHolder.requestTenant(config.getTenant());
            var oneCardJobParam = new OneCardJobParam();
            oneCardJobParam.setTenant(config.getTenant());
            accountDetailsService.syncAccountDetails(oneCardJobParam);
        });
    }

    /**
     * 食堂订单数据采集
     */
    @JobAnnotation(name = "collectCanteenOrder", cron = "0 0/10 * * * ?", desc = "一卡通-消费管理-食堂订单数据采集")
    public void collectCanteenOrder(String param) {
        log.info("食堂订单数据采集");
        SessionContextHolder.clearSessionContext();
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.ONECARD_SERVER);
        subEndpointRefreshConfigs.forEach(config -> {
            SessionContextHolder.requestTenant(config.getTenant());
            var oneCardJobParam = new OneCardJobParam();
            oneCardJobParam.setTenant(config.getTenant());
            canteenOrderService.syncCanteenOrder(oneCardJobParam);
            SessionContextHolder.clearSessionContext();
        });
    }
}
