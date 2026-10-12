package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRepairRelationPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InspectRepairRelationMapper extends BaseMapper<InspectRepairRelationPO> {

    void insertByRecordIds(List<Long> recordIds);

    void insertBatch(List<InspectRepairRelationPO> list);
}
