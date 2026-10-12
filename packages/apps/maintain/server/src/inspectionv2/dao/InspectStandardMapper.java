package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.CnpInspectStandardTypeReferVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.CnpInspectStandardVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStandardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 巡检标准 Mapper 接口
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Mapper
public interface InspectStandardMapper extends BaseMapper<InspectStandardPO> {

    @Select("select s.*,r.default_standard as defaultStandard from inspect_standard s " +
            "left join inspect_standard_type_refer r on s.id = r.standard_id ${ew.customSqlSegment}"
    )
    Page<InspectStandardVO> getPage(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select DISTINCT s.* from inspect_standard s " +
            "left join inspect_standard_type_refer r on s.id = r.standard_id ${ew.customSqlSegment}"
    )
    List<InspectStandardVO> getList(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select c.type_name from inspect_standard s " +
            "INNER JOIN inspect_category c on s.inspect_standard_type_id = c.id " +
            "where s.id = #{standardId}"
    )
    String getTypeNameByStandardId(@Param("standardId") Long id);

    @Select("select * from cnp_inspect_standard")
    List<CnpInspectStandardVO> getCnpInspectStandard();

    @Update("update cnp_inspect_standard_type_refer set refer_type_id=#{id} where refer_type_id=#{strId}")
    Integer updateCnpInspectStandard(@Param("strId") String strId, @Param("id") Long id);

    @Select("select * from cnp_inspect_standard_type_refer")
    List<CnpInspectStandardTypeReferVO> selectCnpInspectStandard();
}
