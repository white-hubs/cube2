package com.guhecloud.cube.base.message2.server.sat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceive;
import com.guhecloud.cube.base.message2.server.sat.vo.SatMsgApplicationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SatMsgSceneReceiveMapper extends BaseMapper<SatMsgSceneReceive> {
    void insertBatch(@Param("satMsgSceneReceives") List<SatMsgSceneReceive> satMsgSceneReceives);

    List<SatMsgApplicationVO> selectSatMsgSceneApplications();
}
