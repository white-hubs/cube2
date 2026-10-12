package com.guhecloud.cube.iot.mgt.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.iot.mgt.base.model.IotSysUnitEntity;
import com.guhecloud.cube.iot.mgt.base.vo.IotSysUnitVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IotSysUnitMapper extends BaseMapper<IotSysUnitEntity> {

    List<IotSysUnitVo> queryAll();

    Integer checkIsUsed(@Param("id")Long unitId);

    IotSysUnitEntity queryByUnitCode(@Param("unitCode")String unitCode);
}