package com.guhecloud.cube.biz.fusion.communicate.server.gps.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.po.UserGpsHistoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserGpsHistoryMapper extends BaseMapper<UserGpsHistoryPO> {

    UserGpsHistoryPO selectUserLastGpsHistory(@Param("userId") Long userId);

    List<UserGpsHistoryPO> selectUserLastGpsHistories(@Param("userIds") List<Long> userIds);
}
