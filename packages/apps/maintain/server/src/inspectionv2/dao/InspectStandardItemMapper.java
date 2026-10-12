package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.CnpInspectItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 巡检标准中的巡检项 Mapper 接口
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Mapper
public interface InspectStandardItemMapper extends BaseMapper<InspectStandardItemPO> {

    @Select("select * from cnp_inspect_item")
    List<CnpInspectItemVO> getCnpInspectItem();
}
