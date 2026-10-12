package com.guhecloud.cube.srv.gen.maintain.server.common.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.common.config.mapper.DeDuplicationConfigMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/9/19 10:24
 * @description
 */
@Service
public class DeDuplicationConfigService extends ServiceImpl<DeDuplicationConfigMapper, DeDuplicationConfig> {

    public void change(DeDuplicationConfig deDuplicationConfig) {
        // 删除数据
        remove(
                Wrappers.<DeDuplicationConfig>lambdaQuery()
                        .eq(DeDuplicationConfig::getBusinessType, deDuplicationConfig.getBusinessType())
              );
        // 重新加入
        save(deDuplicationConfig);
    }

    public DeDuplicationConfig get(String businessType) {
        DeDuplicationConfig duplicationConfig = getOne(
                Wrappers.<DeDuplicationConfig>lambdaQuery()
                        .eq(DeDuplicationConfig::getBusinessType, businessType)
                                                      );
        return duplicationConfig;
    }

    public boolean isInDeDuplicationPeriod(Integer deDuplicationPeriod, Integer duration, InDeDuplicationPeriod inDeDuplicationPeriod) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.plusHours(deDuplicationPeriod * -1L);

        boolean isInDeDuplicationPeriod = inDeDuplicationPeriod.isInDeDuplicationPeriod(startTime, endTime);

        boolean isInTimeDuration = inDeDuplicationPeriod.isInTimeDuration(duration);

        if (isInDeDuplicationPeriod && isInTimeDuration) {
            return true;
        }
        return false;
    }

}
