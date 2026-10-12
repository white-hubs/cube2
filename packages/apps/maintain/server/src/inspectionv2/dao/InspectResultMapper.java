package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectResultPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.CnpInspectResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 巡检结果  实际为巡检结果项  冗余了名称 Mapper 接口
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Mapper
public interface InspectResultMapper extends BaseMapper<InspectResultPO> {

    @Select("select * from fireinsp_record")
    List<CnpInspectResultVO> getCnpInspectResult();

}
