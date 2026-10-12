package com.guhecloud.cube.srv.gen.visitor.management.server.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitorStatusChangeRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitorStatusChangeRecordResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 预约状态变更记录
 */
@Mapper
public interface VisitorStatusChangeRecordMapper extends BaseMapper<VisitorStatusChangeRecordPO> {

    /**
     * 访客通行趋势
     *
     * @return 趋势
     */
    @Select(" select DATE_FORMAT(vscr.`create_time` ,'%H') as `hour_str` ,count(vscr.`id`) as `count` " +
            " from `visitor_status_change_record` vscr " +
            " ${ew.customSqlSegment} " +
            " group by `hour_str` " +
            " order by `create_time` asc "
    )
    List<VisitorStatusChangeRecordResult> visitorPassTrend(@Param(Constants.WRAPPER) Wrapper<VisitorStatusChangeRecordPO> wrapper);
}
