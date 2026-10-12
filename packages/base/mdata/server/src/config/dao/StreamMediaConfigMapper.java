package com.guhecloud.cube.biz.mdata.server.config.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.mdata.server.config.entity.StreamMediaConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流媒体服务配置mapper接口
 *
 * @author lanchun
 * @date 2021/12/7 15:10
 */
@Mapper
public interface StreamMediaConfigMapper extends BaseMapper<StreamMediaConfig> {
}
