package com.guhecloud.cube.base.message2.server.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.message2.server.base.entity.MsgChannel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息通道接口
 *
 * @author lanchun
 * @date 2021/11/9 18:59
 */
@Mapper
public interface MsgChannelMapper extends BaseMapper<MsgChannel> {
}
