package com.guhecloud.cube.onecard.server.consumption.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.onecard.server.consumption.dao.ConsumeRecordOutMapper;
import com.guhecloud.cube.onecard.server.consumption.entity.ConsumeRecordOut;
import com.guhecloud.cube.onecard.server.consumption.enumeration.RiXinDaInterfaceEnum;
import com.guhecloud.cube.onecard.server.consumption.param.ConsumeRecordOutParam;
import com.guhecloud.cube.onecard.server.consumption.param.OneCardJobParam;
import com.guhecloud.cube.onecard.server.consumption.service.ConsumeRecordOutService;
import com.guhecloud.cube.onecard.server.consumption.utils.RedisTimeStampUtils;
import com.guhecloud.cube.onecard.server.consumption.vo.ConsumeRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.subsystem.requester.sdk.client.OneCardClient;
import com.guhecloud.subsystem.requester.sdk.param.onecard.AmountChangeParamQuery;
import com.guhecloud.subsystem.requester.sdk.vo.onecard.ConsumeRecordOutVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 14:35
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumeRecordOutServiceImpl extends ServiceImpl<ConsumeRecordOutMapper, ConsumeRecordOut> implements ConsumeRecordOutService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final OneCardClient oneCardClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncConsumptionRecord(OneCardJobParam oneCardJobParam) {
        //定义两个集合，用于存储消费记录和消费记录扩展
        List<ConsumeRecordOut> consumeRecordOuts = new ArrayList<>();
        //遍历枚举类RiXinDaInterfaceEnum
        for (RiXinDaInterfaceEnum value : RiXinDaInterfaceEnum.values()) {
            //定义一个变量，用于存储上次同步时间
            String lastSyncTime = null;
            //从redis中获取上次同步时间
            Object o = this.redisTemplate.opsForHash().get(RedisTimeStampUtils.getRedisKey(), value.name());
            if (Objects.nonNull(o)) {
                //如果获取到上次同步时间，则格式化
                var dateTime = DateUtils.offset((LocalDateTime) o, ChronoUnit.SECONDS, 1);
                lastSyncTime = DateUtils.format(dateTime);
            } else {
                ConsumeRecordOut consumeRecordOut = this.lambdaQuery()
                        .eq(ConsumeRecordOut::getRecordType, value.getDesc())
                        .orderByDesc(ConsumeRecordOut::getTradeTime)
                        .last("limit 1").one();
                lastSyncTime = Objects.nonNull(consumeRecordOut)
                               ? DateUtils.format(DateUtils.offset(consumeRecordOut.getTradeTime(), ChronoUnit.SECONDS, 1))
                               : DateUtils.format(LocalDateTime.now());
            }
            AmountChangeParamQuery amountChangeParamQuery = new AmountChangeParamQuery();
            amountChangeParamQuery.setPageNumber(1);
            amountChangeParamQuery.setPageSize(1000);
            amountChangeParamQuery.setType(value.name());
            amountChangeParamQuery.setBeginDate(Objects.nonNull(oneCardJobParam.getStartTime())
                                                ? oneCardJobParam.getStartTime() : lastSyncTime);
            amountChangeParamQuery.setEndDate(Objects.nonNull(oneCardJobParam.getEndTime())
                                              ? oneCardJobParam.getEndTime() : DateUtils.format(LocalDateTime.now()));
            log.info("开始同步消费记录，时间范围：{}-{}", amountChangeParamQuery.getBeginDate(), amountChangeParamQuery.getEndDate());
            //定义一个参数对象，用于查询消费记录
            Page<ConsumeRecordOutVO> consumeRecord =
                    //设置查询参数，开始时间为上次同步时间，结束时间为当前时间
                    oneCardClient.getConsumeRecord(amountChangeParamQuery);
            if (Objects.isNull(consumeRecord) || CollectionUtils.isEmpty(consumeRecord.getRecords())) {
                continue;
            }
            var dateList = new ArrayList<LocalDateTime>();
            consumeRecord.getRecords().forEach(record -> {
                ConsumeRecordOut consumeRecordOut = ObjectUtils.copying(record, ConsumeRecordOut.class);
                consumeRecordOuts.add(consumeRecordOut);
                dateList.add(consumeRecordOut.getTradeTime());
            });
            //将消费记录添加到集合中
            Optional<LocalDateTime> max = dateList.stream()
                    .filter(Objects::nonNull)
                    //将消费记录的时间添加到集合中
                    .max(Comparator.comparing(t -> t));
            max.ifPresent(date -> this.redisTemplate.opsForHash().put(RedisTimeStampUtils.getRedisKey(), value.name(), date));
        }
        log.info("消费记录同步完成，共同步{}条记录", consumeRecordOuts.size());
        this.saveOrUpdateBatch(consumeRecordOuts);
    }

    @Override
    public Pagination<ConsumeRecordVO> getConsumeRecordOut(ConsumeRecordOutParam param) {
        Period period = param.getPeriod();
        String recordType = Objects.nonNull(param.getInterfaceEnum()) ? param.getInterfaceEnum().getDesc() : null;
        Page<ConsumeRecordOut> consumeRecordOutPage = this.lambdaQuery()
                .eq(Objects.nonNull(param.getAccountId()), ConsumeRecordOut::getAccountId, param.getAccountId())
                .eq(StringUtils.isNoneBlank(recordType), ConsumeRecordOut::getRecordType, recordType)
                .between(Objects.nonNull(period), ConsumeRecordOut::getTradeTime, period.getBegin(), period.getEnd())
                .orderByDesc(ConsumeRecordOut::getTradeTime)
                .page(PagingUtils.convert(param));
        if (Objects.isNull(consumeRecordOutPage) || CollectionUtils.isEmpty(consumeRecordOutPage.getRecords())) {
            return new Pagination<>();
        }
        return PagingUtils.convert(consumeRecordOutPage, ConsumeRecordVO.class);
    }

}
