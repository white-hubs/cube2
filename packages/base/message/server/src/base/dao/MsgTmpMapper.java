package com.guhecloud.cube.base.message2.server.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.message2.server.base.entity.MsgTmp;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息模板接口
 *
 * @author lanchun
 * @date 2021/11/9 19:13
 */
@Mapper
public interface MsgTmpMapper extends BaseMapper<MsgTmp> {
}
