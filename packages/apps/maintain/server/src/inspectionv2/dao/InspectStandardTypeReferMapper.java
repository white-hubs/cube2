package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardTypeReferPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 巡检标准与巡检对象关联，类型包括：设备、挡位类型、等 Mapper 接口
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Mapper
public interface InspectStandardTypeReferMapper extends BaseMapper<InspectStandardTypeReferPO> {

}
