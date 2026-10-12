package com.guhecloud.cube.base.message2.server.sat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.sat.constants.SatMsgConstant;
import com.guhecloud.cube.base.message2.server.sat.dao.SatUserMsgIgnoredConfigMapper;
import com.guhecloud.cube.base.message2.server.sat.dto.SatUserMsgIgnoredConfigDTO;
import com.guhecloud.cube.base.message2.server.sat.entity.SatUserMsgIgnoredConfig;
import com.guhecloud.cube.base.message2.server.sat.param.SatUserMsgIgnoredConfigParam;
import com.guhecloud.cube.base.message2.server.sat.param.validator.SatUserMsgIgnoredConfigValidator;
import com.guhecloud.cube.base.message2.server.sat.service.SatUserMsgIgnoredConfigService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.base.message2.server.sat.constants.SatMsgConstant.USER_MSG_IGNORED_REDIS_KEY;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatUserMsgIgnoredConfigServiceImpl extends ServiceImpl<SatUserMsgIgnoredConfigMapper, SatUserMsgIgnoredConfig> implements SatUserMsgIgnoredConfigService {

    private final SatUserMsgIgnoredConfigValidator satUserMsgIgnoredConfigValidator;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull Long userId, @NonNull SatUserMsgIgnoredConfigParam param) {
        //前置校验
        satUserMsgIgnoredConfigValidator.verifyUserMsgIgnoredConfig(param);
        this.remove(userId);
        //新增
        SatUserMsgIgnoredConfig satUserMsgIgnoredConfig = SatUserMsgIgnoredConfig.builder()
                .userId(userId)
                .enabled(param.getEnabled())
                .startTime(param.getStartTime())
                .endTime(param.getEndTime())
                .tomorrows(param.getTomorrows())
                .build();
        if (!CollectionUtils.isEmpty(param.getChannels())) {
            satUserMsgIgnoredConfig.setChannels(param.getChannels().stream().map(ChannelEnum::name).collect(Collectors.joining(",")));
        }
        this.baseMapper.insert(satUserMsgIgnoredConfig);
        if (Objects.equals(Boolean.TRUE, param.getEnabled())) {
            this.redisTemplate.opsForHash()
                    .put(SatMsgConstant.getTenantRedisKey(USER_MSG_IGNORED_REDIS_KEY),
                         userId.toString(),
                         this.buildSatUserMsgIgnoredConfigDTO(satUserMsgIgnoredConfig));
        }
        return satUserMsgIgnoredConfig.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SatUserMsgIgnoredConfig getSatUserMsgIgnoredConfig(@NonNull Long userId) {
        return this.baseMapper.selectOne(Wrappers.<SatUserMsgIgnoredConfig>lambdaQuery()
                                                 .eq(SatUserMsgIgnoredConfig::getUserId, userId)
                                        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnabled(@NonNull Long userId, @NonNull Boolean enabled) {
        SatUserMsgIgnoredConfig satUserMsgIgnoredConfig = this.getSatUserMsgIgnoredConfig(userId);
        if (Objects.nonNull(satUserMsgIgnoredConfig)) {
            satUserMsgIgnoredConfig.setEnabled(enabled);
            this.updateById(satUserMsgIgnoredConfig);
            if (Objects.equals(Boolean.TRUE, enabled)) {
                this.redisTemplate.opsForHash()
                        .put(SatMsgConstant.getTenantRedisKey(USER_MSG_IGNORED_REDIS_KEY),
                             userId.toString(),
                             this.buildSatUserMsgIgnoredConfigDTO(satUserMsgIgnoredConfig));
            } else {
                this.redisTemplate.opsForHash().delete(SatMsgConstant.getTenantRedisKey(USER_MSG_IGNORED_REDIS_KEY), userId.toString());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long userId) {
        this.redisTemplate.opsForHash().delete(SatMsgConstant.getTenantRedisKey(USER_MSG_IGNORED_REDIS_KEY), userId.toString());
        SatUserMsgIgnoredConfig satUserMsgIgnoredConfig = this.getSatUserMsgIgnoredConfig(userId);
        if (Objects.nonNull(satUserMsgIgnoredConfig)) {
            DatabaseContextHolder.softDelete(this, satUserMsgIgnoredConfig);
        }
    }

    private SatUserMsgIgnoredConfigDTO buildSatUserMsgIgnoredConfigDTO(SatUserMsgIgnoredConfig satUserMsgIgnoredConfig) {
        return SatUserMsgIgnoredConfigDTO.builder()
                .id(satUserMsgIgnoredConfig.getId())
                .userId(satUserMsgIgnoredConfig.getUserId())
                .enabled(satUserMsgIgnoredConfig.getEnabled())
                .startTime(JsonUtils.serialize(satUserMsgIgnoredConfig.getStartTime()))
                .endTime(JsonUtils.serialize(satUserMsgIgnoredConfig.getEndTime()))
                .tomorrows(satUserMsgIgnoredConfig.getTomorrows())
                .channels(satUserMsgIgnoredConfig.getChannels())
                .build();
    }
}
