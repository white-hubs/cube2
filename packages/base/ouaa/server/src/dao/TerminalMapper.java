package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import org.apache.ibatis.annotations.Mapper;

/**
 * 访问端数据操作接口
 *
 * @author Woody
 * @date 2021/7/16
 */
@Mapper
public interface TerminalMapper extends BaseMapper<Terminal> {
}
