package com.guhecloud.cube.base.dataintegrate.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyAppDevice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SafetyAppDeviceMapper extends BaseMapper<SafetyAppDevice> {

    void insertOrUpdateBatch(List<SafetyAppDevice> dataList);
}
