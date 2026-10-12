package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Client;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户端数据操作接口
 *
 * @author Woody
 * @date 2021/7/26
 */
@Mapper
public interface ClientMapper extends BaseMapper<Client> {
}
