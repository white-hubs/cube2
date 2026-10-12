package com.guhecloud.cube.srv.gen.maintain.server.electron.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolPlan;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.KeyValueVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PatrolPlanMapper extends BaseMapper<PatrolPlan> {
    List<KeyValueVO> getPalnStatus();

    void insertOrUpdateBatch(List<PatrolPlan> list);
}
