package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordListVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.param.InspectRecordItemTagCodeStatParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.param.InspectRecordPageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InspectRecordStatMapper extends BaseMapper<InspectRecordPO> {

    /**
     * 按标签统计巡检记录次数
     *
     * @param param
     *
     * @return
     */
    Integer selectInspectRecordItemTagCodeStat(@Param("param") InspectRecordItemTagCodeStatParam param);

    /**
     * 按巡检项标签查询巡检记录列表
     *
     * @param page
     * @param param
     *
     * @return
     */
    Page<InspectRecordListVO> selectInspectRecordPageByTagCode(Page page, @Param("param") InspectRecordPageParam param);
}
