package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Card;
import org.apache.ibatis.annotations.Mapper;

/**
 * 卡片操作接口
 *
 * @author lanchun
 * @date 2022/5/23 15:51
 */
@Mapper
public interface CardMapper extends BaseMapper<Card> {
}
