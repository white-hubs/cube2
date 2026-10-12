package com.guhecloud.cube.base.dataintegrate.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.AlarmPushConfig;
import org.springframework.stereotype.Repository;

/**
 * 描述：报警数据采集配置 Repository接口
 *
 * @author Appet
 * @date 2023-09-13 15:08:36
 */
@Repository("di-alarmconfig-mapper")
public interface AlarmPushConfigMapper extends BaseMapper<AlarmPushConfig> {

}