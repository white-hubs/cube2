package com.guhecloud.cube.base.dictionary.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.base.dictionary.server.dao.DictionaryItemMapper;
import com.guhecloud.cube.base.dictionary.server.entity.DictionaryItem;
import com.guhecloud.cube.base.dictionary.server.enumeration.ChangeNoticeEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.DataBaseOperateTypeEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.DictionaryTypeEnum;
import com.guhecloud.cube.base.dictionary.server.event.DictionaryDeleteMessage;
import com.guhecloud.cube.base.dictionary.server.event.DictionaryMessage;
import com.guhecloud.cube.base.dictionary.server.service.ChangeNoticeService;
import com.guhecloud.cube.base.dictionary.server.service.DictionaryRedisService;
import com.guhecloud.cube.base.dictionary.server.util.DictionaryRedisKeyUtil;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-25  09:29
 * @Description:
 * @Version: 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeNoticeServiceImpl implements ChangeNoticeService {
    private final DictionaryRedisService dictionaryRedisService;
    private final DictionaryItemMapper dictionaryItemMapper;

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public void sendChangeNotice(DictionaryItem dictionaryItem, String code, String value, DictionaryTypeEnum type) {
        if (ObjectUtils.isNotEmpty(dictionaryItem) && ObjectUtils.isNotEmpty(dictionaryItem.getChangeNotice())) {
            String[] changeNotice = dictionaryItem.getChangeNotice().split(",");
            for (String ex : changeNotice) {
                if (ChangeNoticeEnum.ROCKET_MQ.name().equals(ex)) {
                    // 发送MQ消息
                    DictionaryMessage dictionaryMessage = JsonUtils.deserialize(value, DictionaryMessage.class);
                    dictionaryMessage.setType(type.name());
                    RocketMQContextHolder.send(DictionaryMessage.TOPIC + ":" + dictionaryItem.getCode(), dictionaryMessage);
                } else if (ChangeNoticeEnum.REDIS.name().equals(ex)) {
                    // 修改redis缓存
                    String redisKey = type.equals(DictionaryTypeEnum.USER_DICTIONARY)
                                      ? DictionaryRedisKeyUtil.getRedisKeyLimitUser(dictionaryItem.getCode(), code)
                                      : DictionaryRedisKeyUtil.getRedisKey(dictionaryItem.getCode(), code);
                    dictionaryRedisService.set(redisKey, value);
                }
            }
        }
    }

    @Override
    public void sendChangeNotice(DictionaryItem dictionaryItem, String code, String value, DictionaryTypeEnum type,
                                 DataBaseOperateTypeEnum operateType
                                ) {
        if (ObjectUtils.isNotEmpty(dictionaryItem) && ObjectUtils.isNotEmpty(dictionaryItem.getChangeNotice())) {
            String[] changeNotice = dictionaryItem.getChangeNotice().split(",");
            for (String ex : changeNotice) {
                if (ChangeNoticeEnum.ROCKET_MQ.name().equals(ex)) {
                    // 发送MQ消息
                    DictionaryMessage dictionaryMessage = JsonUtils.deserialize(value, DictionaryMessage.class);
                    dictionaryMessage.setType(type.name());
                    dictionaryMessage.setOperationType(operateType.name());
                    RocketMQContextHolder.send(DictionaryMessage.TOPIC + ":" + dictionaryItem.getCode(), dictionaryMessage);
                } else if (ChangeNoticeEnum.REDIS.name().equals(ex)) {
                    // 修改redis缓存
                    String redisKey = type.equals(DictionaryTypeEnum.USER_DICTIONARY)
                                      ? DictionaryRedisKeyUtil.getRedisKeyLimitUser(dictionaryItem.getCode(), code)
                                      : DictionaryRedisKeyUtil.getRedisKey(dictionaryItem.getCode(), code);
                    dictionaryRedisService.set(redisKey, value);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public void deleteNotice(String itemCode, String code, DictionaryTypeEnum type) {
        DictionaryItem dictionaryItem = dictionaryItemMapper.selectOne(new LambdaQueryWrapper<DictionaryItem>()
                                                                               .eq(DictionaryItem::getCode, itemCode).last("limit 1"));
        if (ObjectUtils.isNotEmpty(dictionaryItem) && ObjectUtils.isNotEmpty(dictionaryItem.getChangeNotice())) {
            String[] changeNotice = dictionaryItem.getChangeNotice().split(",");
            for (String ex : changeNotice) {
                if (ChangeNoticeEnum.ROCKET_MQ.name().equals(ex)) {
                    // 发送MQ消息
                    RocketMQContextHolder.send(DictionaryDeleteMessage.TOPIC, DictionaryDeleteMessage.builder()
                            .itemCode(itemCode)
                            .code(code)
                            .tyep(type.name())
                            .build());
                } else if (ChangeNoticeEnum.REDIS.name().equals(ex)) {
                    // 删除redis缓存
                    String redisKey = type.equals(DictionaryTypeEnum.USER_DICTIONARY)
                                      ? DictionaryRedisKeyUtil.getRedisKeyLimitUser(dictionaryItem.getCode(), code)
                                      : DictionaryRedisKeyUtil.getRedisKey(dictionaryItem.getCode(), code);
                    dictionaryRedisService.delete(redisKey);
                }
            }
        }
    }

    @Override
    public void refreshCache(DictionaryItem dictionaryItem, String code, String value, DictionaryTypeEnum type) {
        if (Objects.isNull(dictionaryItem) || StringUtils.isBlank(dictionaryItem.getChangeNotice())) {
            return;
        }
        if (!dictionaryItem.getChangeNotice().contains(ChangeNoticeEnum.REDIS.name())) {
            return;
        }
        String redisKey = type.equals(DictionaryTypeEnum.USER_DICTIONARY)
                          ? DictionaryRedisKeyUtil.getRedisKeyLimitUser(dictionaryItem.getCode(), code)
                          : DictionaryRedisKeyUtil.getRedisKey(dictionaryItem.getCode(), code);
        dictionaryRedisService.set(redisKey, value);
    }
}
