package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.dto.AttClassesVersionDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 考勤班次mapper
 */
@Mapper
public interface AttClassesRecordMapper extends BaseMapper<AttClassesRecordPO> {

    /**
     * 查询班次对应的最新版本
     *
     * @param wrapper 条件
     *
     * @return 班次id对应最大班次集合
     */
    @Select("select acr.classes_id as id,max(acr.version) as version from att_classes_record acr ${ew.customSqlSegment} group by acr.classes_id ")
    List<AttClassesVersionDTO> getClassMaxVersion(@Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
