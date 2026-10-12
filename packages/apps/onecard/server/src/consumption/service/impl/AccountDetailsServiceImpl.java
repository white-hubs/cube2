package com.guhecloud.cube.onecard.server.consumption.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.onecard.server.consumption.dao.AccountDetailsMapper;
import com.guhecloud.cube.onecard.server.consumption.entity.AccountDetails;
import com.guhecloud.cube.onecard.server.consumption.param.OneCardJobParam;
import com.guhecloud.cube.onecard.server.consumption.service.AccountDetailsService;
import com.guhecloud.cube.onecard.server.consumption.utils.RedisTimeStampUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.subsystem.requester.sdk.client.OneCardClient;
import com.guhecloud.subsystem.requester.sdk.param.onecard.AmountChangeParamQuery;
import com.guhecloud.subsystem.requester.sdk.vo.onecard.AccountDetailsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountDetailsServiceImpl extends ServiceImpl<AccountDetailsMapper, AccountDetails> implements AccountDetailsService {
    private static final String ACCOUNT_DETAILS = "ACCOUNT_DETAILS";

    private final RedisTemplate<String, Object> redisTemplate;

    private final OneCardClient oneCardClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncAccountDetails(OneCardJobParam oneCardJobParam) {

        String lastSyncTime = null;
        //从redis中获取上次同步时间
        Object o = this.redisTemplate.opsForHash().get(RedisTimeStampUtils.getRedisKey(), ACCOUNT_DETAILS);
        if (Objects.nonNull(o)) {
            //如果获取到上次同步时间，则格式化
            lastSyncTime = DateUtils.format((LocalDateTime) o);
        } else {
            AccountDetails accountDetails = this.lambdaQuery().orderByDesc(AccountDetails::getUpdateTime).last("limit 1").one();
            lastSyncTime = Objects.nonNull(accountDetails) ? DateUtils.format(accountDetails.getUpdateTime())
                                                           : DateUtils.format(LocalDateTime.now());

        }
        AmountChangeParamQuery amountChangeParamQuery = new AmountChangeParamQuery();
        amountChangeParamQuery.setPageSize(1000);
        amountChangeParamQuery.setPageNumber(1);
        amountChangeParamQuery.setBeginDate(Objects.nonNull(oneCardJobParam.getStartTime())
                                            ? oneCardJobParam.getStartTime() : lastSyncTime);
        amountChangeParamQuery.setEndDate(Objects.nonNull(oneCardJobParam.getEndTime())
                                          ? oneCardJobParam.getEndTime() : DateUtils.format(LocalDateTime.now()));
        log.info("开始同步消费账户记录，时间范围：{}-{}", amountChangeParamQuery.getBeginDate(), amountChangeParamQuery.getEndDate());
        Page<AccountDetailsVO> accountVoPage =
                oneCardClient.getAccountStatusInfoBatch(amountChangeParamQuery);
        if (Objects.isNull(accountVoPage) || CollectionUtils.isEmpty(accountVoPage.getRecords())) {
            return;
        }
        var dateList = new ArrayList<LocalDateTime>();
        var accountDetailsList = new ArrayList<AccountDetails>();
        accountVoPage.getRecords().forEach(record -> {
            AccountDetails accountDetails = ObjectUtils.copying(record, AccountDetails.class);
            accountDetailsList.add(accountDetails);
            dateList.add(accountDetails.getUpdateTime());
        });

        List<Long> longList = accountDetailsList.stream().map(AccountDetails::getId).collect(Collectors.toList());
        Map<Long, AccountDetails> accountDetailsMap = this.lambdaQuery().in(AccountDetails::getId, longList).list()
                .stream().collect(Collectors.toMap(AccountDetails::getId, t -> t));
        List<AccountDetails> updateAccountDetailsList = new ArrayList<>();
        List<AccountDetails> insertAccountDetailsList = new ArrayList<>();
        accountDetailsList.forEach(accountDetails -> {
            if (accountDetailsMap.containsKey(accountDetails.getId())) {
                AccountDetails existAccount = accountDetailsMap.get(accountDetails.getId());
                existAccount.setId(accountDetails.getId());
                existAccount.setUserCard(accountDetails.getUserCard());
                existAccount.setSubsidyBalance(accountDetails.getSubsidyBalance());
                existAccount.setCashBalance(accountDetails.getCashBalance());
                existAccount.setHaircutCount(accountDetails.getHaircutCount());
                existAccount.setLaundryCount(accountDetails.getLaundryCount());
                updateAccountDetailsList.add(existAccount);
            } else {
                insertAccountDetailsList.add(accountDetails);
            }
        });
        log.info("同步消费账户记录结束，更新{}条记录，新增{}条记录", updateAccountDetailsList.size(), insertAccountDetailsList.size());
        if (CollectionUtils.isNotEmpty(updateAccountDetailsList)) {
            this.updateBatchById(updateAccountDetailsList);
        }
        if (CollectionUtils.isNotEmpty(insertAccountDetailsList)) {
            this.saveBatch(insertAccountDetailsList);
        }
        //将消费记录添加到集合中
        Optional<LocalDateTime> max = dateList.stream()
                .filter(Objects::nonNull)
                //将消费记录的时间添加到集合中
                .max(Comparator.comparing(t -> t));
        max.ifPresent(date -> this.redisTemplate.opsForHash().put(RedisTimeStampUtils.getRedisKey(), ACCOUNT_DETAILS, date));
    }
}
